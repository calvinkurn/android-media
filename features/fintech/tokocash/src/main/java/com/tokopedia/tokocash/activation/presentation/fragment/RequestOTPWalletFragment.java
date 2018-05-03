package com.tokopedia.tokocash.activation.presentation.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.activation.presentation.contract.RequestOtpTokoCashContract;
import com.tokopedia.tokocash.activation.presentation.presenter.RequestOTPWalletPresenter;
import com.tokopedia.tokocash.di.TokoCashComponent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

@RuntimePermissions
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
    private TkpdProgressDialog progressDialog;
    private IncomingSmsReceiver incomingSmsReceiver;

    @Inject
    RequestOTPWalletPresenter presenter;

    public static RequestOTPWalletFragment newInstance() {
        RequestOTPWalletFragment fragment = new RequestOTPWalletFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_request_otp, container, false);
        walletPhoneNumber = view.findViewById(R.id.wallet_phone_number);
        sendSmsVerification = view.findViewById(R.id.send_sms_verification);
        repeatSendSms = view.findViewById(R.id.repeat_send_sms);
        inputOtpView = view.findViewById(R.id.input_otp_view);
        verificationButton = view.findViewById(R.id.verification_btn);
        inputOtp = view.findViewById(R.id.input_otp);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (cacheHandler != null && !cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
            startTimer();
        }

        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_PHONE_VERIF_TIMER);
        incomingSmsReceiver = new IncomingSmsReceiver();
        incomingSmsReceiver.setListener(getReceiverSMSListener());
        incomingSmsReceiver.registerSmsReceiver(getActivity());

        listener.setTitlePage(getResources().getString(R.string.tokocash_toolbar_verification));
        walletPhoneNumber.setText(presenter.getUserPhoneNumber());
        setActionVar();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showCheckSMSPermission();
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
                    .setPositiveButton(com.tokopedia.core.R.string.title_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestOTPWalletFragmentPermissionsDispatcher
                                    .checkSmsPermissionWithCheck(RequestOTPWalletFragment.this);

                        }
                    })
                    .setNegativeButton(com.tokopedia.core.R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            RequestPermissionUtil.onPermissionDenied(getActivity(),
                                    Manifest.permission.READ_SMS);
                        }
                    })
                    .show();
        } else if (getActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
            RequestOTPWalletFragmentPermissionsDispatcher
                    .checkSmsPermissionWithCheck(RequestOTPWalletFragment.this);
        }
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
                            MethodChecker.getDrawable(getActivity(), R.drawable.green_button_rounded_unify));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestOTPWalletFragmentPermissionsDispatcher.onRequestPermissionsResult(
                RequestOTPWalletFragment.this, requestCode, grantResults);
    }

    private IncomingSmsReceiver.ReceiveSMSListener getReceiverSMSListener() {
        return new IncomingSmsReceiver.ReceiveSMSListener() {
            @Override
            public void onReceiveOTP(String otpCode) {
                validateCodeOTP(otpCode);
            }
        };
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void validateCodeOTP(String otpCode) {
        inputOtp.setText(otpCode);
        verificationButton.performClick();
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

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void checkSmsPermission() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (incomingSmsReceiver != null)
            getActivity().unregisterReceiver(incomingSmsReceiver);

        cacheHandler = null;
        presenter.onDestroyView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void directToSuccessActivateTokoCashPage();
    }
}