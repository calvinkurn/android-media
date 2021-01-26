package com.tokopedia.loginregister.registerinitial.view.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent;
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestData;
import com.tokopedia.loginregister.registerinitial.view.util.RegisterUtil;
import com.tokopedia.loginregister.registerinitial.viewmodel.RegisterInitialViewModel;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity;
import com.tokopedia.unifycomponents.LoaderUnify;
import com.tokopedia.unifycomponents.TextFieldUnify;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.usecase.coroutines.Fail;
import com.tokopedia.usecase.coroutines.Success;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author by nisie on 10/25/18.
 */
public class RegisterEmailFragment extends BaseDaggerFragment {

    private static final int REQUEST_AUTO_LOGIN = 101;
    private static final int REQUEST_ACTIVATE_ACCOUNT = 102;

    int PASSWORD_MINIMUM_LENGTH = 8;

    String NAME = "NAME";
    String PASSWORD = "PASSWORD";
    String EMAIL = "EMAIL";

    String TERM_CONDITION = "Syarat dan Ketentuan";
    String PRIVACY_POLICY = "Kebijakan Privasi";

    String TERM_CONDITION_URL = "launch.TermPrivacy://parent?param=0";
    String PRIVACY_POLICY_URL = "launch.TermPrivacy://parent?param=1";

    private static final String ALREADY_REGISTERED = "sudah terdaftar";

    View redirectView;
    UnifyButton registerButton;
    TextFieldUnify wrapperName;
    TextFieldUnify wrapperEmail;
    TextFieldUnify wrapperPassword;
    Typography registerNextTAndC;
    LoaderUnify progressBar;

    String source = "";
    String token = "";

    @Inject
    LoginRegisterAnalytics analytics;

    @Inject
    RegisterAnalytics registerAnalytics;

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    UserSessionInterface userSession;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    ViewModelProvider viewModelProvider;
    RegisterInitialViewModel registerInitialViewModel;

    //** see fragment_register_email
    private int REGISTER_BUTTON_IME = 123321;

    public static RegisterEmailFragment createInstance(Bundle bundle) {
        RegisterEmailFragment fragment = new RegisterEmailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.trackScreen(getActivity(), getScreenName());
    }

    @Override
    protected void initInjector() {
        DaggerRegisterInitialComponent daggerLoginComponent =
                (DaggerRegisterInitialComponent) DaggerRegisterInitialComponent
                        .builder()
                        .loginRegisterComponent(getComponent(LoginRegisterComponent.class))
                        .build();

        daggerLoginComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return LoginRegisterAnalytics.Companion.getSCREEN_REGISTER_EMAIL();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelProvider = ViewModelProviders.of(this, viewModelFactory);
        registerInitialViewModel =  viewModelProvider.get(RegisterInitialViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_with_email, parent, false);
        redirectView = view.findViewById(R.id.redirect_reset_password);
        registerButton = view.findViewById(R.id.register_button);
        wrapperName = view.findViewById(R.id.wrapper_name);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        progressBar = view.findViewById(R.id.progress_bar);
        registerNextTAndC = view.findViewById(R.id.register_next_detail_t_and_p);
        prepareView();
        setViewListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) { }


    private void prepareView() {
        if (getArguments() != null) {
            wrapperEmail.getTextFieldInput().setText(getArguments().getString(ApplinkConstInternalGlobal.PARAM_EMAIL, ""));
            wrapperEmail.getTextFieldInput().setEnabled(false);
            token = getArguments().getString(ApplinkConstInternalGlobal.PARAM_TOKEN, "");
            source = getArguments().getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "");
        }

        initObserver();

        String sourceString = getActivity().getString(R.string.bottom_info_terms_and_privacy2);

        SpannableString spannable = new SpannableString(sourceString);

        ClickableSpan clickableSpanTermCondition = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                registerAnalytics.trackClickTermConditionButton();
                if(getActivity() != null){
                    Intent intent = new Intent (Intent.ACTION_VIEW);
                    intent.setData (Uri.parse(TERM_CONDITION_URL));
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setColor(MethodChecker.getColor(registerNextTAndC.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
            }
        };

        ClickableSpan clickableSpanPrivacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                registerAnalytics.trackClickPrivacyPolicyButton();
                if(getActivity() != null){
                    Intent intent = new Intent (Intent.ACTION_VIEW);
                    intent.setData (Uri.parse(PRIVACY_POLICY_URL));
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setColor(MethodChecker.getColor(registerNextTAndC.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
            }
        };

        spannable.setSpan(clickableSpanTermCondition, sourceString.indexOf(TERM_CONDITION),
                sourceString.indexOf(TERM_CONDITION) + TERM_CONDITION.length(), 0);

        spannable.setSpan(clickableSpanPrivacyPolicy, sourceString.indexOf(PRIVACY_POLICY),
                sourceString.indexOf(PRIVACY_POLICY) + PRIVACY_POLICY.length(), 0);

        registerNextTAndC.setText(spannable, TextView.BufferType.SPANNABLE);
        registerNextTAndC.setMovementMethod(LinkMovementMethod.getInstance());

        showPasswordHint();
        showNameHint();
    }

    private void initObserver(){
        registerInitialViewModel.getRegisterRequestResponse().observe(getViewLifecycleOwner(), registerRequestDataResult -> {
            if(registerRequestDataResult instanceof Success){
                RegisterRequestData data = ((Success<RegisterRequestData>) registerRequestDataResult).getData();
                userSession.clearToken();
                userSession.setToken(data.getAccessToken(), data.getTokenType(), data.getRefreshToken());
                onSuccessRegister();
                if(getActivity() != null){
                    Intent intent = new Intent();
                    intent.putExtra(ApplinkConstInternalGlobal.PARAM_ACTION, data.getAction());
                    intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }else if(registerRequestDataResult instanceof Fail){
                Throwable throwable = ((Fail) registerRequestDataResult).getThrowable();
                dismissLoadingProgress();
                if (throwable instanceof MessageErrorException
                        && throwable.getMessage() != null
                        && throwable.getMessage().contains(ALREADY_REGISTERED)) {
                    showInfo();
                } else  if (throwable instanceof MessageErrorException
                        && throwable.getMessage() != null) {
                    onErrorRegister(throwable.getMessage());
                }else {
                    if(getContext() != null)
                    {
                        String forbiddenMessage = getContext().getString(
                                com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth);
                        String errorMessage = ErrorHandler.getErrorMessage(getContext(), throwable);
                        if (errorMessage.equals(forbiddenMessage)){
                            onForbidden();
                        } else {
                            onErrorRegister(errorMessage);
                        }
                    }

                }
            }
        });
    }

    private Spannable getSpannable(String sourceString, String hyperlinkString) {
        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400));
                              }
                          }
                , sourceString.indexOf(hyperlinkString)
                , sourceString.length()
                , 0);


        return spannable;
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            wrapperName.getTextFieldInput().setText(savedInstanceState.getString(NAME, ""));
            wrapperEmail.getTextFieldInput().setText(savedInstanceState.getString(EMAIL, ""));
            wrapperEmail.getTextFieldInput().setEnabled(false);
            wrapperPassword.getTextFieldInput().setText(savedInstanceState.getString(PASSWORD, ""));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        wrapperEmail.getTextFieldInput().addTextChangedListener(emailWatcher(wrapperEmail));
        wrapperPassword.getTextFieldInput().addTextChangedListener(passwordWatcher(wrapperPassword));
        wrapperName.getTextFieldInput().addTextChangedListener(nameWatcher(wrapperName));

        if (getActivity() != null && userSession.isLoggedIn()) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }


    private TextWatcher nameWatcher(final TextFieldUnify wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperErrorNew(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                showNameHint();
                if (s.length() == 0) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_field_required));
                } else if (s.length() < 3) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_minimal_name));
                } else if (RegisterUtil.checkRegexNameLocal(wrapperName.getTextFieldInput().getText().toString())) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_illegal_character));
                } else if (RegisterUtil.isExceedMaxCharacter(wrapperName.getTextFieldInput().getText().toString())) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_max_35_character));
                }

                checkIsValidForm();
            }
        };
    }

    private TextWatcher passwordWatcher(final TextFieldUnify wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperErrorNew(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                showPasswordHint();
                if (s.length() == 0) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_field_required));
                } else if (wrapperPassword.getTextFieldInput().getText().toString().length() < PASSWORD_MINIMUM_LENGTH) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_minimal_password));
                }

                checkIsValidForm();
            }
        };
    }

    private TextWatcher emailWatcher(final TextFieldUnify wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperErrorNew(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    setWrapperErrorNew(wrapper, getString(R.string.error_field_required));
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(wrapperEmail.getTextFieldInput().getText().toString()).matches()) {
                    setWrapperErrorNew(wrapper, getString(R.string.wrong_email_format));
                }

                checkIsValidForm();
            }
        };
    }

    public List<String> getEmailListOfAccountsUserHasLoggedInto() {
        Set<String> listOfAddresses = new LinkedHashSet<>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                listOfAddresses.add(account.name);
            }
        }
        return new ArrayList<>(listOfAddresses);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setViewListener() {
        wrapperPassword.getTextFieldInput().setOnEditorActionListener((v, id, event) -> {
            if (id == REGISTER_BUTTON_IME || id == EditorInfo.IME_NULL) {
                registerEmail();
                return true;
            }
            return false;
        });

        registerButton.setOnClickListener(v -> registerEmail());
    }

    private void registerEmail(){
        showLoadingProgress();
        registerAnalytics.trackClickSignUpButtonEmail();
        registerInitialViewModel.registerRequest(
                wrapperEmail.getTextFieldInput().getText().toString(),
                wrapperPassword.getTextFieldInput().getText().toString(),
                wrapperName.getTextFieldInput().getText().toString(),
                token
        );
    }

    boolean isCanRegister(String name, String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(password)) {
            isValid = false;
        } else if (password.length() < PASSWORD_MINIMUM_LENGTH) {
            isValid = false;
        }

        if (TextUtils.isEmpty(name)) {
            isValid = false;
        } else if (RegisterUtil.checkRegexNameLocal(name)) {
            isValid = false;
        } else if (RegisterUtil.isBelowMinChar(name)) {
            isValid = false;
        } else if (RegisterUtil.isExceedMaxCharacter(name)) {
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
        }

        return isValid;
    }

    private void checkIsValidForm() {
        if (isCanRegister(wrapperName.getTextFieldInput().getText().toString(), wrapperEmail.getTextFieldInput().getText().toString(), wrapperPassword.getTextFieldInput().getText().toString())) {
            setRegisterButtonEnabled();
        } else {
            setRegisterButtonDisabled();
        }
    }

    private void setRegisterButtonEnabled() {
        if (getActivity() != null) {
            registerButton.setEnabled(true);
        }
    }


    private void setRegisterButtonDisabled() {
        if (getActivity() != null) {
            registerButton.setEnabled(false);
        }
    }


    private void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
        wrapper.setHelperEnabled(false);
        wrapper.setHelper(null);
        if (s == null) {
            wrapper.setError(null);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    private void setWrapperErrorNew(TextFieldUnify wrapper, String s) {
        if (s == null) {
            wrapper.setMessage("");
            wrapper.setError(false);
        } else {
            wrapper.setError(true);
            wrapper.setMessage(s);
        }
    }

    private void setWrapperHint(TextFieldUnify wrapper, String s) {
        wrapper.setError(false);
        wrapper.setMessage(s);
    }

    public void resetError() {
        setWrapperErrorNew(wrapperName, null);
        setWrapperErrorNew(wrapperEmail, null);
        showPasswordHint();
        showNameHint();
    }

    public void showPasswordHint() {
        wrapperPassword.setError(false);
        wrapperPassword.setMessage(getResources().getString(R.string.minimal_8_character));
    }

    public void showNameHint() {
        setWrapperHint(wrapperName, "  ");
    }

    public void setActionsEnabled(boolean isEnabled) {

        wrapperEmail.getTextFieldInput().setEnabled(isEnabled);
        wrapperName.getTextFieldInput().setEnabled(isEnabled);
        wrapperPassword.getTextFieldInput().setEnabled(isEnabled);
        registerButton.setEnabled(isEnabled);
    }

    public void showLoadingProgress() {
        setActionsEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissLoadingProgress() {
        setActionsEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    public void goToAutomaticLogin() {
        Intent intentLogin = LoginActivity.DeepLinkIntents.getAutomaticLogin(
                getActivity(),
                wrapperEmail.getTextFieldInput().getText().toString(),
                wrapperPassword.getTextFieldInput().getText().toString(),
                source
        );
        startActivityForResult(intentLogin, REQUEST_AUTO_LOGIN);
    }

    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    public void onErrorRegister(String errorMessage) {
        dismissLoadingProgress();
        onFailedRegisterEmail(errorMessage);
        setActionsEnabled(true);
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    public void onSuccessRegister() {
        if (getActivity() != null) {
            dismissLoadingProgress();
            setActionsEnabled(true);
            lostViewFocus();
            registerAnalytics.trackSuccessClickSignUpButtonEmail();
        }
    }

    public void lostViewFocus() {
        wrapperEmail.getTextFieldInput().clearFocus();
        wrapperName.getTextFieldInput().clearFocus();
        wrapperPassword.getTextFieldInput().clearFocus();
        registerButton.clearFocus();
    }

    private boolean isEmailAddressFromDevice() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        boolean result = false;
        if (list.size() > 0) {
            for (String e : list) {
                if (e.equals(wrapperEmail.getTextFieldInput().getText().toString())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public void showInfo() {
        dismissLoadingProgress();
        Typography view = redirectView.findViewById(R.id.body);
        final String emailString = wrapperEmail.getTextFieldInput().getText().toString();
        String text = getString(R.string.account_registered_body, emailString);
        String part = getString(R.string.account_registered_body_part);
        Spannable spannable = getSpannable(text, part);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), text.indexOf(emailString)
                , text.indexOf(emailString) + emailString.length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannable, TextView.BufferType.SPANNABLE);
        view.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.FORGOT_PASSWORD);
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, emailString);
                startActivity(intent);
            }
        });
        redirectView.setVisibility(View.VISIBLE);
    }

    public void onForbidden() {
        ForbiddenActivity.startActivity(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME, wrapperName.getTextFieldInput().getText().toString());
        outState.putString(EMAIL, wrapperEmail.getTextFieldInput().getText().toString());
        outState.putString(PASSWORD, wrapperPassword.getTextFieldInput().getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_AUTO_LOGIN:
                if (getActivity() != null && resultCode == Activity.RESULT_OK) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else {
                    dismissLoadingProgress();
                }
                break;

            case REQUEST_ACTIVATE_ACCOUNT:
                if (resultCode == Activity.RESULT_OK && getActivity() != null) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else {
                    dismissLoadingProgress();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public int getIsAutoVerify() {
        return isEmailAddressFromDevice() ? 1 : 0;
    }

    private void onFailedRegisterEmail(String errorMessage){
        registerAnalytics.trackFailedClickEmailSignUpButton(errorMessage);
        registerAnalytics.trackFailedClickSignUpButtonEmail(errorMessage);
    }

    public void onBackPressed() {
        registerAnalytics.trackClickOnBackButtonRegisterEmail();
    }
}
