package com.tokopedia.digital.cart.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.PinEntryEditText;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.network.NetworkErrorHelper;
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
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class OtpVerificationActivity extends BasePresenterActivity<IOtpVerificationPresenter>
        implements IOtpVerificationView, IncomingSmsReceiver.ReceiveSMSListener {
    public static final int REQUEST_CODE = OtpVerificationActivity.class.hashCode();
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final int RESULT_OTP_VERIFIED = 4;
    public static final int RESULT_OTP_UNVERIFIED = 5;
    public static final int RESULT_OTP_CANCELED = 3;

    @BindView(R2.id.pb_main_loading)
    ProgressBar pbMainLoading;
    @BindView(R2.id.et_input_otp)
    PinEntryEditText etOtp;
    @BindView(R2.id.btn_verify_otp)
    TextView btnValidateOtp;
    @BindView(R2.id.tv_pin_error)
    TextView tvPinError;
    @BindView(R2.id.btn_request_otp)
    TextView btnRequestOtp;
    @BindView(R2.id.tv_message)
    TextView tvMessage;
    @BindView(R2.id.btn_request_otp_call)
    TextView btnRequestOtpCall;
    @BindView(R2.id.main_container)
    RelativeLayout mainContainer;

    private TkpdProgressDialog progressDialog;
    private CompositeSubscription compositeSubscription;
    private IncomingSmsReceiver incomingSmsReceiver;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        OtpRepository otpRepository = new OtpRepositoryImpl(new OtpSourceFactory(this));
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        presenter = new OtpVerificationPresenter(
                this,
                new OtpVerificationInteractor(
                        otpRepository, new CartMapperData(), compositeSubscription
                )
        );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otp_verification_digital_module;
    }

    @Override
    protected void initView() {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPinError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnValidateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processVerifyOtp();
            }
        });
    }

    @Override
    protected void initVar() {
        incomingSmsReceiver = new IncomingSmsReceiver();
        incomingSmsReceiver.setListener(this);
        incomingSmsReceiver.registerSmsReceiver(this);
    }

    @Override
    protected void setActionVar() {
        presenter.processFirstRequestSmsOtp();
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
    public void showInitialProgressLoading() {
        mainContainer.setVisibility(View.GONE);
        pbMainLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInitialProgressLoading() {
        mainContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void clearContentRendered() {
        pbMainLoading.setVisibility(View.GONE);
        mainContainer.setVisibility(View.GONE);
    }

    @Override
    public void showProgressLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {
        finish();
    }

    @Override
    public void renderSuccessReRequestSmsOtp(String message) {
        tvMessage.setText(message);
        tvPinError.setText("");
        tvPinError.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderErrorTimeoutReRequestSmsOtp(String messageErrorTimeout) {
        showToastMessage(messageErrorTimeout);
    }

    @Override
    public void renderErrorNoConnectionReRequestSmsOtp(String messageErrorNoConnection) {
        View view = findViewById(android.R.id.content);
        if (view != null)
            NetworkErrorHelper.showEmptyState(
                    this, view, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.processReRequestSmsOtp();
                        }
                    }
            );
        else showToastMessage(messageErrorNoConnection);
    }

    @Override
    public void renderErrorReRequestSmsOtp(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderErrorResponseReRequestSmsOtp(String message) {
        tvMessage.setText(message);
        tvPinError.setText("");
        tvPinError.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderSuccessFirstRequestSmsOtp(String message) {
        tvMessage.setText(message);
        tvPinError.setText("");
        tvPinError.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderErrorTimeoutFirstRequestSmsOtp(String messageErrorTimeout) {
        closeWithResultOtpCanceled(messageErrorTimeout);
    }

    @Override
    public void renderErrorNoConnectionFirstRequestSmsOtp(String messageErrorNoConnection) {
        View view = findViewById(android.R.id.content);
        if (view != null)
            NetworkErrorHelper.showEmptyState(
                    this, view, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.processFirstRequestSmsOtp();
                        }
                    }
            );
        else showToastMessage(messageErrorNoConnection);
    }

    @Override
    public void renderErrorFirstRequestSmsOtp(String message) {
        closeWithResultOtpCanceled(message);
    }

    @Override
    public void renderErrorResponseFirstRequestSmsOtp(String message) {
        tvMessage.setText(message);
        tvPinError.setText("");
        tvPinError.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderSuccessRequestCallOtp(String message) {
        tvMessage.setText(message);
        tvPinError.setText("");
        tvPinError.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderErrorTimeoutRequestCallOtp(String messageErrorTimeout) {
        showToastMessage(messageErrorTimeout);
    }

    @Override
    public void renderErrorNoConnectionRequestCallOtp(String messageErrorNoConnection) {
        View view = findViewById(android.R.id.content);
        if (view != null)
            NetworkErrorHelper.showEmptyState(
                    this, view, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.processRequestCallOtp();
                        }
                    }
            );
        else showToastMessage(messageErrorNoConnection);
    }

    @Override
    public void renderErrorRequestCallOtp(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderErrorResponseRequestCallOtp(String message) {
        tvMessage.setText(message);
        tvPinError.setText("");
        tvPinError.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        pbMainLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderSuccessVerifyOtp(String message) {
        closeWithResultOtpVerified(message);
    }

    @Override
    public void renderErrorTimeoutVerifyOtp(String messageErrorTimeout) {
        showToastMessage(messageErrorTimeout);
    }

    @Override
    public void renderErrorNoConnectionVerifyOtp(String messageErrorNoConnection) {
        View view = findViewById(android.R.id.content);
        if (view != null)
            NetworkErrorHelper.showEmptyState(
                    this, view, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.processVerifyOtp();
                        }
                    }
            );
        else showToastMessage(messageErrorNoConnection);
    }

    @Override
    public void renderErrorVerifyOtp(String message) {
        showToastMessage(message);
    }

    @Override
    public void renderErrorResponseVerifyOtp(String message) {
        tvPinError.setVisibility(View.VISIBLE);
        tvPinError.setText(message);
    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(this);
    }

    @Override
    public String getOtpCode() {
        return etOtp.getText().toString();
    }

    @Override
    public void onBackPressed() {
        closeWithResultOtpCanceled("Verifikasi kode keamanan dibatalkan");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(incomingSmsReceiver);
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }

    @OnClick(R2.id.btn_request_otp)
    void actionRequestOtp() {
        presenter.processReRequestSmsOtp();
    }

    @OnClick(R2.id.btn_request_otp_call)
    void actionRequestCallOtp() {
        presenter.processRequestCallOtp();
    }

    @OnClick(R2.id.btn_verify_otp)
    void actionVerifyOtp() {
        presenter.processVerifyOtp();
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        etOtp.setText(otpCode);
    }

    private void closeWithResultOtpVerified(String message) {
        Intent intent = new Intent().putExtra(EXTRA_MESSAGE, message);
        setResult(RESULT_OTP_VERIFIED, intent);
        finish();
    }

    private void closeWithResultOtpUnVerified(String message) {
        Intent intent = new Intent().putExtra(EXTRA_MESSAGE, message);
        setResult(RESULT_OTP_UNVERIFIED, intent);
        finish();
    }

    private void closeWithResultOtpCanceled(String message) {
        Intent intent = new Intent().putExtra(EXTRA_MESSAGE, message);
        setResult(RESULT_OTP_CANCELED, intent);
        finish();
    }
}
