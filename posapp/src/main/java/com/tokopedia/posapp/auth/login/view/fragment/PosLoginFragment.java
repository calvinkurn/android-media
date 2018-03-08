package com.tokopedia.posapp.auth.login.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.di.LoginQualifier;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.auth.login.di.qualifier.PosLoginQualifier;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.login.loginemail.view.presenter.LoginPresenter;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author okasurya on 2/23/18.
 */

public class PosLoginFragment extends BaseDaggerFragment implements Login.View {
    private AutoCompleteTextView emailEditText;
    private TextInputEditText passwordEditText;
    private TextView forgotPass;
    private LinearLayout loginLayout;
    private TextView loginButton;
    private TkpdHintTextInputLayout wrapperEmail;
    private TkpdHintTextInputLayout wrapperPassword;

    @PosLoginQualifier
    @Inject
    LoginPresenter presenter;

    public static Fragment createInstance() {
        return new PosLoginFragment();
    }

    @Override
    protected String getScreenName() {
        return PosLoginFragment.class.getSimpleName();
    }

    @Override
    protected void initInjector() {
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_pos_login, container, false);
        initView(parentView);
        setViewListener();
        return parentView;
    }

    private void initView(View view) {
        emailEditText = view.findViewById(R.id.email_auto);
        passwordEditText = view.findViewById(R.id.password);
        forgotPass = view.findViewById(R.id.forgot_pass);
        loginLayout = view.findViewById(R.id.login_layout);
        loginButton = view.findViewById(R.id.accounts_sign_in);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
    }

    private void setViewListener() {
        passwordEditText.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == com.tokopedia.session.R.id.ime_login || id == EditorInfo.IME_NULL) {
                            presenter.login(emailEditText.getText().toString().trim(),
                                    passwordEditText.getText().toString());
                            return true;
                        }

                        return false;
                    }
                });

        passwordEditText.addTextChangedListener(watcher(wrapperPassword));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                presenter.login(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString());
            }
        });

        emailEditText.addTextChangedListener(watcher(wrapperEmail));

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ForgotPasswordActivity.getCallingIntent(getActivity(), emailEditText.getText()
                        .toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
            }
        });
    }

    private TextWatcher watcher(final TkpdHintTextInputLayout wrapper) {
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
                    setWrapperError(wrapper, getString(com.tokopedia.core.R.string.error_field_required));
                }
            }
        };
    }

    private void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(null);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    @Override
    public void resetError() {

    }

    @Override
    public void showLoadingLogin() {

    }

    @Override
    public void showErrorPassword(int resId) {

    }

    @Override
    public void showErrorEmail(int resId) {

    }

    @Override
    public void dismissLoadingLogin() {

    }

    @Override
    public void onSuccessLogin() {

    }

    @Override
    public void onErrorLogin(String errorMessage) {

    }

    @Override
    public void setAutoCompleteAdapter(ArrayList<String> listId) {

    }

    @Override
    public void showLoadingDiscover() {

    }

    @Override
    public void dismissLoadingDiscover() {

    }

    @Override
    public void onErrorDiscoverLogin(String errorMessage) {

    }

    @Override
    public void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> providers) {

    }

    @Override
    public GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener() {
        return null;
    }

    @Override
    public void onGoToCreatePasswordPage(GetUserInfoDomainData getUserInfoDomainData) {

    }

    @Override
    public void onGoToPhoneVerification() {

    }

    @Override
    public void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String email, String phone) {

    }

    @Override
    public void setSmartLock() {

    }

    @Override
    public void resetToken() {

    }

    @Override
    public void onErrorLogin(String errorMessage, int codeError) {

    }

    @Override
    public void onGoToActivationPage(String email) {

    }

    @Override
    public void onSuccessLoginEmail() {

    }

    @Override
    public void onSuccessLoginSosmed(String loginMethod) {

    }

    @Override
    public void disableArrow() {

    }

    @Override
    public void enableArrow() {

    }
}
