package com.tokopedia.digital.tokocash.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.domain.ActivateTokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.ActivateTokoCashInteractor;
import com.tokopedia.digital.tokocash.listener.RequestOTPWalletView;
import com.tokopedia.digital.tokocash.presenter.IRequestOTPWalletPresenter;
import com.tokopedia.digital.tokocash.presenter.RequestOTPWalletPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class RequestOTPWalletFragment extends BasePresenterFragment<IRequestOTPWalletPresenter>
        implements RequestOTPWalletView {

    private static final String FORMAT = "%02d";
    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
    private static final String HAS_PHONE_VERIF_TIMER = "HAS_PHONE_VERIF_TIMER";
    private static final int DEFAULT_COUNTDOWN_TIMER_SECOND = 90;
    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;

    @BindView(R2.id.wallet_phone_number)
    TextView walletPhoneNumber;
    @BindView(R2.id.send_sms_verification)
    Button sendSmsVerification;
    @BindView(R2.id.repeat_send_sms)
    Button repeatSendSms;
    @BindView(R2.id.input_otp_view)
    LinearLayout inputOtpView;
    @BindView(R2.id.verification_btn)
    Button verificationButton;
    @BindView(R2.id.input_otp)
    EditText inputOtp;

    private ActionListener listener;
    private LocalCacheHandler cacheHandler;
    private CountDownTimer countDownTimer;
    private TkpdProgressDialog progressDialog;

    public static RequestOTPWalletFragment newInstance() {
        RequestOTPWalletFragment fragment = new RequestOTPWalletFragment();
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

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
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        presenter = new RequestOTPWalletPresenter(
                new ActivateTokoCashInteractor(new ActivateTokoCashRepository
                        (new TokoCashService(sessionHandler.getAccessToken(MainApplication.getAppContext())))), this);
    }

    @Override
    protected void initialListener(Activity activity) {
        listener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_verifikasi_phone_tokocash;
    }

    @Override
    protected void initView(View view) {
        listener.setTitlePage(getResources().getString(R.string.tokocash_toolbar_verification));
        walletPhoneNumber.setText(SessionHandler.getPhoneNumber());
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_PHONE_VERIF_TIMER);
    }

    @Override
    protected void setActionVar() {
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
                            MethodChecker.getDrawable(getActivity(), R.drawable.green_button_rounded_unify));
                    verificationButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                } else {
                    verificationButton.setEnabled(false);
                    MethodChecker.setBackground(verificationButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.digital_grey_button_verifikasi_rounded));
                    verificationButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_verifikasi_tokocash));
                }
            }
        });
    }

    @Override
    public void onSuccessRequestOtpWallet() {
        startTimer();
    }

    @Override
    public void onSuccessLinkWalletToTokoCash() {
        listener.directToSuccessActivateTokoCashPage();
    }

    @Override
    public void onErrorOTPWallet(String message) {
        finishProgressDialog();
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.showDialog();
        } else {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        }
    }

    private void finishProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void startTimer() {
        if (cacheHandler.isExpired() || cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
            cacheHandler.putBoolean(HAS_PHONE_VERIF_TIMER, true);
            cacheHandler.setExpire(DEFAULT_COUNTDOWN_TIMER_SECOND);
            cacheHandler.applyEditor();
        }

        countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * 1000,
                COUNTDOWN_INTERVAL_SECOND) {
            @Override
            public void onTick(long millisForFinished) {
                inputOtpView.setVisibility(View.VISIBLE);
                sendSmsVerification.setVisibility(View.GONE);
                repeatSendSms.setVisibility(View.VISIBLE);
                repeatSendSms.setText(String.format(getString(R.string.repeat_sms_verification),
                        String.format(FORMAT, TimeUnit.MILLISECONDS.toSeconds(millisForFinished))));
            }

            @Override
            public void onFinish() {
                sendSmsVerification.setVisibility(View.VISIBLE);
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
        presenter.onDestroyView();
        cacheHandler = null;
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void directToSuccessActivateTokoCashPage();
    }
}