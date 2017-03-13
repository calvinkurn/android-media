package com.tokopedia.digital.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.tkpd.library.ui.widget.PinEntryEditText;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class OtpVerificationActivity extends BasePresenterActivity {
    public static final int REQUEST_CODE = OtpVerificationActivity.class.hashCode();
    public static final int RESULT_OTP_VERIFIED = 4;
    public static final int RESULT_OTP_UNVERIFIED = 5;

    @BindView(R2.id.et_input_otp)
    PinEntryEditText etOtp;
    @BindView(R2.id.btn_verify_otp)
    TextView btnValidateOtp;

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
        Bundle bundle = new Bundle();

        Map<String, String> param = new HashMap<>();
        param.put("mode", "sms");
        param.put("otp_type", "16");
        Observable<Response<String>> observable = new AccountsService(bundle).getApi()
                .requestOTP(AuthUtil.generateParams(this, param));
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {

                    }
                });
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
