package com.tokopedia.core.manage.people.profile.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.manage.people.profile.listener.EmailVerificationView;
import com.tokopedia.core.manage.people.profile.presenter.EmailVerificationPresenter;
import com.tokopedia.core.manage.people.profile.presenter.EmailVerificationPresenterImpl;
import com.tokopedia.core.util.MethodChecker;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 9/29/16.
 */

public class EmailVerificationDialogFragment extends DialogFragment implements EmailVerificationView {

    private static final String HAS_CHANGE_EMAIL_TIMER = "HAS_CHANGE_EMAIL_TIMER";
    private static final String PARAM_USER_EMAIL = "user_email";
    private static final String FORMAT = "%02d:%02d";
    private static final String CACHE_CHANGE_EMAIL = "CACHE_CHANGE_EMAIL";

    @BindView(R2.id.current_email)
    TextView currentEmail;

    @BindView(R2.id.email_input)
    EditText emailInput;

    @BindView(R2.id.password)
    EditText userPassword;

    @BindView(R2.id.input_otp_code)
    EditText inputOtpCodeField;

    @BindView(R2.id.code_confirm_button)
    TextView confirmButton;

    @BindView(R2.id.abort_button)
    TextView cancelButton;

    @BindView(R2.id.close_button)
    TextView closeButton;

    @BindView(R2.id.request_otp_code)
    TextView requestOTPButton;

    @BindView(R2.id.instruction_check_new_email)
    View checkEmailInstruction;

    @BindView(R2.id.change_email_layout)
    View changeEmailLayout;

    public interface EmailChangeConfirmationListener {
        public void onEmailChanged();
    }

    TkpdProgressDialog mTkpdProgressDialog;
    EmailVerificationPresenter presenter;
    EmailChangeConfirmationListener listener;

    CountDownTimer countDownTimer;
    LocalCacheHandler cacheHandler;
    boolean isRunningTimer;

    public static EmailVerificationDialogFragment createInstance(String userEmail, EmailChangeConfirmationListener listener) {
        EmailVerificationDialogFragment fragment = new EmailVerificationDialogFragment();
        fragment.setEmailChangeListener(listener);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_EMAIL, userEmail);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
    }

    private void initialPresenter() {
        presenter = new EmailVerificationPresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    private int getFragmentLayout() {
        return R.layout.dialog_email_verification;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
        setViewListener();
        initialVar();
        setActionVar();

        if (!cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_CHANGE_EMAIL_TIMER, false)) {
            inputOtpCodeField.setEnabled(true);
            startTimer();
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void initView(View view) {
        mTkpdProgressDialog = new TkpdProgressDialog(getActivity(),
                TkpdProgressDialog.NORMAL_PROGRESS);
        currentEmail.setText(getArguments().getString(PARAM_USER_EMAIL));

    }

    private void setViewListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                presenter.changeEmailClicked();
            }
        });
        cancelButton.setOnClickListener(onDismissListener());
        closeButton.setOnClickListener(onDismissListener());
        requestOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.onRequestOTP();
            }
        });

    }

    private View.OnClickListener onDismissListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    private void initialVar() {

    }

    private void setActionVar() {
        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_CHANGE_EMAIL);

    }

    @Override
    public void showLoadingProgress() {
        mTkpdProgressDialog.showDialog();
    }

    @Override
    public void onSuccessRequestOTP() {
        startTimer();
        Toast.makeText(getActivity(), getString(R.string.success_send_otp), Toast.LENGTH_LONG).show();
    }

    private void startTimer() {

        if (cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_CHANGE_EMAIL_TIMER, false)) {
            cacheHandler.putBoolean(HAS_CHANGE_EMAIL_TIMER, true);
            cacheHandler.setExpire(90);
            cacheHandler.applyEditor();
        }

        if(!isRunningTimer){
            countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    try {
                        isRunningTimer = true;

                        MethodChecker.setBackground(requestOTPButton, getResources().getDrawable(R.drawable.btn_transparent_disable));
                        requestOTPButton.setTextColor(getResources().getColor(R.color.grey_600));
                        requestOTPButton.setEnabled(false);
                        requestOTPButton.setText(String.format(FORMAT,
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
                        isRunningTimer = false;
                        enableOtpButton();
                    } catch (Exception e) {

                    }
                }

            }.start();
        }

        requestOTPButton.requestFocus();
    }

    private void enableOtpButton() {
        requestOTPButton.setTextColor(getResources().getColor(R.color.white));
        MethodChecker.setBackground(requestOTPButton, getResources().getDrawable(R.drawable.btn_buy));
        requestOTPButton.setText(R.string.title_send_verification_code);
        requestOTPButton.setEnabled(true);
    }


    @Override
    public void finishLoading() {
        mTkpdProgressDialog.dismiss();
    }

    @Override
    public void showErrorToast(String error) {
        if (error.equals(""))
            CommonUtils.UniversalToast(getActivity(), getString(R.string.msg_network_error));
        else
            CommonUtils.UniversalToast(getActivity(), error);
    }

    @Override
    public EditText getEmail() {
        return emailInput;
    }

    @Override
    public EditText getPassword() {
        return userPassword;
    }

    @Override
    public EditText getOTP() {
        return inputOtpCodeField;
    }

    @Override
    public void onSuccessChangeEmail() {
        changeEmailLayout.setVisibility(View.GONE);
        checkEmailInstruction.setVisibility(View.VISIBLE);
        listener.onEmailChanged();
    }

    @Override
    public void setEmailChangeListener(EmailChangeConfirmationListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}