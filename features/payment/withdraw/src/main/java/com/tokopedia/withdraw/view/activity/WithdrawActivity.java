package com.tokopedia.withdraw.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.constant.WithdrawConstant;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.view.WithdrawalFragmentCallback;
import com.tokopedia.withdraw.view.fragment.SuccessFragmentWithdrawal;
import com.tokopedia.withdraw.view.fragment.WithdrawFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * For navigating to this class
 *
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal
 */
public class WithdrawActivity extends BaseSimpleActivity implements WithdrawalFragmentCallback {


    public static final String IS_SELLER = "is_seller";
    public static final String TAG_SUCCESS_FRAGMENT = "TAG_SUCCESS_FRAGMENT";
    @Inject
    WithdrawAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
        initInjector();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_saldo_withdraw;
    }

    @Override
    protected void onStart() {
        super.onStart();
        analytics.sendScreen(this, getScreenName());
    }

    private void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();

        withdrawComponent.inject(this);
    }

    @Override
    public String getScreenName() {
        return WithdrawAnalytics.SCREEN_WITHDRAW;
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(MethodChecker.getColor(this, R.color.white)));
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.black_70));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop(), 30, toolbar.getPaddingBottom());
        toolbar.setTitleTextAppearance(this, R.style.WebViewToolbarText);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_action_back);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }


    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return WithdrawFragment.createInstance(bundle);
    }

    public static Intent getCallingIntent(Context context, boolean isSeller) {
        Intent intent = new Intent(context, WithdrawActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_SELLER, isSeller);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onBackPressed() {
        setResultIfSuccessFragment();
        analytics.eventClickBackArrow();
        super.onBackPressed();
    }

    private void setResultIfSuccessFragment() {
        if (getSupportFragmentManager().findFragmentByTag(TAG_SUCCESS_FRAGMENT) != null) {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
        }
    }

    @Override
    public void openSuccessFragment(@NotNull BankAccount bankAccount, @NotNull String message, long amount) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WithdrawConstant.Keys.BANK_ACCOUNT, bankAccount);
        bundle.putString(WithdrawConstant.Keys.MESSAGE, message);
        bundle.putLong(WithdrawConstant.Keys.AMOUNT, amount);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.parent_view, SuccessFragmentWithdrawal.getInstance(bundle), TAG_SUCCESS_FRAGMENT);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
