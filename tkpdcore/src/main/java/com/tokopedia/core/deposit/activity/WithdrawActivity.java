package com.tokopedia.core.deposit.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.deposit.fragment.WithdrawFragment;
import com.tokopedia.core.deposit.presenter.WithdrawPresenter;

/**
 * Created by Nisie on 4/13/16.
 */
public class WithdrawActivity extends BasePresenterActivity<WithdrawPresenter> {

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEPOSIT_WITHDRAW;
    }

    public static Intent createInstance(Context context) {
        return new Intent(context, WithdrawActivity.class);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initView() {
        WithdrawFragment fragment = WithdrawFragment.createInstance(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
