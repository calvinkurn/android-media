package com.tokopedia.tkpd.ride;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationFragment;
import com.tokopedia.tkpd.R;

public class RidePhoneNumberVerificationActivity extends BasePresenterActivity {
    public static final int RIDE_PHONE_VERIFY_REQUEST_CODE = 1011;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, RidePhoneNumberVerificationActivity.class);
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
        return R.layout.activity_ride_phone_number_verification;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        addFragment(R.id.container, PhoneVerificationFragment.createInstance());
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}
