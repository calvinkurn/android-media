package com.tokopedia.otp.cotp.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.otp.OtpModuleRouter;
import com.tokopedia.otp.R;
import com.tokopedia.otp.common.OTPAnalytics;
import com.tokopedia.otp.common.design.PinInputEditText;
import com.tokopedia.otp.common.di.DaggerOtpComponent;
import com.tokopedia.otp.common.di.OtpComponent;
import com.tokopedia.otp.common.util.IncomingSmsReceiver;
import com.tokopedia.otp.common.util.RequestPermissionUtil;
import com.tokopedia.otp.cotp.di.DaggerCotpComponent;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.presenter.VerificationPresenter;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author by nisie on 11/30/17.
 */

@RuntimePermissions
public class VerificationFragment extends BaseDaggerFragment implements Verification.View,
        IncomingSmsReceiver.ReceiveSMSListener {

    private static final String ARGS_DATA = "ARGS_DATA";
    private static final String ARGS_PASS_DATA = "pass_data";

    private static final int COUNTDOWN_LENGTH = 90;
    private static final int INTERVAL = 1000;
    private static final int MAX_OTP_LENGTH = 6;

    private static final String CACHE_OTP = "CACHE_OTP";
    private static final String HAS_TIMER = "has_timer";

    private static final CharSequence VERIFICATION_CODE = "Kode verifikasi";

    ImageView icon;
    TextView message;
    PinInputEditText inputOtp;
    TextView countdownText;
    TextView verifyButton;
    TextView errorOtp;
    View limitOtp;
    TextView tvLimitOtp;
    View finishCountdownView;
    TextView noCodeText;
    ImageView errorImage;

    CountDownTimer countDownTimer;
    ProgressBar progressDialog;

    private boolean isRunningTimer = false;
    protected LocalCacheHandler cacheHandler;
    private VerificationViewModel viewModel;

    @Inject
    VerificationPresenter presenter;

    @Inject
    IncomingSmsReceiver smsReceiver;

    @Inject
    OTPAnalytics analytics;

    public static Fragment createInstance(VerificationViewModel passModel) {
        Fragment fragment = new VerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PASS_DATA, passModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        OtpComponent otpComponent = DaggerOtpComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication())
                        .getBaseAppComponent()).build();

        DaggerCotpComponent.builder()
                .otpComponent(otpComponent)
                .build().inject(this);

        presenter.attachView(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null
                && getArguments().getParcelable(ARGS_PASS_DATA) != null) {
            viewModel = parseViewModel(getArguments());
        } else {
            getActivity().finish();
        }

        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_OTP);
        smsReceiver.setListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null
                && viewModel.getType().equals(RequestOtpUseCase.MODE_SMS)) {
            smsReceiver.registerSmsReceiver(getActivity());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showCheckSMSPermission();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (smsReceiver != null
                && viewModel != null
                && viewModel.getType().equals(RequestOtpUseCase.MODE_SMS)) {
            getActivity().unregisterReceiver(smsReceiver);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    private void showCheckSMSPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED
                && !getActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
            new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setMessage(
                            RequestPermissionUtil
                                    .getNeedPermissionMessage(Manifest.permission.READ_SMS)
                    )
                    .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VerificationFragmentPermissionsDispatcher
                                    .checkSmsPermissionWithCheck(VerificationFragment.this);

                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            RequestPermissionUtil.onPermissionDenied(getActivity(),
                                    Manifest.permission.READ_SMS);
                        }
                    })
                    .show();
        } else if (getActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
            VerificationFragmentPermissionsDispatcher
                    .checkSmsPermissionWithCheck(VerificationFragment.this);
        }
    }

    private VerificationViewModel parseViewModel(Bundle bundle) {
        viewModel = bundle.getParcelable(ARGS_PASS_DATA);
        return viewModel;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_DATA, viewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());
    }

    @Override
    protected String getScreenName() {
        if (viewModel != null && !TextUtils.isEmpty(viewModel.getAppScreen())) {
            return viewModel.getAppScreen();
        } else {
            return OTPAnalytics.Screen.SCREEN_COTP_DEFAULT;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cotp_verification, parent, false);
        icon = view.findViewById(R.id.icon);
        message = view.findViewById(R.id.message);
        inputOtp = view.findViewById(R.id.input_otp);
        countdownText = view.findViewById(R.id.countdown_text);
        verifyButton = view.findViewById(R.id.verify_button);
        limitOtp = view.findViewById(R.id.limit_otp);
        tvLimitOtp = view.findViewById(R.id.limit_otp_text);
        errorOtp = view.findViewById(R.id.error_otp);
        finishCountdownView = view.findViewById(R.id.finish_countdown);
        noCodeText = view.findViewById(R.id.no_code);
        errorImage = view.findViewById(R.id.error_image);
        progressDialog = view.findViewById(R.id.progress_bar);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        if (!isCountdownFinished()) {
            startTimer();
        } else {
            setLimitReachedCountdownText();
        }

        limitOtp.setVisibility(View.GONE);
        inputOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputOtp.getText().length() == MAX_OTP_LENGTH) {
                    enableVerifyButton();
                } else {
                    disableVerifyButton();
                }
            }
        });

        inputOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        && inputOtp.length() == MAX_OTP_LENGTH) {
                    presenter.verifyOtp(viewModel.getOtpType(), viewModel.getPhoneNumber(), viewModel
                            .getEmail(), inputOtp.getText().toString());
                    return true;
                }
                return false;
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.verifyOtp(viewModel.getOtpType(), viewModel.getPhoneNumber(), viewModel
                        .getEmail(), inputOtp.getText().toString());
            }
        });

        errorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOtp.setText("");
                removeErrorOtp();
            }
        });
    }

    private void disableVerifyButton() {
        verifyButton.setEnabled(false);
        verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getActivity(), R
                .drawable.grey_button_otp));
    }

    private void enableVerifyButton() {
        verifyButton.setEnabled(true);
        verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getActivity(), R
                .drawable.green_button_otp));
        removeErrorOtp();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        presenter.requestOTP(viewModel);
    }

    private void initData() {
        int imageId = viewModel.getIconResId();
        if (imageId != 0)
            ImageHandler.loadImageWithId(icon, imageId);
        else if (!TextUtils.isEmpty(viewModel.getImageUrl())) {
            ImageHandler.LoadImage(icon, viewModel.getImageUrl());
        } else {
            icon.setVisibility(View.GONE);
        }
        message.setText(MethodChecker.fromHtml(viewModel.getMessage()));
        verifyButton.setEnabled(false);
    }

    @Override
    public void onSuccessGetOTP() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string
                .verification_code_sent));
        startTimer();
    }

    @Override
    public void onSuccessVerifyOTP() {
        removeErrorOtp();
        resetCountDown();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();

    }

    @Override
    public void onGoToPhoneVerification() {
        if (getActivity().getApplicationContext() instanceof OtpModuleRouter) {
            getActivity().setResult(Activity.RESULT_OK);
            Intent intent = ((OtpModuleRouter) getActivity().getApplicationContext())
                    .getPhoneVerificationActivationIntent(getActivity());
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void resetCountDown() {
        cacheHandler.putBoolean(HAS_TIMER, false);
        cacheHandler.applyEditor();
    }

    @Override
    public void onErrorGetOTP(String errorMessage) {
        if (errorMessage.contains(getString(R.string.limit_otp_reached_many_times))) {
            tvLimitOtp.setText(errorMessage);
            limitOtp.setVisibility(View.VISIBLE);
            setLimitReachedCountdownText();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
            setFinishedCountdownText();
        }
    }

    @Override
    public void onErrorVerifyOtpCode(String errorMessage) {
        if (errorMessage.contains(VERIFICATION_CODE)) {
            inputOtp.setError(true);
            inputOtp.setFocusableInTouchMode(true);
            inputOtp.post(new Runnable() {
                public void run() {
                    inputOtp.requestFocusFromTouch();
                    InputMethodManager lManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(inputOtp, 0);
                }
            });
            errorImage.setVisibility(View.VISIBLE);
            errorOtp.setVisibility(View.VISIBLE);
        } else {
            onErrorVerifyLogin(errorMessage);
        }

    }

    @Override
    public void onErrorVerifyLogin(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onErrorVerifyOtpCode(int resId) {
        onErrorVerifyOtpCode(getString(resId));
    }

    @Override
    public void showLoadingProgress() {
        progressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingProgress() {
        progressDialog.setVisibility(View.GONE);
    }

    @Override
    public boolean isCountdownFinished() {
        return cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_TIMER, false);
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    private void startTimer() {
        if (isCountdownFinished()) {
            cacheHandler.putBoolean(HAS_TIMER, true);
            cacheHandler.setExpire(COUNTDOWN_LENGTH);
            cacheHandler.applyEditor();
        }

        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * INTERVAL, INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    isRunningTimer = true;
                    setRunningCountdownText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds
                            (millisUntilFinished)));
                }

                public void onFinish() {
                    isRunningTimer = false;
                    setFinishedCountdownText();
                }

            }.start();
        }
        inputOtp.requestFocus();
    }

    private void setFinishedCountdownText() {
        countdownText.setVisibility(View.GONE);
        finishCountdownView.setVisibility(View.VISIBLE);
        noCodeText.setVisibility(View.VISIBLE);

        TextView resend = finishCountdownView.findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOtp.setText("");
                removeErrorOtp();
                presenter.requestOTP(viewModel);
            }
        });

        TextView useOtherMethod = finishCountdownView.findViewById(R.id.use_other_method);
        TextView or = finishCountdownView.findViewById(R.id.or);

        if (viewModel != null
                && viewModel.canUseOtherMethod()) {
            or.setVisibility(View.VISIBLE);
            useOtherMethod.setVisibility(View.VISIBLE);

            useOtherMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dropKeyboard();
                    goToOtherVerificationMethod();
                }
            });
        } else {
            or.setVisibility(View.GONE);
            useOtherMethod.setVisibility(View.GONE);
        }
    }

    private void removeErrorOtp() {
        inputOtp.setError(false);
        errorOtp.setVisibility(View.INVISIBLE);
        errorImage.setVisibility(View.INVISIBLE);
    }

    private void setLimitReachedCountdownText() {

        finishCountdownView.setVisibility(View.GONE);
        noCodeText.setVisibility(View.GONE);

        if (viewModel != null
                && viewModel.canUseOtherMethod()) {
            countdownText.setVisibility(View.VISIBLE);
            countdownText.setEnabled(true);
            countdownText.setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
            countdownText.setText(R.string.login_with_other_method);
            countdownText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToOtherVerificationMethod();
                }
            });
        } else {
            countdownText.setVisibility(View.GONE);
        }

    }

    private void setRunningCountdownText(String countdown) {
        countdownText.setVisibility(View.VISIBLE);
        countdownText.setOnClickListener(null);
        finishCountdownView.setVisibility(View.GONE);
        noCodeText.setVisibility(View.GONE);

        countdownText.setTextColor(MethodChecker.getColor(getActivity(), R.color.font_black_disabled_38));
        countdownText.setEnabled(false);
        String text = String.format("%s <b> %s %s</b> %s",
                getString(R.string.please_wait_in),
                countdown,
                getString(R.string.second),
                getString(R.string.to_resend_otp));

        countdownText.setText(MethodChecker.fromHtml(text));

    }

    private void goToOtherVerificationMethod() {
        if (getActivity() instanceof VerificationActivity) {
            ((VerificationActivity) getActivity()).goToSelectVerificationMethod();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        presenter.detachView();
    }

    public void setData(Bundle bundle) {
        viewModel = parseViewModel(bundle);
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        processOTPSMS(otpCode);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void processOTPSMS(String otpCode) {
        if (inputOtp != null)
            inputOtp.setText(otpCode);
        presenter.verifyOtp(viewModel.getOtpType(), viewModel.getPhoneNumber(), viewModel.getEmail(),
                otpCode);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void checkSmsPermission() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        VerificationFragmentPermissionsDispatcher.onRequestPermissionsResult(
                VerificationFragment.this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_SMS)
    void showRationaleForReadSms(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_SMS);
    }

    @OnPermissionDenied(Manifest.permission.READ_SMS)
    void showDeniedForReadSms() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_SMS);
    }

    @OnNeverAskAgain(Manifest.permission.READ_SMS)
    void showNeverAskForReadSms() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_SMS);
    }
}
