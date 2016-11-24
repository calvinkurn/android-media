package com.tokopedia.core.msisdn.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.msisdn.fragment.MsisdnFragment;
import com.tokopedia.core.session.Login;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

/**
 * Created by nisie on 11/7/16.
 */

public class MsisdnActivity extends BasePresenterActivity {

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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            MsisdnFragment fragment = MsisdnFragment.createInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
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
    public String getScreenName() {
        return AppScreen.SCREEN_PHONE_VERIFICATION;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.SELLER_HOME);
        startActivity(intent);
        SessionHandler.clearUserData(this);

        super.onBackPressed();

        finish();
    }
}