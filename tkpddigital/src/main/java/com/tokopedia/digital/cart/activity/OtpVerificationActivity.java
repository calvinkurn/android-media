package com.tokopedia.digital.cart.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.ui.widget.PinEntryEditText;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.otp.data.factory.OtpSourceFactory;
import com.tokopedia.core.otp.data.repository.OtpRepositoryImpl;
import com.tokopedia.core.otp.domain.OtpRepository;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.cart.data.mapper.CartMapperData;
import com.tokopedia.digital.cart.interactor.OtpVerificationInteractor;
import com.tokopedia.digital.cart.listener.IOtpVerificationView;
import com.tokopedia.digital.cart.presenter.IOtpVerificationPresenter;
import com.tokopedia.digital.cart.presenter.OtpVerificationPresenter;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class OtpVerificationActivity extends BasePresenterActivity<IOtpVerificationPresenter>
        implements IOtpVerificationView {
    public static final int REQUEST_CODE = OtpVerificationActivity.class.hashCode();
    public static final int RESULT_OTP_VERIFIED = 4;
    public static final int RESULT_OTP_UNVERIFIED = 5;

    @BindView(R2.id.et_input_otp)
    PinEntryEditText etOtp;
    @BindView(R2.id.btn_verify_otp)
    TextView btnValidateOtp;
    @BindView(R2.id.btn_request_otp)
    TextView btnRequestOtp;
    @BindView(R2.id.tv_message)
    TextView tvMessage;
    @BindView(R2.id.btn_request_otp_call)
    TextView btnRequestOtpCall;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        OtpRepository otpRepository = new OtpRepositoryImpl(new OtpSourceFactory(this));
        presenter = new OtpVerificationPresenter(
                this, new OtpVerificationInteractor(otpRepository, new CartMapperData())
        );
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
        btnValidateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processVerifyOtp();
            }
        });
    }

    @Override
    protected void initVar() {
        presenter.processRequestOtp();
    }

    @Override
    protected void setActionVar() {

    }

    public static Intent newInstance(Context context) {
        return new Intent(context, OtpVerificationActivity.class);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void renderSuccessRequestOtp(String message) {

    }

    @Override
    public void renderErrorRequestOtp(String message) {

    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(this);
    }

    @Override
    public String getOtpCode() {
        return etOtp.getText().toString();
    }

}
