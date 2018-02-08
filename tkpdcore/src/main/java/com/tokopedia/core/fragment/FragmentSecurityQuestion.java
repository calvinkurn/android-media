package com.tokopedia.core.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.OTPModel;
import com.tokopedia.core.session.model.QuestionFormModel;
import com.tokopedia.core.session.model.SecurityQuestionViewModel;
import com.tokopedia.core.session.presenter.SecurityQuestionPresenter;
import com.tokopedia.core.session.presenter.SecurityQuestionPresenterImpl;
import com.tokopedia.core.session.presenter.SecurityQuestionView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

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
 * modify by m.normansyah 9-11-2015,
 * complete MVP
 */
@RuntimePermissions
public class FragmentSecurityQuestion extends Fragment implements SecurityQuestionView, IncomingSmsReceiver.ReceiveSMSListener {

    private static final String CAN_REQUEST_OTP_IMMEDIATELY = "auto_request_otp";
    private TkpdProgressDialog Progress;
    SecurityQuestionPresenter presenter;
    private static final String FORMAT = "%02d:%02d";

    @BindView(R2.id.input_text)
    EditText vAnswer;
    @BindView(R2.id.input_otp)
    EditText vInputOtp;
    @BindView(R2.id.title_otp)
    TextView titleOTP;
    @BindView(R2.id.title_security)
    TextView titleSecurity;
    @BindView(R2.id.wrapper_input_text)
    TextInputLayout wrapperAnswer;
    @BindView(R2.id.title)
    TextView vQuestion;
    @BindView(R2.id.view_security)
    View vSecurity;
    @BindView(R2.id.view_otp)
    View vOtp;
    @BindView(R2.id.view_error)
    View vError;
    @BindView(R2.id.send_otp)
    TextView vSendOtp;
    @BindView(R2.id.send_otp_call)
    TextView vSendOtpCall;
    @BindView(R2.id.save_but)
    TextView vSaveBut;
    @BindView(R2.id.error_title)
    TextView vErrorTitle;
    @BindView(R2.id.error_msg)
    TextView vErrorMessage;
    @BindView(R2.id.progress)
    ProgressBar vProgress;
    @BindView(R2.id.title_change_number)
    TextView changeNumber;
    @BindView(R2.id.verify_button)
    TextView verifyTrueCaller;

    CountDownTimer countDownTimer;
    private Unbinder unbinder;
    IncomingSmsReceiver smsReceiver;
    boolean isRunningTimer;

    public interface SecurityQuestionListener {
        void onSuccess();

        void onFailed();
    }

    public void onFailedProfileShared(String error) {
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
    }

    public void onSuccessProfileShared(String phoneNumber) {
        SnackbarManager.make(getActivity(), getString(R.string.success_fetch_truecaller), Snackbar.LENGTH_LONG).show();
        presenter.verifyTruecaller(getActivity(), phoneNumber);
    }

    private SecurityQuestionListener Listener;


    public static FragmentSecurityQuestion newInstance(int Security1, int Security2, String UserID, String email, SecurityQuestionListener listener) {
        FragmentSecurityQuestion fragment = new FragmentSecurityQuestion();
        Bundle args = new Bundle();
        args.putInt(SECURITY_1_KEY, Security1);
        args.putInt(SECURITY_2_KEY, Security2);
        args.putString(USER_ID_KEY, UserID);
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        fragment.Listener = listener;
        return fragment;
    }

    public FragmentSecurityQuestion() {
        this.smsReceiver = new IncomingSmsReceiver();
        this.smsReceiver.setListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SecurityQuestionPresenterImpl(this);
        presenter.getDataAfterRotate(savedInstanceState);
        presenter.initInstances(getActivity());
        presenter.getDataFromArgument(getArguments());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save last input by user
        if (presenter.isSecurityQuestion() && presenter.isValidForm(vAnswer.getText().toString())) {
            presenter.saveAnswer(vAnswer.getText().toString());// this is for rotation
        }
        if (presenter.isOtp() && presenter.isValidForm(vInputOtp.getText().toString())) {
            presenter.saveOTPAnswer(vInputOtp.getText().toString());// this is for rotation
        }
        // save remaining data from user
        presenter.saveDataBeforeRotate(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_security_question, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        wrapperAnswer.setHintEnabled(false);
        Progress = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        initListener();
        showViewOtp();
        if (!presenter.isAfterRotate()) // if not rotate get question
            presenter.getQuestionForm();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter.isAfterRotate())
            presenter.initDataAfterRotate();
        smsReceiver.registerSmsReceiver(getActivity());
        presenter.doSendAnalytics();

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
                    .setMessage(RequestPermissionUtil.getNeedPermissionMessage(Manifest.permission.READ_SMS)
                    )
                    .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentSecurityQuestionPermissionsDispatcher.checkSmsPermissionWithCheck(FragmentSecurityQuestion.this);

                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_SMS);
                        }
                    })
                    .show();
        } else if (getActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
            FragmentSecurityQuestionPermissionsDispatcher.checkSmsPermissionWithCheck(FragmentSecurityQuestion.this);
        }
    }

    @Override
    public void onPause() {
        if (smsReceiver != null)
            getActivity().unregisterReceiver(smsReceiver);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
    }

    @Override
    public void setAnswerOTP(String text) {
        vInputOtp.setText(text);
    }

    @Override
    public void setAnswerSecurity(String text) {
        vAnswer.setText(text);
        vAnswer.setText("081910355420");
    }

    @Override
    public void startTimer() {
        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(90000, 1000) {
                public void onTick(long millisUntilFinished) {
                    try {
                        isRunningTimer = true;
                        MethodChecker.setBackground(vSendOtp, getResources().getDrawable(R.drawable.btn_transparent_disable));
                        vSendOtp.setEnabled(false);
                        vSendOtp.setText(String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                        vSendOtpCall.setVisibility(View.GONE);

                    } catch (Exception e) {
                        cancel();
                    }
                }

                public void onFinish() {
                    try {
                        isRunningTimer = false;
                        enableOtpButton();
                    } catch (Exception e) {

                    }
                }

            }.start();
        }
        vInputOtp.requestFocus();
    }

    private void enableOtpButton() {
        vSendOtp.setTextColor(getResources().getColor(R.color.tkpd_green_onboarding));
        MethodChecker.setBackground(vSendOtp, getResources().getDrawable(R.drawable.btn_share_transaparent));
        vSendOtp.setText(R.string.title_resend_otp);
        vSendOtp.setEnabled(true);

        if (titleSecurity != null
                && titleSecurity.getText() != null
                && !titleSecurity.getText().equals("")
                && !titleSecurity.getText().toString().equals(
                getString(R.string.content_security_question_email)))
            vSendOtpCall.setVisibility(View.VISIBLE);
    }

    @Override
    public void destroyTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void disableOtpButton() {
        MethodChecker.setBackground(vSendOtp, getResources().getDrawable(R.drawable.btn_transparent_disable));
        vSendOtp.setEnabled(false);
        vSendOtp.setTextColor(getResources().getColor(R.color.grey_600));
        vSendOtpCall.setVisibility(View.GONE);
    }

    @Override
    public void showTrueCaller(boolean b) {
        Drawable img = MethodChecker.getDrawable(getActivity(), R.drawable.truecaller);
        img.setBounds(0, 0, 75, 75);
        verifyTrueCaller.setCompoundDrawables(img, null, null, null);

        verifyTrueCaller.setVisibility(b ? View.VISIBLE : View.GONE);
        if (b) UnifyTracking.eventTruecallerImpression();
    }

    @Override
    public void initListener() {
        isRunningTimer = false;
        vSaveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vError.setVisibility(View.GONE);

                if (otpIsValid()) {
                    KeyboardHandler.DropKeyboard(getActivity(), view);
                    presenter.saveOTPAnswer(vInputOtp.getText().toString());// this is for rotation
                    presenter.doAnswerQuestion(vInputOtp.getText().toString());
                }
            }
        });

        vSendOtpCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.doRequestOtpWithCall();

            }
        });
        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OldSessionRouter.getChangePhoneNumberRequestActivity(getActivity());
                startActivity(intent);
            }
        });

        verifyTrueCaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getPhoneTrueCaller();
                UnifyTracking.eventClickTruecaller();
            }
        });
    }


    private boolean otpIsValid() {
        boolean isValid = true;
        //TODO: OTP validation
        if (vInputOtp.getText().length() == 0 || vInputOtp.getText().length() > 6 || vInputOtp.getText().length() < 6) {
            isValid = false;
            displayError(true);
            vInputOtp.requestFocus();
        }
        return isValid;
    }

    @Override
    public void showViewOtp() {
        vOtp.setVisibility(View.VISIBLE);
        vSecurity.setVisibility(View.GONE);
//        if (TrackingUtils.getGtmString(CAN_REQUEST_OTP_IMMEDIATELY).equals("true")
//                && !verifyTrueCaller.isShown())
//            presenter.doRequestOtp();
        titleOTP.setText("Hai " + SessionHandler.getTempLoginName(getActivity()) + ",");


        Spannable spannable = new SpannableString(getString(R.string.action_send_otp_with_call_2));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                              }
                          }
                , getString(R.string.action_send_otp_with_call_2).indexOf("lewat")
                , getString(R.string.action_send_otp_with_call_2).length()
                , 0);

        vSendOtpCall.setText(spannable, TextView.BufferType.SPANNABLE);

        Spannable spannable2 = new SpannableString(getString(R.string.content_change_number));

        spannable2.setSpan(new ClickableSpan() {
                               @Override
                               public void onClick(View view) {

                               }

                               @Override
                               public void updateDrawState(TextPaint ds) {
                                   ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                               }
                           }
                , getString(R.string.content_change_number).indexOf("klik disini")
                , getString(R.string.content_change_number).length()
                , 0);

        changeNumber.setText(spannable2, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void setModel(QuestionFormModel data) {
        Progress.dismiss();

        switch (presenter.determineQuestionType(data.getQuestion(), data.getTitle())) {
            case QuestionFormModel.OTP_No_HP_TYPE:
                vSendOtp.setText(presenter.getOtpSendString());
                vInputOtp.setEnabled(true);
                String phone = SessionHandler.getTempPhoneNumber(getActivity());
                phone = phone.substring(phone.length() - 4);
                String contentSecurity = String.format
                        ((getResources().getString(R.string.content_security_question_phone) + " " +
                                "<b>****-****- %s </b>"), phone);
                titleSecurity.setText(MethodChecker.fromHtml(contentSecurity));
                changeNumber.setVisibility(View.VISIBLE);
                vSendOtpCall.setVisibility(View.VISIBLE);
                presenter.showTrueCaller(getActivity());
                if (TrackingUtils.getGtmString(CAN_REQUEST_OTP_IMMEDIATELY).equals("true") && !verifyTrueCaller.isShown()) {
                    presenter.doRequestOtp();
                }
                vSendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.doRequestOtp();
                    }
                });
                break;
            case QuestionFormModel.OTP_Email_TYPE:
                vSendOtp.setText(presenter.getOtpSendString());
                vInputOtp.setEnabled(true);
                titleSecurity.setText(getResources().getString(R.string.content_security_question_email));
                changeNumber.setVisibility(View.GONE);
                vSendOtpCall.setVisibility(View.GONE);
                verifyTrueCaller.setVisibility(View.GONE);
                vSendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.doRequestOtpToEmail();
                    }
                });
                presenter.doRequestOtpToEmail();
                break;
        }
        vQuestion.setText(data.getTitle());
    }

    @Override
    public void requestOTP(OTPModel model) {
        if (model.getSuccess() == OTPModel.SUCCESS) {
            SnackbarManager.make(getActivity(), getResources().getString(R.string.msg_send_otp), Snackbar.LENGTH_LONG).show();
            vInputOtp.setEnabled(true);

        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.SECURITY_QUESTION;
    }

    @Override
    public void displayError(boolean isError) {
        if (isError)
            vError.setVisibility(View.VISIBLE);
        else
            vError.setVisibility(View.GONE);
    }

    @Override
    public void displayProgress(boolean isShow) {
        //[START] save progress for rotation
        presenter.updateViewModel(SecurityQuestionViewModel.IS_SECURITY_LOADING_TYPE, isShow);
        //[END] save progress for rotation
        if (isShow) {
            Progress.showDialog();
        } else {
            Progress.dismiss();
        }
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        Log.d(TAG, messageTAG + "retry arise !!!");
        SnackbarManager.make(getActivity(), getString(R.string.message_verification_timeout), Snackbar.LENGTH_SHORT).show();
        displayProgress(false);
        enableOtpButton();
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_SHORT).show();
        displayProgress(false);
        enableOtpButton();
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String) data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_SHORT).show();
        displayProgress(false);
        enableOtpButton();
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case DownloadService.MAKE_LOGIN:
                if (getActivity() != null && getActivity() instanceof SessionView) {
                    ((SessionView) getActivity()).destroy();
                }
                break;

            default:
                if (presenter != null)
                    presenter.setData(type, data);
                break;
        }
    }

    public void showError(String message) {
        displayProgress(false);
        if (message.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        enableOtpButton();
    }

    public void onSuccessRequestOTPWithCall(String message) {
        startTimer();
        displayProgress(false);
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).show();
    }

    public void onSuccessRequestOTP(String message) {
        startTimer();
        displayProgress(false);
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        FragmentSecurityQuestionPermissionsDispatcher.processOtpWithCheck(FragmentSecurityQuestion.this, otpCode);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void processOtp(String otpCode) {
        if (vInputOtp != null)
            vInputOtp.setText(otpCode);
        presenter.saveOTPAnswer(otpCode);
        presenter.doAnswerQuestion(otpCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentSecurityQuestionPermissionsDispatcher.onRequestPermissionsResult(FragmentSecurityQuestion.this, requestCode, grantResults);
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

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void checkSmsPermission() {

    }
}