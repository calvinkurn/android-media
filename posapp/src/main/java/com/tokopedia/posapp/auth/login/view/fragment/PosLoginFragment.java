package com.tokopedia.posapp.auth.login.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.auth.login.di.component.PosLoginComponent;
import com.tokopedia.posapp.auth.login.di.component.DaggerPosLoginComponent;
import com.tokopedia.posapp.auth.login.view.PosLogin;
import com.tokopedia.posapp.outlet.view.activity.OutletActivity;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.register.view.activity.SmartLockActivity;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author okasurya on 2/23/18.
 */

public class PosLoginFragment extends BaseLoginFragment implements PosLogin.View {
    private AutoCompleteTextView emailEditText;
    private TextInputEditText passwordEditText;
    private TextView forgotPass;
    private View loadingView;
    private LinearLayout loginLayout;
    private TextView loginButton;
    private TkpdHintTextInputLayout wrapperEmail;
    private TkpdHintTextInputLayout wrapperPassword;
    private Button buttonDevOpt;

    @Inject
    PosLogin.Presenter presenter;

    @Inject
    GlobalCacheManager cacheManager;

    @Inject
    SessionHandler sessionHandler;

    public static Fragment createInstance() {
        return new PosLoginFragment();
    }

    @Override
    protected String getScreenName() {
        return PosLoginFragment.class.getSimpleName();
    }

    @Override
    protected void initInjector() {
        PosLoginComponent posLoginComponent = (DaggerPosLoginComponent) DaggerPosLoginComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .build();
        posLoginComponent.inject(this);
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
        loadingView = view.findViewById(R.id.login_status);
        loginLayout = view.findViewById(R.id.login_layout);
        loginButton = view.findViewById(R.id.accounts_sign_in);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        if(GlobalConfig.DEBUG) {
            buttonDevOpt = view.findViewById(R.id.button_devopt);
            buttonDevOpt.setVisibility(View.VISIBLE);
            buttonDevOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), DeveloperOptions.class));
                }
            });
        }
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


//     if (!loginEmailDomain.getInfo().getGetUserInfoDomainData().isCreatedPassword()) {
//        view.onGoToCreatePasswordPage(loginEmailDomain.getInfo()
//                .getGetUserInfoDomainData());
//    } else if (loginEmailDomain.getLoginResult() != null
//            && !goToSecurityQuestion(loginEmailDomain.getLoginResult())
//            && (isMsisdnVerified(loginEmailDomain.getInfo()) || GlobalConfig.isSellerApp())) {
//        view.dismissLoadingLogin();
//        view.setSmartLock();
//        view.onSuccessLoginEmail();
//    } else if (!goToSecurityQuestion(loginEmailDomain.getLoginResult())
//            && !isMsisdnVerified(loginEmailDomain.getInfo())) {
//        view.setSmartLock();
//        view.onGoToPhoneVerification();
//    } else if (goToSecurityQuestion(loginEmailDomain.getLoginResult())) {
//        view.setSmartLock();
//        view.onGoToSecurityQuestion(
//                loginEmailDomain.getLoginResult().getSecurityDomain(),
//                loginEmailDomain.getLoginResult().getFullName(),
//                loginEmailDomain.getInfo().getGetUserInfoDomainData().getEmail(),
//                loginEmailDomain.getInfo().getGetUserInfoDomainData().getPhone());
//    } else {
//        view.dismissLoadingLogin();
//        view.resetToken();
//        view.onErrorLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
//    }

    @Override
    public void resetError() {

    }

    @Override
    public void showLoadingLogin() {
        showLoading(true);
    }

    @Override
    public void showErrorPassword(int resId) {
        setWrapperError(wrapperPassword, getString(resId));
        passwordEditText.requestFocus();
        UnifyTracking.eventLoginError(AppEventTracking.EventLabel.PASSWORD);
    }

    @Override
    public void showErrorEmail(int resId) {
        setWrapperError(wrapperEmail, getString(resId));
        emailEditText.requestFocus();
        UnifyTracking.eventLoginError(AppEventTracking.EventLabel.EMAIL);
    }

    @Override
    public void dismissLoadingLogin() {
        showLoading(false);
    }

    @Override
    public void onSuccessLogin() {

    }

    @Override
    public void onErrorLogin(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
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
        startActivityForResult(
                PhoneVerificationActivationActivity.getCallingIntent(getActivity()),
                REQUEST_VERIFY_PHONE);
    }

    @Override
    public void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String email, String phone) {
        goToSecurityQuestion(securityDomain, fullName, email, phone, cacheManager);
    }

    @Override
    public void setSmartLock() {
        saveSmartLock(SmartLockActivity.RC_SAVE_SECURITY_QUESTION,
                emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void resetToken() {
        presenter.resetToken();
    }

    @Override
    public void onErrorLogin(String errorMessage, int codeError) {

    }

    @Override
    public void onGoToActivationPage(String email) {

    }

    @Override
    public void onSuccessLoginEmail() {
        startActivity(OutletActivity.newTopIntent(getActivity()));
        getActivity().finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SMART_LOCK
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getExtras() != null
                && data.getExtras().getString(SmartLockActivity.USERNAME) != null
                && data.getExtras().getString(SmartLockActivity.PASSWORD) != null) {
            emailEditText.setText(data.getExtras().getString(SmartLockActivity.USERNAME));
            passwordEditText.setText(data.getExtras().getString(SmartLockActivity.PASSWORD));
            presenter.login(data.getExtras().getString(SmartLockActivity.USERNAME),
                    data.getExtras().getString(SmartLockActivity.PASSWORD));
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_LOGIN_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_LOGIN_PHONE_NUMBER && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_VERIFY_PHONE) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showLoading(final boolean isLoading) {
        int shortAnimTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        loadingView.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (loadingView != null) {
                            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                        }
                    }
                });

    }
}
