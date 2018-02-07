package com.tokopedia.digital.tokocash.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.tokocash.TokoCashService;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.domain.TokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.ActivateTokoCashInteractor;
import com.tokopedia.digital.tokocash.listener.RequestOTPWalletView;
import com.tokopedia.digital.tokocash.presenter.IRequestOTPWalletPresenter;
import com.tokopedia.digital.tokocash.presenter.RequestOTPWalletPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

@RuntimePermissions
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
    private IncomingSmsReceiver incomingSmsReceiver;
    private CompositeSubscription compositeSubscription;

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
        if (!cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
            startTimer();
        }
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

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
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        String acessToken = sessionHandler.getAccessToken(MainApplication.getAppContext());
        TokoCashService tokoCashService = new TokoCashService(acessToken);
        compositeSubscription = new CompositeSubscription();
        ActivateTokoCashInteractor interactor = new ActivateTokoCashInteractor
                (compositeSubscription, new TokoCashRepository(tokoCashService),
                        new JobExecutor(),
                        new UIThread());
        presenter = new RequestOTPWalletPresenter(interactor, this);
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
        incomingSmsReceiver = new IncomingSmsReceiver();
        incomingSmsReceiver.setListener(getReceiverSMSListener());
        incomingSmsReceiver.registerSmsReceiver(getActivity());
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
        finishProgressDialog();
        startTimer();
    }

    @Override
    public void onSuccessLinkWalletToTokoCash() {
        finishProgressDialog();
        SessionHandler.setIsMSISDNVerified(true);
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

        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();

        cacheHandler = null;
    }

    public interface ActionListener {
        void setTitlePage(String titlePage);

        void directToSuccessActivateTokoCashPage();
    }
}