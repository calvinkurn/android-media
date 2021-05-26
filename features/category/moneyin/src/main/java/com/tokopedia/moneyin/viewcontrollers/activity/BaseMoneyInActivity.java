package com.tokopedia.moneyin.viewcontrollers.activity;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
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
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.track.TrackApp;
import com.tokopedia.moneyin.R;
import com.tokopedia.moneyin.MoneyInGTMConstants;
import com.tokopedia.moneyin.di.DaggerMoneyInComponent;
import com.tokopedia.moneyin.viewcontrollers.ContextInterface;
import com.tokopedia.moneyin.viewmodel.BaseTradeInViewModel;
import com.tokopedia.moneyin.di.MoneyInComponent;
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity;
import com.tokopedia.basemvvm.viewmodel.BaseViewModel;
import com.tokopedia.unifycomponents.Toaster;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public abstract class BaseMoneyInActivity<T extends BaseTradeInViewModel> extends BaseViewModelActivity<T> implements ContextInterface, HasComponent<MoneyInComponent> {
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    public static final int LOGIN_REQUEST = 514;
    public static final int APP_SETTINGS = 9988;
    public static final String MONEYIN_TNC_URL = "https://www.tokopedia.com/help/article/st-2135-syarat-dan-ketentuan-langsung-laku";
    protected BaseTradeInViewModel TradeVM;
    protected String clickEvent = MoneyInGTMConstants.ACTION_CLICK_MONEYIN;
    protected String viewEvent = MoneyInGTMConstants.ACTION_VIEW_MONEYIN;

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
            sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                    MoneyInGTMConstants.CATEGORY_MONEYIN_PRICERANGE_PAGE,
                    MoneyInGTMConstants.ACTION_CLICK_ICON_TNC,
                    "");

            showTnC(MONEYIN_TNC_URL);
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
        ((BaseTradeInViewModel) TradeVM).setContextInterface(this);
        toolbar.setTitle(R.string.money_in);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24);
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
        return getString(R.string.money_in_close);
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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

    @Override
    public Context getContextFromActivity() {
        return this;
    }
    @Override
    public MoneyInComponent getComponent() {
        return DaggerMoneyInComponent.builder().
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
