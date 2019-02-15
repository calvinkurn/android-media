package com.tokopedia.otp.cotp.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
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
import com.tokopedia.otp.common.util.SmsBroadcastReceiver;
import com.tokopedia.otp.cotp.di.DaggerCotpComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.presenter.VerificationPresenter;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationFragment extends BaseDaggerFragment implements Verification.View {

    protected static final String ARGS_DATA = "ARGS_DATA";
    protected static final String ARGS_PASS_DATA = "pass_data";

    private static final int COUNTDOWN_LENGTH = 90;
    private static final int INTERVAL = 1000;
    protected static final int MAX_OTP_LENGTH = 6;

    private static final String CACHE_OTP = "CACHE_OTP";
    private static final String HAS_TIMER = "has_timer";

    private static final CharSequence VERIFICATION_CODE = "Kode verifikasi";

    protected ImageView icon;
    protected TextView message;
    protected PinInputEditText inputOtp;
    protected TextView countdownText;
    protected TextView verifyButton;
    protected TextView errorOtp;
    protected View limitOtp;
    protected TextView tvLimitOtp;
    protected View finishCountdownView;
    protected TextView noCodeText;
    protected ImageView errorImage;

    CountDownTimer countDownTimer;
    ProgressBar progressDialog;

    private boolean isRunningTimer = false;
    protected LocalCacheHandler cacheHandler;
    protected VerificationViewModel viewModel;
    private SmsRetrieverClient smsRetrieverClient;

    @Inject
    VerificationPresenter presenter;

    @Inject
    OTPAnalytics analytics;

    @Inject
    SmsBroadcastReceiver smsBroadcastReceiver;

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
        smsRetrieverClient = SmsRetriever.getClient(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!= null) {
            smsBroadcastReceiver.register(getActivity(), getOTPReceiverListener());
        }
    }

    private SmsBroadcastReceiver.ReceiveSMSListener getOTPReceiverListener() {
        return new SmsBroadcastReceiver.ReceiveSMSListener() {
            @Override
            public void onReceiveOTP(@NotNull String otpCode) {
                        processOTPSMS(otpCode);
            }
        };
    }


    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && smsBroadcastReceiver != null) {
            getActivity().unregisterReceiver(smsBroadcastReceiver);
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

    protected void prepareView() {
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
                    verifyOtp();
                } else {
                    disableVerifyButton();
                }
            }
        });

        inputOtp.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE
                    && inputOtp.length() == MAX_OTP_LENGTH) {
                verifyOtp();
                return true;
            }
            return false;
        });

        verifyButton.setOnClickListener(v -> {
            if (analytics != null && viewModel != null) {
                analytics.eventClickVerifyButton(viewModel.getOtpType());
            }
            verifyOtp();
        });

        errorImage.setOnClickListener(v -> {
            inputOtp.setText("");
            removeErrorOtp();
        });
    }

    protected void verifyOtp() {
        presenter.verifyOtp(viewModel.getOtpType(), viewModel.getPhoneNumber(), viewModel
                .getEmail(), inputOtp.getText().toString());
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
        updateViewFromServer();
        smsRetrieverClient.startSmsRetriever();
        requestOtp();
    }

    private void updateViewFromServer() {
        presenter.updateViewFromServer(viewModel);
    }

    protected void requestOtp() {
        presenter.requestOTP(viewModel);
    }

    private void initData() {
        setData();
        verifyButton.setEnabled(false);
    }

    private void setData() {
        int imageId = viewModel.getIconResId();
        if (!TextUtils.isEmpty(viewModel.getImageUrl())) {
            ImageHandler.LoadImage(icon, viewModel.getImageUrl());
        } else if (imageId != 0)
            ImageHandler.loadImageWithId(icon, imageId);
        else {
            icon.setVisibility(View.GONE);
        }
        message.setText(MethodChecker.fromHtml(viewModel.getMessage()));
    }

    @Override
    public void onSuccessGetOTP(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
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

    protected void resetCountDown() {
        cacheHandler.putBoolean(HAS_TIMER, false);
        cacheHandler.applyEditor();
    }

    @Override
    public void onErrorGetOTP(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        setFinishedCountdownText();
    }

    @Override
    public void onLimitOTPReached(String errorMessage) {
        tvLimitOtp.setText(errorMessage);
        limitOtp.setVisibility(View.VISIBLE);
        setLimitReachedCountdownText();
    }

    @Override
    public void logUnknownError(Throwable throwable) {
        try {
            Crashlytics.logException(throwable);
        } catch (IllegalStateException e) {
            e.printStackTrace();
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
    public void trackOnBackPressed() {
        if (analytics != null && viewModel != null) {
            analytics.eventClickBackOTPPage(viewModel.getOtpType());
        }

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

    protected void setFinishedCountdownText() {
        countdownText.setVisibility(View.GONE);
        finishCountdownView.setVisibility(View.VISIBLE);
        noCodeText.setVisibility(View.VISIBLE);

        TextView resend = finishCountdownView.findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (analytics != null && viewModel != null) {
                    analytics.eventClickResendOtp(viewModel.getOtpType());
                }
                inputOtp.setText("");
                removeErrorOtp();
                requestOtp();
            }
        });

        TextView useOtherMethod = finishCountdownView.findViewById(R.id.use_other_method);
        TextView or = finishCountdownView.findViewById(R.id.or);

        if (viewModel != null
                && viewModel.canUseOtherMethod()) {
            or.setVisibility(View.VISIBLE);
            useOtherMethod.setVisibility(View.VISIBLE);

            useOtherMethod.setOnClickListener(v -> {
                if (analytics != null && viewModel != null) {
                    analytics.eventClickUseOtherMethod(viewModel.getOtpType());
                }
                dropKeyboard();
                goToOtherVerificationMethod();
            });
        } else {
            or.setVisibility(View.GONE);
            useOtherMethod.setVisibility(View.GONE);
        }
    }

    protected void removeErrorOtp() {
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

    protected void goToOtherVerificationMethod() {
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
    public void onSuccessGetModelFromServer(MethodItem methodItem) {
        this.viewModel.setImageUrl(methodItem.getImageUrl());
        this.viewModel.setMessage(methodItem.getVerificationText());
        setData();
    }

    public void processOTPSMS(String otpCode) {
        if (inputOtp != null)
            inputOtp.setText(otpCode);
        verifyOtp();
    }

}
