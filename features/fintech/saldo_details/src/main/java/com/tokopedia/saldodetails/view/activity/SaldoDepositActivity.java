package com.tokopedia.saldodetails.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * For navigating to this class
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal#SALDO_DEPOSIT}
 */


public class SaldoDepositActivity extends BaseSimpleActivity implements
        HasComponent<SaldoDetailsComponent> {

    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final String TAG = "DEPOSIT_FRAGMENT";

    @Inject
    UserSession userSession;
    private boolean isSeller;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SaldoDetailsPresenter.REQUEST_WITHDRAW_CODE && resultCode == Activity.RESULT_OK) {
            if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
                finish();
            } else {
                ((SaldoDepositFragment) getSupportFragmentManager().findFragmentByTag(TAG)).refresh();
            }
        }
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == RESULT_OK) {
                inflateFragment();
            } else {
                finish();
            }
        }
    }

    private void initInjector() {
        SaldoDetailsComponentInstance.getComponent(getApplication()).inject(this);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SaldoDepositActivity.class);
    }

    @Override
    public SaldoDetailsComponent getComponent() {
        return SaldoDetailsComponentInstance.getComponent(getApplication());
    }

    @Override
    protected int getLayoutRes() {
        return com.tokopedia.saldodetails.R.layout.activity_saldo_deposit;
    }

    @Override
    protected Fragment getNewFragment() {

        if (userSession.isLoggedIn()) {
            return SaldoDepositFragment.createInstance(isSeller);
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
            return null;
        }

    }

    @Override
    protected int getParentViewResourceID() {
        return com.tokopedia.saldodetails.R.id.saldo_deposit_parent_view;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        initInjector();
        initializeView();
        setUpToolbar();
    }

    private void setUpToolbar() {
        toolbar = findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_toolbar);
        Drawable upArrow = ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_action_back);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, com.tokopedia.design.R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        } else {
            toolbar.setNavigationIcon(com.tokopedia.design.R.drawable.ic_icon_back_black);
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop(), getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_12), toolbar.getPaddingBottom());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
            updateTitle("");
        }

    }

    private void initializeView() {
        isSeller = userSession.hasShop() || userSession.isAffiliate();
        TextView saldoHelp = findViewById(com.tokopedia.saldodetails.R.id.toolbar_saldo_help);

        saldoHelp.setVisibility(View.VISIBLE);
        saldoHelp.setOnClickListener(v -> {
            RouteManager.route(this, ApplinkConstInternalGlobal.SALDO_INTRO);
        });
    }

    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}
