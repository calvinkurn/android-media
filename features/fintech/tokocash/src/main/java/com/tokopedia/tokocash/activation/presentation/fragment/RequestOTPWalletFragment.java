package com.tokopedia.tokocash.activation.presentation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.activation.presentation.contract.RequestOtpTokoCashContract;
import com.tokopedia.tokocash.activation.presentation.presenter.RequestOTPWalletPresenter;
import com.tokopedia.tokocash.common.di.TokoCashComponent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class RequestOTPWalletFragment extends BaseDaggerFragment implements RequestOtpTokoCashContract.View {

    private static final String FORMAT = "%02d";
    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
    private static final String HAS_PHONE_VERIF_TIMER = "HAS_PHONE_VERIF_TIMER";
    private static final int DEFAULT_COUNTDOWN_TIMER_SECOND = 90;
    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;

    private TextView walletPhoneNumber;
    private Button sendSmsVerification;
    private Button repeatSendSms;
    private LinearLayout inputOtpView;
    private Button verificationButton;
    private EditText inputOtp;

    private ActionListener listener;
    private LocalCacheHandler cacheHandler;
    private CountDownTimer countDownTimer;
    private ProgressBar progressBar;

    @Inject
    RequestOTPWalletPresenter presenter;
    @Inject
    CacheManager cacheManager;

    public static RequestOTPWalletFragment newInstance() {
        RequestOTPWalletFragment fragment = new RequestOTPWalletFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_otp, container, false);
        walletPhoneNumber = view.findViewById(R.id.wallet_phone_number);
        sendSmsVerification = view.findViewById(R.id.send_sms_verification);
        repeatSendSms = view.findViewById(R.id.repeat_send_sms);
        inputOtpView = view.findViewById(R.id.input_otp_view);
        verificationButton = view.findViewById(R.id.verification_btn);
        inputOtp = view.findViewById(R.id.input_otp);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (cacheHandler != null && !cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
            startTimer();
        }

        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_PHONE_VERIF_TIMER);
        listener.setTitlePage(getResources().getString(R.string.tokocash_toolbar_verification));
        walletPhoneNumber.setText(presenter.getUserPhoneNumber());
        setActionVar();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ActionListener) activity;
    }

    private void setActionVar() {
        sendSmsVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.requestOTPWallet();
            }
        });

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.linkWalletToTokoCash(inputOtp.getText().toString());
            }
        });

        inputOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 6) {
                    verificationButton.setEnabled(true);
                    MethodChecker.setBackground(verificationButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_green));
                    verificationButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                } else {
                    verificationButton.setEnabled(false);
                    MethodChecker.setBackground(verificationButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.activate_grey_button_rounded));
                    verificationButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_grey_activation));
                }
            }
        });
    }

    @Override
    public void onSuccessRequestOtpWallet() {
        finishProgressDialog();
        startTimer();
    }

    @Override
    public void onSuccessLinkWalletToTokoCash() {
        finishProgressDialog();
        presenter.setMsisdnUserVerified(true);
        listener.directToSuccessActivateTokoCashPage();
    }

    @Override
    public void onErrorOTPWallet(Throwable e) {
        finishProgressDialog();
        NetworkErrorHelper.showSnackbar(getActivity(), e.getMessage());
    }

    @Override
    public void onErrorNetwork(Throwable e) {
        finishProgressDialog();
        String message = ErrorHandler.getErrorMessage(getActivity(), e);
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void finishProgressDialog() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    private void startTimer() {
        if (cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
            cacheHandler.putBoolean(HAS_PHONE_VERIF_TIMER, true);
            cacheHandler.setExpire(DEFAULT_COUNTDOWN_TIMER_SECOND);
            cacheHandler.applyEditor();
        }

        countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * 1000,
                COUNTDOWN_INTERVAL_SECOND) {
            @Override
            public void onTick(long millisForFinished) {
                if (inputOtpView != null)
                    inputOtpView.setVisibility(View.VISIBLE);
                if (sendSmsVerification != null)
                    sendSmsVerification.setVisibility(View.GONE);
                if (repeatSendSms != null) {
                    repeatSendSms.setVisibility(View.VISIBLE);
                    repeatSendSms.setText(String.format(getString(R.string.repeat_sms_verification),
                            String.format(FORMAT, TimeUnit.MILLISECONDS.toSeconds(millisForFinished))));
                }
            }

            @Override
            public void onFinish() {
                if (sendSmsVerification != null)
                    sendSmsVerification.setVisibility(View.VISIBLE);
                if (repeatSendSms != null)
                    repeatSendSms.setVisibility(View.GONE);
            }
        }.start();
        inputOtp.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        cacheHandler = null;
        presenter.onDestroyView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        TokoCashComponent tokoCashComponent =
                TokoCashComponentInstance.getComponent(getActivity().getApplication());
        tokoCashComponent.inject(this);
        presenter.attachView(this);
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void directToSuccessActivateTokoCashPage();
    }
}