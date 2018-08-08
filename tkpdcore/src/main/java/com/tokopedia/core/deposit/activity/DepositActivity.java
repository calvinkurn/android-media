package com.tokopedia.core.deposit.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.deposit.fragment.DepositFragment;
import com.tokopedia.core.deposit.presenter.DepositFragmentPresenterImpl;
import com.tokopedia.core.deposit.presenter.DepositPresenter;
import com.tokopedia.core.deposit.presenter.DepositPresenterImpl;

/**
 * Created by Nisie on 3/30/16.
 */
public class DepositActivity extends BasePresenterActivity<DepositPresenter> {

    private static final String TAG = "DEPOSIT_FRAGMENT";

    public static Intent createInstance(Context context) {
        return new Intent(context, DepositActivity.class);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEPOSIT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new DepositPresenterImpl();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit;
    }

    @Override
    protected void initView() {
        DepositFragment fragment = DepositFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DepositFragmentPresenterImpl.REQUEST_WITHDRAW_CODE && resultCode == Activity.RESULT_OK){
            ((DepositFragment)getFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
