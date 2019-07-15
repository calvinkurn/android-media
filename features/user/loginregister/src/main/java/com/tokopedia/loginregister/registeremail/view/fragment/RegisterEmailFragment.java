package com.tokopedia.loginregister.registeremail.view.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.utils.StripedUnderlineUtil;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.activation.view.activity.ActivationActivity;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.registeremail.di.DaggerRegisterEmailComponent;
import com.tokopedia.loginregister.registeremail.domain.pojo.RegisterEmailPojo;
import com.tokopedia.loginregister.registeremail.view.activity.RegisterEmailActivity;
import com.tokopedia.loginregister.registeremail.view.listener.RegisterEmailContract;
import com.tokopedia.loginregister.registeremail.view.presenter.RegisterEmailPresenter;
import com.tokopedia.loginregister.registeremail.view.util.RegisterUtil;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity;
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
public class RegisterEmailFragment extends BaseDaggerFragment
        implements RegisterEmailContract.View {

    private static final int REQUEST_AUTO_LOGIN = 101;
    private static final int REQUEST_ACTIVATE_ACCOUNT = 102;

    int PASSWORD_MINIMUM_LENGTH = 6;

    String NAME = "NAME";
    String PASSWORD = "PASSWORD";
    String EMAIL = "EMAIL";

    String TERM_CONDITION = "Syarat dan Ketentuan";
    String PRIVACY_POLICY = "Kebijakan Privasi";

    String TERM_CONDITION_URL = "launch.TermPrivacy://parent?param=0";
    String PRIVACY_POLICY_URL = "launch.TermPrivacy://parent?param=1";

    View container;
    View redirectView;
    AutoCompleteTextView email;
    TextInputEditText registerPassword;
    TextView registerButton;
    TkpdHintTextInputLayout wrapperName;
    TkpdHintTextInputLayout wrapperEmail;
    TkpdHintTextInputLayout wrapperPassword;
    EditText name;
    TextView registerNextTAndC;
    ProgressBar progressBar;

    @Inject
    RegisterEmailPresenter presenter;

    @Inject
    LoginRegisterAnalytics analytics;

    @Inject
    RegisterAnalytics registerAnalytics;

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    UserSessionInterface userSession;

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
        DaggerRegisterEmailComponent daggerLoginComponent =
                (DaggerRegisterEmailComponent) DaggerRegisterEmailComponent
                        .builder().loginRegisterComponent(getComponent(LoginRegisterComponent.class))
                        .build();

        daggerLoginComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return LoginRegisterAnalytics.Companion.getSCREEN_REGISTER_EMAIL();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_with_email, parent, false);
        container = view.findViewById(R.id.container);
        redirectView = view.findViewById(R.id.redirect_reset_password);
        email = view.findViewById(R.id.register_email);
        registerPassword = view.findViewById(R.id.register_password);
        registerButton = view.findViewById(R.id.register_button);
        wrapperName = view.findViewById(R.id.wrapper_name);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        name = view.findViewById(R.id.name);
        progressBar = view.findViewById(R.id.progress_bar);
        registerNextTAndC = view.findViewById(R.id.register_next_detail_t_and_p);
        presenter.attachView(this);
        prepareView();
        setViewListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) { }


    private void prepareView() {
        if (getArguments() != null) {
            email.setText(getArguments().getString(RegisterEmailActivity.EXTRA_PARAM_EMAIL, ""));
        }

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
                textPaint.setColor(ContextCompat.getColor(registerNextTAndC.getContext(), R.color.green_nob));
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
                textPaint.setColor(ContextCompat.getColor(registerNextTAndC.getContext(), R.color.green_nob));
            }
        };

        spannable.setSpan(clickableSpanTermCondition, sourceString.indexOf(TERM_CONDITION),
                sourceString.indexOf(TERM_CONDITION) + TERM_CONDITION.length(), 0);

        spannable.setSpan(clickableSpanPrivacyPolicy, sourceString.indexOf(PRIVACY_POLICY),
                sourceString.indexOf(PRIVACY_POLICY) + PRIVACY_POLICY.length(), 0);

        registerNextTAndC.setText(spannable, TextView.BufferType.SPANNABLE);
        registerNextTAndC.setMovementMethod(LinkMovementMethod.getInstance());

        showPasswordHint();
        showEmailHint();
        showNameHint();
    }


    private Spannable getSpannable(String sourceString, String hyperlinkString) {
        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(R.color.tkpd_main_green));
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
            name.setText(savedInstanceState.getString(NAME, ""));
            email.setText(savedInstanceState.getString(EMAIL, ""));
            registerPassword.setText(savedInstanceState.getString(PASSWORD, ""));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        email.addTextChangedListener(emailWatcher(wrapperEmail));
        registerPassword.addTextChangedListener(passwordWatcher(wrapperPassword));
        name.addTextChangedListener(nameWatcher(wrapperName));

        if (getActivity() != null && userSession.isLoggedIn()) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }


    private TextWatcher nameWatcher(final TkpdHintTextInputLayout wrapper) {
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
                showNameHint();
                if (s.length() == 0) {
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                } else if (s.length() < 3) {
                    setWrapperError(wrapper, getString(R.string.error_minimal_name));
                } else if (RegisterUtil.checkRegexNameLocal(name.getText().toString())) {
                    setWrapperError(wrapper, getString(R.string.error_illegal_character));
                } else if (RegisterUtil.isExceedMaxCharacter(name.getText().toString())) {
                    setWrapperError(wrapper, getString(R.string.error_max_35_character));
                }

                checkIsValidForm();
            }
        };
    }

    private TextWatcher passwordWatcher(final TkpdHintTextInputLayout wrapper) {
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
                showPasswordHint();
                if (s.length() == 0) {
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                } else if (registerPassword.getText().toString().length() < PASSWORD_MINIMUM_LENGTH) {
                    setWrapperError(wrapper, getString(R.string.error_minimal_password));
                }

                checkIsValidForm();
            }
        };
    }

    private TextWatcher emailWatcher(final TkpdHintTextInputLayout wrapper) {
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
                showEmailHint();
                if (s.length() == 0) {
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    setWrapperError(wrapper, getString(R.string.wrong_email_format));
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
        email.setOnTouchListener((v, event) -> {
            if (!email.isPopupShowing()) {
                email.showDropDown();
            }
            return false;
        });

        registerPassword.setOnEditorActionListener((v, id, event) -> {
            if (id == REGISTER_BUTTON_IME || id == EditorInfo.IME_NULL) {
                registerEmail();
                return true;
            }
            return false;
        });

        registerButton.setOnClickListener(v -> registerEmail());
    }

    private void registerEmail() {
        registerAnalytics.trackClickSignUpButtonEmail();
        presenter.onRegisterClicked(
                email.getText().toString(),
                name.getText().toString(),
                registerPassword.getText().toString(),
                registerPassword.getText().toString(),
                getIsAutoVerify()
        );
    }

    private void checkIsValidForm() {
        if (presenter.isCanRegister(name.getText().toString(), email.getText().toString(), registerPassword.getText().toString())) {
            setRegisterButtonEnabled();
        } else {
            setRegisterButtonDisabled();
        }
    }

    private void setRegisterButtonEnabled() {
        if (getActivity() != null) {
            MethodChecker.setBackground(registerButton, MethodChecker.getDrawable(getActivity().getApplicationContext(), R
                    .drawable
                    .green_button_rounded));
            registerButton.setTextColor(MethodChecker.getColor(getActivity().getApplicationContext(),
                    R.color.white));
            registerButton.setEnabled(true);
        }
    }


    private void setRegisterButtonDisabled() {
        if (getActivity() != null) {
            MethodChecker.setBackground(registerButton, MethodChecker.getDrawable(
                    getActivity().getApplicationContext(), R.drawable.grey_button_rounded));
            registerButton.setTextColor(MethodChecker.getColor(
                    getActivity().getApplicationContext(),
                    R.color.grey_500));
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

    private void setWrapperHint(TkpdHintTextInputLayout wrapper, String s) {
        wrapper.setErrorEnabled(false);
        wrapper.setHelperEnabled(true);
        wrapper.setHelper(s);
    }

    @Override
    public void resetError() {
        setWrapperError(wrapperName, null);
        setWrapperError(wrapperEmail, null);
        setWrapperError(wrapperPassword, null);
        showPasswordHint();
        showEmailHint();
        showNameHint();
    }

    public void showPasswordHint() {
        setWrapperHint(wrapperPassword, getResources().getString(R.string.minimal_6_character));
    }

    public void showNameHint() {
        setWrapperHint(wrapperName, "  ");
    }

    public void showEmailHint() {
        setWrapperHint(wrapperEmail, getResources().getString(R.string.send_verif_to_email));
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {

        email.setEnabled(isEnabled);
        name.setEnabled(isEnabled);
        registerPassword.setEnabled(isEnabled);
        registerButton.setEnabled(isEnabled);
    }

    @Override
    public void showLoadingProgress() {
        setActionsEnabled(false);
        container.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingProgress() {
        setActionsEnabled(true);
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }


    @Override
    public void goToActivationPage(RegisterEmailPojo viewModel, String email, String password) {
        if (getActivity() != null) {
            Intent intent = ActivationActivity.getCallingIntent(getActivity(),
                    email,
                    password
            );
            startActivityForResult(intent, REQUEST_ACTIVATE_ACCOUNT);
        }
    }

    @Override
    public void goToAutomaticLogin() {
        Intent intentLogin = LoginActivity.DeepLinkIntents.getAutomaticLogin(
                getActivity(),
                email.getText().toString(),
                registerPassword.getText().toString()
        );
        startActivityForResult(intentLogin, REQUEST_AUTO_LOGIN);
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onErrorRegister(String errorMessage) {
        dismissLoadingProgress();
        onFailedRegisterEmail(errorMessage);
        setActionsEnabled(true);
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRegister(RegisterEmailPojo pojo, String name, String email) {
        if (getActivity() != null) {
            dismissLoadingProgress();
            setActionsEnabled(true);
            lostViewFocus();
            registerAnalytics.trackSuccessClickSignUpButtonEmail();
            registerAnalytics.trackSuccessClickEmailSignUpButton();
            analytics.eventSuccessRegisterEmail(getActivity().getApplicationContext(), pojo.getuId(), name, email);
        }
    }

    public void lostViewFocus() {
        email.clearFocus();
        name.clearFocus();
        registerPassword.clearFocus();
        registerButton.clearFocus();
    }

    private boolean isEmailAddressFromDevice() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        boolean result = false;
        if (list.size() > 0) {
            for (String e : list) {
                if (e.equals(email.getText().toString())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public void showInfo() {
        dismissLoadingProgress();
        TextView view = redirectView.findViewById(R.id.body);
        final String emailString = email.getText().toString();
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
        container.setVisibility(View.GONE);
    }

    @Override
    public void onForbidden() {
        ForbiddenActivity.startActivity(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME, name.getText().toString());
        outState.putString(EMAIL, email.getText().toString());
        outState.putString(PASSWORD, registerPassword.getText().toString());
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

    @Override
    public void onBackPressed() {
        registerAnalytics.trackClickOnBackButtonRegisterEmail();
    }
}
