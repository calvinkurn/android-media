package com.tokopedia.core.fragment;


import android.os.Bundle;
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
import com.tokopedia.core.session.model.LoginInterruptModel;
import com.tokopedia.core.session.model.OTPModel;
import com.tokopedia.core.session.model.QuestionFormModel;
import com.tokopedia.core.session.model.SecurityQuestionViewModel;
import com.tokopedia.core.session.presenter.SecurityQuestion;
import com.tokopedia.core.session.presenter.SecurityQuestionImpl;
import com.tokopedia.core.session.presenter.SecurityQuestionView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * modify by m.normansyah 9-11-2015,
 * complete MVP
 */
public class FragmentSecurityQuestion extends Fragment implements SecurityQuestionView {

    private TkpdProgressDialog Progress;
    SecurityQuestion securityQuestion;

    @Bind(R2.id.input_text)
    EditText vAnswer;
    @Bind(R2.id.input_otp)
    EditText vInputOtp;
    @Bind(R2.id.wrapper_input_text)
    TextInputLayout wrapperAnswer;
    @Bind(R2.id.wrapper_input_otp)
    TextInputLayout wrapperInputOtp;
    @Bind(R2.id.title)
    TextView vQuestion;
    @Bind(R2.id.view_security)
    View vSecurity;
    @Bind(R2.id.view_otp)
    View vOtp;
    @Bind(R2.id.view_error)
    View vError;
    @Bind(R2.id.send_otp)
    TextView vSendOtp;
    @Bind(R2.id.save_but)
    TextView vSaveBut;
    @Bind(R2.id.exit_but)
    TextView vCancelBut;
    @Bind(R2.id.error_title)
    TextView vErrorTitle;
    @Bind(R2.id.error_msg)
    TextView vErrorMessage;
    @Bind(R2.id.progress)
    ProgressBar vProgress;

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
        ButterKnife.bind(this, rootView);
        wrapperAnswer.setHintEnabled(false);
        wrapperInputOtp.setHintEnabled(false);
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
        ButterKnife.unbind(this);
    }

    @Override
    public void setAnswerOTP(String text) {
        vInputOtp.setText(text);
    }

    @Override
    public void setAnswerSecurity(String text) {
        vAnswer.setText(text);
    }

    @Override
    public void initListener() {
        vSaveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vError.setVisibility(View.GONE);

                if (otpIsValid()) {
                    KeyboardHandler.DropKeyboard(getActivity(), view);
                    wrapperInputOtp.setError(null);
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
        vCancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // destroy
                SessionHandler.clearUserData(getActivity());// delete every thing that saved during login
                if (getActivity() != null && getActivity() instanceof SessionView) {
                    ((SessionView) getActivity()).destroy();
                }
            }
        });
    }

    private boolean otpIsValid() {
        boolean isValid = true;
        if(vInputOtp.getText().length() == 0){
            isValid = false;
            wrapperInputOtp.setError(getString(R.string.error_field_required));
            vInputOtp.requestFocus();
        }
        else if (vInputOtp.getText().length() > 6) {
            isValid = false;
            wrapperInputOtp.setError(getString(R.string.error_max_otp));
            vInputOtp.requestFocus();
        } else if (vInputOtp.getText().length() < 6) {
            isValid = false;
            wrapperInputOtp.setError(getString(R.string.error_min_otp));
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
    }


    @Override
    public void setModel(QuestionFormModel data) {
        Progress.dismiss();

        switch (securityQuestion.determineQuestionType(data.getQuestion(), data.getTitle())) {
            case QuestionFormModel.OTP_No_HP_TYPE:
                showViewOtp();
                vSendOtp.setText(securityQuestion.getOtpSendString());
                vInputOtp.setEnabled(true);
                break;
            case QuestionFormModel.OTP_Email_TYPE:
                showViewOtp();
                vSendOtp.setText(securityQuestion.getOtpSendString());
                vInputOtp.setEnabled(true);
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
}
