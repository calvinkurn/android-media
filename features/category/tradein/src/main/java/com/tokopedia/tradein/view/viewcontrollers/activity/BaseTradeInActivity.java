package com.tokopedia.tradein.view.viewcontrollers.activity;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.TradeInGTMConstants;
import com.tokopedia.tradein.view.viewcontrollers.ContextInterface;
import com.tokopedia.tradein.viewmodel.BaseTradeInViewModel;
import com.tokopedia.tradein.di.DaggerTradeInComponent;
import com.tokopedia.tradein.di.TradeInComponent;
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity;
import com.tokopedia.basemvvm.viewmodel.BaseViewModel;
import com.tokopedia.unifycomponents.Toaster;

import org.jetbrains.annotations.NotNull;

import com.tokopedia.common_tradein.utils.TradeInUtils;

import javax.inject.Inject;

public abstract class BaseTradeInActivity<T extends BaseTradeInViewModel> extends BaseViewModelActivity<T> implements ContextInterface, HasComponent<TradeInComponent> {
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    public static final int LOGIN_REQUEST = 514;
    public static final int TRADEIN_HOME_REQUEST = 22345;
    public static final int APP_SETTINGS = 9988;
    public static final int TRADEIN_OFFLINE = 0;
    public static final int TRADEIN_ONLINE = 1;
    public static final int TRADEIN_MONEYIN = 2;
    public static final String TRADEIN_EXCHANGE = "exchange";
    public static final String TRADEIN_MONEY_IN = "money-in";
    public static final String TRADEIN_TNC_URL = "https://www.tokopedia.com/help/article/a-1789";
    public static final String MONEYIN_TNC_URL = "https://www.tokopedia.com/help/article/st-2135-syarat-dan-ketentuan-langsung-laku";
    protected String TRADEIN_TEST_TYPE = TRADEIN_EXCHANGE;
    protected int TRADEIN_TYPE = TRADEIN_OFFLINE;
    protected String tncUrl = TRADEIN_TNC_URL;
    protected String clickEvent = TradeInGTMConstants.ACTION_CLICK_TRADEIN;
    protected String viewEvent = TradeInGTMConstants.ACTION_VIEW_TRADEIN;
    protected BaseTradeInViewModel TradeVM;

    abstract protected int getMenuRes();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

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
                tncUrl = MONEYIN_TNC_URL;
                event = TradeInGTMConstants.ACTION_CLICK_MONEYIN;
                category = TradeInGTMConstants.CATEGORY_MONEYIN_PRICERANGE_PAGE;
            }
            sendGeneralEvent(event,
                    category,
                    TradeInGTMConstants.ACTION_CLICK_ICON_TNC,
                    "");

            showTnC(tncUrl);
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
        super.onCreate(savedInstanceState);
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
        ((BaseTradeInViewModel) TradeVM).setContextInterface(this);
        if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
            toolbar.setTitle(R.string.money_in);
            TRADEIN_TEST_TYPE = TRADEIN_MONEY_IN;
            clickEvent = TradeInGTMConstants.ACTION_CLICK_MONEYIN;
            viewEvent = TradeInGTMConstants.ACTION_VIEW_MONEYIN;
            tncUrl = MONEYIN_TNC_URL;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(com.tokopedia.design.R.drawable.ic_icon_back_black);
        }
        TradeVM.getProgBarVisibility().observe(this, (visibility) -> {
            if (visibility != null) {
                if (visibility)
                    showProgressBar();
                else
                    hideProgressBar();
            }
        });

        TradeVM.getWarningMessage().observe(this, (message) -> {
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
        TradeVM.getErrorMessage().observe(this, (message) -> {
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
        return getString(com.tokopedia.design.R.string.close);
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


    protected void showTnC(String url) {
        RouteManager.route(this, url);
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
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
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
    public Context getContextFromActivity() {
        return this;
    }
    @Override
    public TradeInComponent getComponent() {
        return DaggerTradeInComponent.builder().
                baseAppComponent(((BaseMainApplication) getApplication()).
                        getBaseAppComponent()).
                build();
    }

    @NotNull
    @Override
    public ViewModelProvider.Factory getVMFactory() {
        return viewModelFactory;
    }
}
