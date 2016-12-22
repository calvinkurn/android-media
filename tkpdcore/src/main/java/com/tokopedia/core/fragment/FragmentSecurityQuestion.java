package com.tokopedia.core.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.session.model.LoginInterruptModel;
import com.tokopedia.core.session.model.OTPModel;
import com.tokopedia.core.session.model.QuestionFormModel;
import com.tokopedia.core.session.model.SecurityQuestionViewModel;
import com.tokopedia.core.session.presenter.SecurityQuestion;
import com.tokopedia.core.session.presenter.SecurityQuestionImpl;
import com.tokopedia.core.session.presenter.SecurityQuestionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * modify by m.normansyah 9-11-2015,
 * complete MVP
 */
public class FragmentSecurityQuestion extends Fragment implements SecurityQuestionView {

    private TkpdProgressDialog Progress;
    SecurityQuestion securityQuestion;
    private static final String FORMAT = "%02d:%02d";

    @BindView(R2.id.input_text)
    EditText vAnswer;
    @BindView(R2.id.input_otp)
    EditText vInputOtp;
    @BindView(R2.id.title_otp)
    TextView titleOTP;
    @BindView(R2.id.phone_number)
    EditText phoneNumber;
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

    CountDownTimer countDownTimer;
    private Unbinder unbinder;

    public interface SecurityQuestionListener {
        public void onSuccess();

        public void onFailed();
    }

    private SecurityQuestionListener Listener;


    public static FragmentSecurityQuestion newInstance(int Security1, int Security2, String UserID, SecurityQuestionListener listener) {
        FragmentSecurityQuestion fragment = new FragmentSecurityQuestion();
        Bundle args = new Bundle();
        args.putInt(SECURITY_1_KEY, Security1);
        args.putInt(SECURITY_2_KEY, Security2);
        args.putString(USER_ID_KEY, UserID);
        fragment.setArguments(args);
        fragment.Listener = listener;
        return fragment;
    }

    public FragmentSecurityQuestion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        securityQuestion = new SecurityQuestionImpl(this);
        securityQuestion.getDataAfterRotate(savedInstanceState);
        securityQuestion.initInstances(getActivity());
        securityQuestion.getDataFromArgument(getArguments());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save last input by user
        if (securityQuestion.isSecurityQuestion() && securityQuestion.isValidForm(vAnswer.getText().toString())) {
            securityQuestion.saveAnswer(vAnswer.getText().toString());// this is for rotation
        }
        if (securityQuestion.isOtp() && securityQuestion.isValidForm(vInputOtp.getText().toString())) {
            securityQuestion.saveOTPAnswer(vInputOtp.getText().toString());// this is for rotation
        }
        // save remaining data from user
        securityQuestion.saveDataBeforeRotate(outState);
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
        if (!securityQuestion.isAfterRotate()) // if not rotate get question
            securityQuestion.getQuestionForm();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (securityQuestion.isAfterRotate())
            securityQuestion.initDataAfterRotate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void disableButton() {
        vSendOtp.setBackground(getResources().getDrawable(R.drawable.btn_transparent_disable));
        vSendOtp.setEnabled(false);
        vSendOtp.setTextColor(getResources().getColor(R.color.grey_600));

        vSendOtpCall.setBackground(getResources().getDrawable(R.drawable.btn_transparent_disable));
        vSendOtpCall.setEnabled(false);
        vSendOtpCall.setTextColor(getResources().getColor(R.color.grey_600));

        countDownTimer = new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                try {
                    vSendOtp.setBackground(getResources().getDrawable(R.drawable.btn_transparent_disable));
                    vSendOtp.setEnabled(false);
                    vSendOtp.setText("" + String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                    vSendOtpCall.setBackground(getResources().getDrawable(R.drawable.btn_transparent_disable));
                    vSendOtpCall.setEnabled(false);
                    vSendOtpCall.setText("" + String.format(FORMAT,
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
                    vSendOtp.setTextColor(getResources().getColor(R.color.tkpd_green_onboarding));
                    vSendOtp.setBackground(getResources().getDrawable(R.drawable.btn_share_transaparent));
                    vSendOtp.setText("Kirim ulang OTP");
                    vSendOtp.setEnabled(true);

                    vSendOtpCall.setTextColor(getResources().getColor(R.color.tkpd_green_onboarding));
                    vSendOtpCall.setBackground(getResources().getDrawable(R.drawable.btn_share_transaparent));
                    vSendOtpCall.setText("Kirim ulang OTP dengan telpon");
                    vSendOtpCall.setEnabled(true);
                } catch (Exception e) {

                }
            }

        }.start();
        vInputOtp.requestFocus();
    }

    @Override
    public void destroyTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void showOTPWithCall() {
        vSendOtpCall.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        vSaveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vError.setVisibility(View.GONE);

                if (otpIsValid()) {
                    KeyboardHandler.DropKeyboard(getActivity(), view);
                    securityQuestion.saveOTPAnswer(vInputOtp.getText().toString());// this is for rotation
                    securityQuestion.doAnswerQuestion(vInputOtp.getText().toString());
                }
            }
        });
        vSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                securityQuestion.doRequestOtp();

            }
        });

        vSendOtpCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                securityQuestion.doRequestOtpWithCall();

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
    public void showViewSecurity() {
        vSecurity.setVisibility(View.VISIBLE);
        vOtp.setVisibility(View.GONE);
    }

    @Override
    public void showViewOtp() {
        vOtp.setVisibility(View.VISIBLE);
        vSecurity.setVisibility(View.GONE);
        securityQuestion.doRequestOtp();
        titleOTP.setText("Halo, " + SessionHandler.getTempLoginName(getActivity()));
    }


    @Override
    public void setModel(QuestionFormModel data) {
        Progress.dismiss();

        switch (securityQuestion.determineQuestionType(data.getQuestion(), data.getTitle())) {
            case QuestionFormModel.OTP_No_HP_TYPE:
                vSendOtp.setText(securityQuestion.getOtpSendString());
                vInputOtp.setEnabled(true);
                titleSecurity.setText(getResources().getString(R.string.content_security_question_phone));
                String phone = SessionHandler.getTempPhoneNumber(getActivity());
                phone = phone.substring(phone.length() - 4);
                phoneNumber.setText(new StringBuilder().append("XXXX-XXXX-").append(phone).toString());
                phoneNumber.setVisibility(View.VISIBLE);
                break;
            case QuestionFormModel.OTP_Email_TYPE:
                vSendOtp.setText(securityQuestion.getOtpSendString());
                vInputOtp.setEnabled(true);
                titleSecurity.setText(getResources().getString(R.string.content_security_question_email));
                phoneNumber.setVisibility(View.GONE);
                break;
        }
        ;
        vQuestion.setText(data.getTitle());
    }

    @Override
    public void finishSecurityQuestion(LoginInterruptModel loginInterruptModel) {
        throw new RuntimeException("don't use this anymore !!!");
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
        securityQuestion.updateViewModel(SecurityQuestionViewModel.IS_SECURITY_LOADING_TYPE, isShow);
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
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_SHORT).show();
        displayProgress(false);
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String) data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_SHORT).show();
        displayProgress(false);
    }

    @Override
    public void setData(int type, Bundle data) {
        if (securityQuestion != null)
            securityQuestion.setData(type, data);
    }

    public void showError(String message) {
        displayProgress(false);
        if (message.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    public void onSuccessRequestOTPWithCall(String message) {
        disableButton();
        displayProgress(false);
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).show();
    }

    public void onSuccessRequestOTP(String message) {
        disableButton();
        displayProgress(false);
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).show();
    }
}