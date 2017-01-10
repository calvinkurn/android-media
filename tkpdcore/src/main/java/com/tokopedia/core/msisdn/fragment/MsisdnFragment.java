package com.tokopedia.core.msisdn.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.msisdn.MSISDNConstant;
import com.tokopedia.core.msisdn.listener.MsisdnVerificationFragmentView;
import com.tokopedia.core.msisdn.presenter.MsisdnVerificationFragmentPresenter;
import com.tokopedia.core.msisdn.presenter.MsisdnVerificationFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PhoneVerificationUtil;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by nisie on 11/7/16.
 */

@RuntimePermissions
public class MsisdnFragment extends BasePresenterFragment<MsisdnVerificationFragmentPresenter>
        implements MsisdnVerificationFragmentView, IncomingSmsReceiver.ReceiveSMSListener, MSISDNConstant {

    private static final String FORMAT = "%02d:%02d";
    private static final long COUNTDOWN_INTERVAL = 1000;
    private static final long COUNTDOWN_TIME = 90000;

    @BindView(R2.id.input_otp)
    EditText vInputOtp;
    @BindView(R2.id.title_verify_phone)
    TextView titleVerifyPhone;
    @BindView(R2.id.phone_number)
    EditText phoneNumber;
    @BindView(R2.id.title_security)
    TextView titleSecurity;
    @BindView(R2.id.view_otp)
    View vOtp;
    @BindView(R2.id.view_error)
    View vError;
    @BindView(R2.id.send_otp)
    TextView vSendOtp;
    @BindView(R2.id.save_but)
    TextView vSaveBut;
    @BindView(R2.id.error_title)
    TextView vErrorTitle;
    @BindView(R2.id.error_msg)
    TextView vErrorMessage;
    @BindView(R2.id.progress)
    ProgressBar vProgress;
    @BindView(R2.id.title_send_otp)
    TextView titleSendOtp;
    @BindView(R2.id.title_otp)
    TextView titleOtp;

    TkpdProgressDialog progressDialog;
    private PhoneVerificationUtil.MSISDNListener listener;
    CountDownTimer countDownTimer;
    PhoneVerificationUtil phoneVerificationUtil;
    IncomingSmsReceiver smsReceiver;

    public static MsisdnFragment createInstance() {
        MsisdnFragment fragment = new MsisdnFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public MsisdnFragment() {
        this.smsReceiver = new IncomingSmsReceiver();
        this.smsReceiver.setListener(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        progressDialog.showDialog();
        phoneVerificationUtil = new PhoneVerificationUtil(getActivity());
        phoneVerificationUtil.setMSISDNListener(new PhoneVerificationUtil.MSISDNListener() {
            @Override
            public void onMSISDNVerified() {
                finishLoading();
                Intent intent = SellerRouter.getAcitivityShopCreateEdit(context);
                intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                        SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onMSISDNNotVerified() {
                finishLoading();
                phoneNumber.setEnabled(true);
                phoneNumber.setText(SessionHandler.getPhoneNumber());
            }

            @Override
            public void onNoConnection() {
                finishLoading();
                showEmptyState("");
            }

            @Override
            public void onTimeout() {
                finishLoading();
                showEmptyState(getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onFailAuth() {
                finishLoading();
                showEmptyState(getString(R.string.default_request_error_forbidden_auth));
            }

            @Override
            public void onNullData() {
                finishLoading();
                showEmptyState(getString(R.string.default_request_error_null_data));
            }

            @Override
            public void onThrowable(Throwable e) {
                finishLoading();
                showEmptyState(e.toString());
            }

            @Override
            public void onError(String error) {
                finishLoading();
                showEmptyState(error);
            }
        });
        phoneVerificationUtil.checkIsMSISDNVerified();
    }

    private void showEmptyState(String error) {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        if (error.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    progressDialog.showDialog();
                    phoneVerificationUtil.checkIsMSISDNVerified();
                }
            });
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    progressDialog.showDialog();
                    phoneVerificationUtil.checkIsMSISDNVerified();
                }
            });
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new MsisdnVerificationFragmentPresenterImpl(this);

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_msisdn;
    }

    @Override
    protected void initView(View view) {
        KeyboardHandler.DropKeyboard(getActivity(), getView());

        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        titleVerifyPhone.setText(getActivity().getString(R.string.hello) + ", " + SessionHandler.getLoginName(getActivity()));
        phoneNumber.addTextChangedListener(watcher(phoneNumber));
    }

    private TextWatcher watcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = CustomPhoneNumberUtil.transform(s.toString());
                if (s.toString().length() != phone.length()) {
                    editText.removeTextChangedListener(this);
                    editText.setText(phone);
                    editText.setSelection(phone.length());
                    editText.addTextChangedListener(this);
                }
            }
        };
    }

    @Override
    protected void setViewListener() {

        vSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.requestOTP(getPhoneNumber().trim());
            }
        });

        vSaveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.verifyOTP(getVerificationCode().trim());
            }
        });

    }

    public void destroyTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void initCountDownTimer() {
        titleSendOtp.setVisibility(View.VISIBLE);
        MethodChecker.setBackground(vSendOtp, getResources().getDrawable(R.drawable.btn_transparent_disable));
        vSendOtp.setEnabled(false);
        vSendOtp.setTextColor(getResources().getColor(R.color.grey_600));
        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, COUNTDOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                try {
                    MethodChecker.setBackground(vSendOtp, getResources().getDrawable(R.drawable.btn_transparent_disable));
                    vSendOtp.setEnabled(false);
                    vSendOtp.setText(String.valueOf(String.format(Locale.getDefault(), FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                } catch (Exception e) {
                    cancel();
                }
            }

            public void onFinish() {
                titleSendOtp.setVisibility(View.GONE);
                vSendOtp.setTextColor(getResources().getColor(R.color.tkpd_green_onboarding));
                MethodChecker.setBackground(vSendOtp, getResources().getDrawable(R.drawable.btn_share_transaparent));
                vSendOtp.setText("Kirim ulang OTP");
                vSendOtp.setEnabled(true);
            }

        }.start();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

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
        return phoneNumber.getText().toString().replace("-", "");
    }

    @Override
    public String getVerificationCode() {
        return vInputOtp.getText().toString();
    }

    @Override
    public void setError(EditText editText, String error) {
        editText.setError(error);
        editText.requestFocus();
    }

    @Override
    public void removeError() {
        vInputOtp.setError(null);
        phoneNumber.setError(null);
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
        getActivity().finish();
    }

    @Override
    public EditText getOtpEditText() {
        return vInputOtp;
    }

    @Override
    public EditText getPhoneEditText() {
        return phoneNumber;
    }

    @Override
    public void onSuccessRequestOTP() {
        finishLoading();
        initCountDownTimer();
        phoneNumber.setEnabled(false);
        SnackbarManager.make(getActivity(), getActivity().getString(R.string.success_send_otp), Snackbar.LENGTH_LONG).show();
        vInputOtp.setVisibility(View.VISIBLE);
        titleOtp.setVisibility(View.VISIBLE);
        vSaveBut.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessVerifyOTP() {
        finishLoading();
        Intent intent = SellerRouter.getAcitivityShopCreateEdit(context);
        intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
        startActivity(intent);
        getActivity().finish();

    }

    @Override
    public void setSuccessRequestOTPCache() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        destroyTimer();
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        MsisdnFragmentPermissionsDispatcher.processOTPWithCheck(MsisdnFragment.this, otpCode);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void processOTP(String otpCode) {
        if (vInputOtp != null)
            vInputOtp.setText(otpCode);
    }

    public void setListener(PhoneVerificationUtil.MSISDNListener listener) {
        this.listener = listener;
    }

    public PhoneVerificationUtil.MSISDNListener getListener() {
        return listener;
    }

    @Override
    public void onPause() {
        if (smsReceiver != null)
            getActivity().unregisterReceiver(smsReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        smsReceiver.registerSmsReceiver(getActivity());
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MsisdnFragmentPermissionsDispatcher.onRequestPermissionsResult(MsisdnFragment.this, requestCode, grantResults);
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