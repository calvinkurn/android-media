package com.tokopedia.digital.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class OtpVerificationActivity extends BasePresenterActivity {
    public static final int REQUEST_CODE = OtpVerificationActivity.class.hashCode();
    public static final int RESULT_OTP_VERIFIED = 4;
    public static final int RESULT_OTP_UNVERIFIED = 5;

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
        return R.layout.activity_otp_verification_digital_module;
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

    }

    public static Intent newInstance(Context context) {
        return new Intent(context, OtpVerificationActivity.class);
    }
}
