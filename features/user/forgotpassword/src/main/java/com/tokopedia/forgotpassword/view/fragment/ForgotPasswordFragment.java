package com.tokopedia.forgotpassword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.forgotpassword.R;
import com.tokopedia.forgotpassword.analytics.ForgotPasswordAnalytics;
import com.tokopedia.forgotpassword.di.DaggerForgotPasswordComponent;
import com.tokopedia.forgotpassword.di.ForgotPasswordComponent;
import com.tokopedia.forgotpassword.view.listener.ForgotPasswordContract;
import com.tokopedia.forgotpassword.view.presenter.ForgotPasswordPresenter;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordFragment extends BaseDaggerFragment
        implements ForgotPasswordContract.View {

    private static final String ARGS_EMAIL = "ARGS_EMAIL";
    private static final String ARGS_AUTO_RESET = "ARGS_AUTO_RESET";
    private static final String ARGS_REMOVE_FOOTER = "ARGS_REMOVE_FOOTER";
    View frontView;
    View successView;
    TextView emailSend;
    TextView sendButton;
    EditText emailEditText;
    TextInputLayout tilEmail;
    TextView registerButton;

    ProgressBar progressBar;

    @Inject
    ForgotPasswordAnalytics analytics;

    @Inject
    ForgotPasswordPresenter presenter;

    @Inject
    UserSessionInterface userSession;

    public static ForgotPasswordFragment createInstance(String email, boolean isAutoReset, boolean isRemoveFooter) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_EMAIL, email);
        bundle.putBoolean(ARGS_AUTO_RESET, isAutoReset);
        bundle.putBoolean(ARGS_REMOVE_FOOTER, isRemoveFooter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            ForgotPasswordComponent forgotPasswordComponent = DaggerForgotPasswordComponent.builder()
                    .baseAppComponent(
                            ((BaseMainApplication) getActivity().getApplication())
                                    .getBaseAppComponent())
                    .build();

            forgotPasswordComponent.inject(this);
            presenter.attachView(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        frontView = view.findViewById(R.id.front_view);
        successView = view.findViewById(R.id.success_view);
        emailSend = view.findViewById(R.id.email_send);
        sendButton = view.findViewById(R.id.send_button);
        emailEditText = view.findViewById(R.id.email);
        tilEmail = view.findViewById(R.id.til_email);
        registerButton = view.findViewById(R.id.register_button);
        progressBar = view.findViewById(R.id.progress_bar);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().getBoolean(ARGS_AUTO_RESET)) {
            onSuccessResetPassword();
        }
        if (getArguments().getBoolean(ARGS_REMOVE_FOOTER, false)) {
            registerButton.setVisibility(View.GONE);
        }
    }

    protected void initView() {
        registerButton.setOnClickListener(v -> goToRegister());
        emailEditText.addTextChangedListener(watcher(tilEmail));
        sendButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                KeyboardHandler.DropKeyboard(getActivity(), emailEditText);
                frontView.setVisibility(View.GONE);
                presenter.resetPassword(emailEditText.getText().toString().trim());
            }
        });

        if (getArguments() != null && !getArguments().getString(ARGS_EMAIL, "").equals("")) {
            emailEditText.setText(getArguments().getString(ARGS_EMAIL));
        }

        if (userSession.isLoggedIn()) {
            registerButton.setVisibility(View.GONE);
        } else {
            registerButton.setVisibility(View.VISIBLE);

            String sourceString = "Belum punya akun? " + "Daftar Sekarang";

            Spannable spannable = new SpannableString(sourceString);

            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View view) {

                                  }

                                  @Override
                                  public void updateDrawState(TextPaint ds) {
                                      ds.setUnderlineText(true);
                                      ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                                  }
                              }
                    , sourceString.indexOf("Daftar")
                    , sourceString.length()
                    , 0);

            registerButton.setText(spannable, TextView.BufferType.SPANNABLE);
        }

    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                }
            }
        };
    }


    private void setWrapperError(TextInputLayout wrapper, String s) {
        wrapper.setError(s);

        if (s == null) {
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
        }
    }

    private void goToRegister() {
        if (getActivity() != null) {
            Intent intent = ((ApplinkRouter) getActivity().getApplicationContext())
                    .getApplinkIntent(
                            getActivity(), ApplinkConst.REGISTER);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void resetError() {
        emailEditText.setError(null);

    }

    @Override
    public void showLoadingProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorResetPassword(String errorMessage) {
        finishLoadingProgress();
        frontView.setVisibility(View.VISIBLE);

        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessResetPassword() {
        finishLoadingProgress();
        frontView.setVisibility(View.GONE);
        successView.setVisibility(View.VISIBLE);
        String myData = getString(R.string.title_reset_success_hint_1) + "\n"
                + emailEditText.getText().toString() + ".\n"
                + getString(R.string.title_reset_success_hint_2);

        emailSend.setText(myData);

        analytics.trackSuccessForgotPassword();

    }

    @Override
    public void setEmailError(String error) {
        frontView.setVisibility(View.VISIBLE);

        tilEmail.setErrorEnabled(true);
        tilEmail.setError(error);
    }

    private void finishLoadingProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return ForgotPasswordAnalytics.Screen.FORGOT_PASSWORD;
    }
}
