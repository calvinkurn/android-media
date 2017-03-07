package com.tokopedia.core.msisdn.fragment;

import android.Manifest;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.msisdn.MSISDNConstant;
import com.tokopedia.core.msisdn.listener.MsisdnVerificationFragmentView;
import com.tokopedia.core.msisdn.presenter.MsisdnVerificationFragmentPresenter;
import com.tokopedia.core.msisdn.presenter.MsisdnVerificationFragmentPresenterImpl;
import com.tokopedia.core.util.PhoneVerificationUtil;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Nisie on 7/13/16.
 */
@RuntimePermissions
public class MsisdnVerificationFragment extends DialogFragment
        implements MsisdnVerificationFragmentView, IncomingSmsReceiver.ReceiveSMSListener, MSISDNConstant {

    private static final String FORMAT = "%02d:%02d";
    private static final java.lang.String PHONE_NUMBER_VERIFICATION = "PHONE_NUMBER_VERIFICATION";

    @BindView(R2.id.view_verification)
    View verificationView;

    @BindView(R2.id.view_phone_number)
    View phoneNumberView;

    @BindView(R2.id.send_otp)
    Button sendOtp;

    @BindView(R2.id.verify_otp)
    Button verifyOtp;

    @BindView(R2.id.close_button)
    Button closeButton;

    @BindView(R2.id.input_phone)
    EditText phoneNumberEditText;

    @BindView(R2.id.input_otp)
    EditText otpEditText;

    @BindView(R2.id.logo)
    ImageView logo;

    @BindView(R2.id.btn_no_thanks)
    TextView noThanksButton;

    @BindView(R2.id.username)
    TextView username;

    @BindView(R2.id.verification_instruction)
    TextView instruction;

    MsisdnVerificationFragmentPresenter presenter;
    TkpdProgressDialog progressDialog;
    private PhoneVerificationUtil.MSISDNListener listener;
    LocalCacheHandler cacheHandler;
    private Unbinder unbinder;
    IncomingSmsReceiver smsReceiver;
    CountDownTimer countDownTimer;

    public static MsisdnVerificationFragment createInstance() {
        MsisdnVerificationFragment fragment = new MsisdnVerificationFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public MsisdnVerificationFragment() {
        this.smsReceiver = new IncomingSmsReceiver();
        this.smsReceiver.setListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("VerificationView", verificationView.getVisibility());
        outState.putBoolean("phoneNumberEnabled", phoneNumberEditText.isEnabled());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            phoneNumberEditText.setEnabled(savedInstanceState.getBoolean("phoneNumberEnabled"));
            setVisibilityVerificationView(savedInstanceState.getInt("VerificationView"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initialVar();
        initView(view);
        setViewListener();
        setActionVar();

        setCache();

    }

    private void setCache() {
        if (!cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_REQUEST_OTP, false)) {
            setFinishRequestOTP();
        }
    }

    private void setVisibilityVerificationView(int view) {
        verificationView.setVisibility(view);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        smsReceiver.registerSmsReceiver(getActivity());

        super.onResume();
    }

    @Override
    public void onPause() {
        if (smsReceiver != null)
            getActivity().unregisterReceiver(smsReceiver);
        super.onPause();

    }

    private void initialPresenter() {
        presenter = new MsisdnVerificationFragmentPresenterImpl(this);
    }

    private int getFragmentLayout() {
        return R.layout.fragment_msisdn_verification;
    }

    private void initView(View view) {
        ImageHandler.loadImageWithId(logo, R.drawable.ic_verifikasi);
        phoneNumberEditText.setText(cacheHandler.getString(PHONE_NUMBER_VERIFICATION, SessionHandler.getPhoneNumber()));
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        username.setText(getActivity().getString(R.string.hello) + ", " + SessionHandler.getLoginName(getActivity()));


    }

    private void setViewListener() {
        noThanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setReminder(true);

            }
        });
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestOTP();
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventOTPVerif();
                KeyboardHandler.DropKeyboard(getActivity(), verifyOtp);
                presenter.verifyOTP(otpEditText.getText().toString().trim());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDismiss();
            }
        });
    }

    public void onRequestOTP() {
        UnifyTracking.eventOTPSend();
        KeyboardHandler.DropKeyboard(getActivity(), sendOtp);
        cacheHandler.putString(PHONE_NUMBER_VERIFICATION, phoneNumberEditText.getText().toString());
        presenter.requestOTP(phoneNumberEditText.getText().toString().trim());
    }

    private void initialVar() {
        cacheHandler = new LocalCacheHandler(getActivity(), PHONE_VERIFICATION);
    }

    private void setActionVar() {

    }

    public void showError(String errorMessage) {
        if (errorMessage.equals("")) {
            CommonUtils.UniversalToast(getActivity(), getActivity().getString(R.string.msg_network_error));
        } else {
            CommonUtils.UniversalToast(getActivity(), errorMessage);
        }
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumberEditText.getText().toString();
    }

    @Override
    public String getVerificationCode() {
        return otpEditText.getText().toString();
    }

    @Override
    public void setError(EditText editText, String error) {
        editText.setError(error);
        editText.requestFocus();
    }

    @Override
    public void removeError() {
        otpEditText.setError(null);
        phoneNumberEditText.setError(null);
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void finishLoading() {
        if (progressDialog.isProgress())
            progressDialog.dismiss();
    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    @Override
    public EditText getOtpEditText() {
        return otpEditText;
    }

    @Override
    public EditText getPhoneEditText() {
        return phoneNumberEditText;
    }

    @Override
    public void onSuccessRequestOTP() {
        CommonUtils.UniversalToast(getActivity(), getActivity().getString(R.string.success_send_otp));
        finishLoading();
        setFinishRequestOTP();
        setSuccessRequestOTPCache();
        startTimer();
    }

    public void startTimer() {

        countDownTimer = new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                try {
                    sendOtp.setEnabled(false);

                    sendOtp.setText("" + String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                } catch (Exception e) {
                    cancel();
                }
            }

            public void onFinish() {
                try {
                    sendOtp.setText(R.string.title_resend_otp);
                    sendOtp.setEnabled(true);
                } catch (Exception e) {

                }
            }

        }.start();
        otpEditText.requestFocus();
    }

    private void setFinishRequestOTP() {
        phoneNumberEditText.setEnabled(false);
        verificationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessVerifyOTP() {
        finishLoading();
        phoneNumberView.setVisibility(View.GONE);
        verificationView.setVisibility(View.GONE);
        noThanksButton.setVisibility(View.GONE);
        instruction.setText(getActivity().getString(R.string.verify_success));
        closeButton.setVisibility(View.VISIBLE);
        if (listener != null)
            listener.onMSISDNVerified();
    }

    @Override
    public void setSuccessRequestOTPCache() {
        cacheHandler.putBoolean(HAS_REQUEST_OTP, true);
        cacheHandler.setExpire(3600);
        cacheHandler.applyEditor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroyView();
        destroyTimer();
        cacheHandler = null;
    }

    private void destroyTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        MsisdnVerificationFragmentPermissionsDispatcher.processOTPWithCheck(MsisdnVerificationFragment.this
                , otpCode);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void processOTP(String otpCode) {
        if (otpEditText != null)
            otpEditText.setText(otpCode);
        presenter.verifyOTP(otpCode.trim());

    }

    public void setListener(PhoneVerificationUtil.MSISDNListener listener) {
        this.listener = listener;
    }

    public PhoneVerificationUtil.MSISDNListener getListener() {
        return listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MsisdnVerificationFragmentPermissionsDispatcher.onRequestPermissionsResult(
                MsisdnVerificationFragment.this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_SMS)
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_SMS);
    }

    @OnPermissionDenied(Manifest.permission.READ_SMS)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_SMS);
    }

    @OnNeverAskAgain(Manifest.permission.READ_SMS)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_SMS);
    }

}
