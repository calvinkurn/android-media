package com.tokopedia.withdraw.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.view.fragment.WithdrawPasswordFragment;

import javax.inject.Inject;

public class WithdrawPasswordActivity extends BaseSimpleActivity {

    public final static String BUNDLE_BANK = "bank";
    public final static String BUNDLE_WITHDRAW = "withdraw";
    public final static String BUNDLE_IS_SELLER_WITHDRAWAL = "isSellerWithdrawal";

    @Inject
    WithdrawAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
        initInjector();
    }

    @Override
    protected void onStart() {
        super.onStart();
        analytics.sendScreen(this, getScreenName());
    }

    @Override
    public String getScreenName() {
        return WithdrawAnalytics.SCREEN_WITHDRAW_PASSWORD;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return WithdrawPasswordFragment.createInstance(bundle);
    }

    protected void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();

        /*DaggerDoWithdrawComponent.builder().withdrawComponent(withdrawComponent)
                .build().inject(this);*/
        withdrawComponent.inject(this);

    }

    private void setToolbar() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(MethodChecker.getColor(this, R.color.white)));
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.black_70));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop(), 30, toolbar.getPaddingBottom());

        toolbar.setTitleTextAppearance(this, R.style.WebViewToolbarText);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_close);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, WithdrawPasswordActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    public void onBackPressed() {
        analytics.eventClickX();
        super.onBackPressed();
    }
}
