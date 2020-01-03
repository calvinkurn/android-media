package com.tokopedia.tradein.view.viewcontrollers;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.TradeInGTMConstants;
import com.tokopedia.tradein.viewmodel.BaseTradeInViewModel;
import com.tokopedia.tradein.viewmodel.TradeInVMFactory;
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity;
import com.tokopedia.basemvvm.viewmodel.BaseViewModel;
import com.tokopedia.unifycomponents.Toaster;

import org.jetbrains.annotations.NotNull;

import com.tokopedia.common_tradein.utils.TradeInUtils;


public abstract class BaseTradeInActivity<T extends BaseTradeInViewModel> extends BaseViewModelActivity<T> {
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    public static final int LOGIN_REQUEST = 514;
    public static final int TRADEIN_HOME_REQUEST = 22345;
    public static final int APP_SETTINGS = 9988;
    public static final int TRADEIN_OFFLINE = 0;
    public static final int TRADEIN_ONLINE = 1;
    public static final int TRADEIN_MONEYIN = 2;
    public static final String TRADEIN_EXCHANGE = "exchange";
    public static final String TRADEIN_MONEY_IN = "money-in";
    protected String TRADEIN_TEST_TYPE = TRADEIN_EXCHANGE;
    protected int TRADEIN_TYPE = TRADEIN_OFFLINE;
    protected int tncStringId;
    protected String clickEvent = TradeInGTMConstants.ACTION_CLICK_TRADEIN;
    protected String viewEvent = TradeInGTMConstants.ACTION_VIEW_TRADEIN;
    protected BaseTradeInViewModel TradeVM;

    abstract protected Fragment getTncFragmentInstance(int TncResId);

    protected boolean isTncShowing = false;
    abstract protected int getMenuRes();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() != -1) {
            getMenuInflater().inflate(getMenuRes(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_show_tnc) {

            String event = TradeInGTMConstants.ACTION_CLICK_TRADEIN;
            String category = TradeInGTMConstants.CATEGORY_TRADEIN_START_PAGE;
            if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
                event = TradeInGTMConstants.ACTION_CLICK_MONEYIN;
                category = TradeInGTMConstants.CATEGORY_MONEYIN_PRICERANGE_PAGE;
            }
            sendGeneralEvent(event,
                    category,
                    TradeInGTMConstants.ACTION_CLICK_ICON_TNC,
                    "");

            showTnC(tncStringId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void sendGeneralEvent(String event, String category, String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(event,
                category,
                action,
                label);
    }

    @NotNull
    @Override
    public abstract Class<T> getViewModelType();


    @Override
    public abstract void setViewModel(@NotNull BaseViewModel viewModel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TradeVM = ViewModelProviders.of(this, getVMFactory()).get(getViewModelType());
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null && uri.toString().contains("money_in/device_validation")) {
            TRADEIN_TYPE = TRADEIN_MONEYIN;
        } else if (uri != null && uri.toString().contains("trade_in/device_validation")) {
            TRADEIN_TYPE = TRADEIN_MONEYIN;
        } else {
            if (intent.hasExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE))
                TRADEIN_TYPE = intent.getIntExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, TRADEIN_OFFLINE);
        }
        super.onCreate(savedInstanceState);
        if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
            toolbar.setTitle(R.string.money_in);
            TRADEIN_TEST_TYPE = TRADEIN_MONEY_IN;
            clickEvent = TradeInGTMConstants.ACTION_CLICK_MONEYIN;
            viewEvent = TradeInGTMConstants.ACTION_VIEW_MONEYIN;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back_black);
        }
        TradeVM.getProgressBarVisibility().observe(this, (visibility) -> {
            if (visibility != null) {
                if (visibility)
                    showProgressBar();
                else
                    hideProgressBar();
            }
        });

        TradeVM.getWarningmessage().observe(this, (message) -> {
            hideProgressBar();
            if (!TextUtils.isEmpty(message)) {
                try {
                    Toaster.INSTANCE.showError(this.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        TradeVM.getErrormessage().observe(this, (message) -> {
            hideProgressBar();
            if (!TextUtils.isEmpty(message)) {
                try {
                    Toaster.INSTANCE.showErrorWithAction(this.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG, getButtonStringOnError(), (v) -> retryOnError());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void retryOnError() {
    }

    private String getButtonStringOnError() {
        return getString(R.string.close);
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
            clickEvent = TradeInGTMConstants.ACTION_CLICK_MONEYIN;
            viewEvent = TradeInGTMConstants.ACTION_VIEW_MONEYIN;
        }
    }

    protected String getDeviceId() {
        return TradeInUtils.getDeviceId(this);
    }

    @NotNull
    @Override
    public ViewModelProvider.AndroidViewModelFactory getVMFactory() {
        return TradeInVMFactory.getInstance(this.getApplication(), getIntent());
    }


    protected void showTnC(int tncResId) {
        isTncShowing = true;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
            getSupportActionBar().setTitle("Syarat dan Ketentuan");
        }

        Fragment fragment = getTncFragmentInstance(tncResId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack("TNC");
        transaction.replace(getRootViewId(), fragment);
        transaction.commit();
    }

    public int getRootViewId() {
        return R.id.root_view;
    }

    public View getRootView() {
        return findViewById(R.id.root_view);
    }

    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.white));
        }
    }


    protected void showMessageWithAction(String message, String actionText, View.OnClickListener listener) {
        Toaster.INSTANCE.showErrorWithAction(this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_INDEFINITE, actionText, listener);

    }

    protected void showMessage(String message) {
        Toaster.INSTANCE.showError(this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG);
    }

    protected void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    protected void showProgressBar() {
        getRootView().findViewById(R.id.progress_bar_layout).setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        getRootView().findViewById(R.id.progress_bar_layout).setVisibility(View.GONE);
    }

    protected String getMeGQlString(int rawResId){
        return GraphqlHelper.loadRawString(getResources(), rawResId);
    }

    @Override
    public void onBackPressed() {
        if (isTncShowing) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back_black);
                getSupportActionBar().setTitle(getTitle());
            }
            isTncShowing = false;
        }
        super.onBackPressed();
    }

}
