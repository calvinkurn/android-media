package com.tokopedia.loginregister.login.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.design.text.TextDrawable;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.loginregister.LoginRegisterPhoneRouter;
import com.tokopedia.loginregister.LoginRegisterRouter;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.activation.view.activity.ActivationActivity;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.common.view.LoginTextView;
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel;
import com.tokopedia.loginregister.login.di.DaggerLoginComponent;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.login.view.listener.LoginContract;
import com.tokopedia.loginregister.login.view.presenter.LoginPresenter;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;
import com.tokopedia.loginregister.loginthirdparty.google.SmartLockActivity;
import com.tokopedia.loginregister.loginthirdparty.webview.WebViewLoginFragment;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.sessioncommon.data.Token;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.SecurityPojo;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author by nisie on 12/18/17.
 */

public class LoginFragment extends BaseDaggerFragment implements LoginContract.View {

    private static final String COLOR_WHITE = "#FFFFFF";
    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String PHONE_NUMBER = "tokocash";

    private static final int REQUEST_SMART_LOCK = 101;
    private static final int REQUEST_SAVE_SMART_LOCK = 102;
    private static final int REQUEST_LOGIN_WEBVIEW = 103;
    private static final int REQUEST_SECURITY_QUESTION = 104;
    private static final int REQUEST_LOGIN_PHONE_NUMBER = 105;
    private static final int REQUESTS_CREATE_PASSWORD = 106;
    private static final int REQUEST_ACTIVATE_ACCOUNT = 107;
    private static final int REQUEST_VERIFY_PHONE = 108;
    private static final int REQUEST_ADD_NAME = 109;
    private static final int REQUEST_LOGIN_GOOGLE = 110;

    public static final String IS_AUTO_LOGIN = "auto_login";
    public static final String AUTO_LOGIN_METHOD = "method";

    public static final String AUTO_LOGIN_EMAIL = "email";
    public static final String AUTO_LOGIN_PASS = "pw";

    public static final String IS_AUTO_FILL = "auto_fill";
    public static final String AUTO_FILL_EMAIL = "email";
    public static final String IS_FROM_REGISTER = "is_from_register";

    private static final int ID_ACTION_REGISTER = 111;

    AutoCompleteTextView emailEditText;
    TextInputEditText passwordEditText;
    ScrollView loginView;
    View loadingView;
    View rootView;
    TextView forgotPass;
    LinearLayout loginLayout;
    LinearLayout loginButtonsContainer;
    TextView loginButton;
    TkpdHintTextInputLayout wrapperEmail;
    TkpdHintTextInputLayout wrapperPassword;
    ImageView loadMoreFab;

    ArrayAdapter<String> autoCompleteAdapter;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;

    @Inject
    LoginPresenter presenter;

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    UserSessionInterface userSession;

    @Inject
    LoginRegisterAnalytics analytics;

    //*For analytics
    private String actionLoginMethod;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return LoginRegisterAnalytics.SCREEN_LOGIN;
    }

    @Override
    protected void initInjector() {
        DaggerLoginComponent daggerLoginComponent = (DaggerLoginComponent) DaggerLoginComponent
                .builder().loginRegisterComponent(getComponent(LoginRegisterComponent.class))
                .build();

        daggerLoginComponent.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.trackScreen(getActivity(), getScreenName());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (userSession != null
                && userSession.isLoggedIn()
                && getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, ID_ACTION_REGISTER, 0, "");
        MenuItem menuItem = menu.findItem(ID_ACTION_REGISTER);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (getDraw() != null) {
            menuItem.setIcon(getDraw());
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Drawable getDraw() {
        TextDrawable drawable = null;
        if (getActivity() != null) {
            drawable = new TextDrawable(getActivity());
            drawable.setText(getResources().getString(R.string.register));
            drawable.setTextColor(getResources().getColor(R.color.tkpd_main_green));
        }
        return drawable;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == ID_ACTION_REGISTER) {
            goToRegisterInitial();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            callbackManager = CallbackManager.Factory.create();
            GoogleSignInOptions gso =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(Token.GOOGLE_API_KEY)
                            .requestEmail()
                            .requestProfile()
                            .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_login, parent, false);
        rootView = view.findViewById(R.id.root);
        emailEditText = view.findViewById(R.id.email_auto);
        passwordEditText = view.findViewById(R.id.password);
        loginView = view.findViewById(R.id.login_form);
        loadingView = view.findViewById(R.id.login_status);
        forgotPass = view.findViewById(R.id.forgot_pass);
        loginLayout = view.findViewById(R.id.login_layout);
        loginButton = view.findViewById(R.id.accounts_sign_in);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        loadMoreFab = view.findViewById(R.id.btn_load_more);
        loginButtonsContainer = view.findViewById(R.id.login_buttons_container);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {

        passwordEditText.setOnEditorActionListener(
                (textView, id, keyEvent) -> {
                    if (id == R.id.ime_login || id == EditorInfo.IME_NULL) {
                        actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_EMAIL;
                        presenter.login(emailEditText.getText().toString().trim(),
                                passwordEditText.getText().toString());
                        return true;
                    }

                    return false;
                });

        passwordEditText.addTextChangedListener(watcher(wrapperPassword));


        loginButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_EMAIL;
                KeyboardHandler.hideSoftKeyboard(getActivity());
                presenter.saveLoginEmail(emailEditText.getText().toString());
                presenter.login(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString());
                analytics.eventClickLoginButton(getActivity().getApplicationContext());
            }
        });

        emailEditText.addTextChangedListener(watcher(wrapperEmail));

        forgotPass.setOnClickListener(v -> goToForgotPassword());

        loginView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (isLastItem()) {
                loadMoreFab.setVisibility(View.GONE);
            } else {
                loadMoreFab.setVisibility(View.VISIBLE);
            }

        });

        loadMoreFab.setOnClickListener(v -> loginView.post(() -> loginView.fullScroll(View.FOCUS_DOWN)));

    }

    private void goToForgotPassword() {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof LoginRegisterRouter) {
            Intent intent = ((LoginRegisterRouter) getActivity().getApplicationContext())
                    .getForgotPasswordIntent(getActivity(), emailEditText.getText().toString()
                            .trim());
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            analytics.eventClickForgotPasswordFromLogin(getActivity().getApplicationContext());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            autoCompleteAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line,
                    presenter.getLoginIdList());
            emailEditText.setAdapter(autoCompleteAdapter);

            presenter.discoverLogin();

            if (getArguments() != null && getArguments().getBoolean(IS_AUTO_FILL, false)) {
                emailEditText.setText(getArguments().getString(AUTO_FILL_EMAIL, ""));
            } else if (getArguments().getBoolean(IS_AUTO_LOGIN, false)) {
                switch (getArguments().getInt(AUTO_LOGIN_METHOD)) {
                    case LoginActivity.METHOD_FACEBOOK:
                        onLoginFacebookClick();
                        break;
                    case LoginActivity.METHOD_GOOGLE:
                        onLoginGoogleClick();
                        break;
                    case LoginActivity.METHOD_WEBVIEW:
                        if (!TextUtils.isEmpty(getArguments().getString(LoginActivity
                                .AUTO_WEBVIEW_NAME, ""))
                                && !TextUtils.isEmpty(getArguments().getString(LoginActivity
                                .AUTO_WEBVIEW_URL, ""))) {
                            onLoginWebviewClick(getArguments().getString(LoginActivity.AUTO_WEBVIEW_NAME,
                                    ""),
                                    getArguments().getString(LoginActivity.AUTO_WEBVIEW_URL,
                                            ""));
                        }
                        break;
                    case LoginActivity.METHOD_EMAIL:
                        actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_EMAIL;
                        String email = getArguments().getString(AUTO_LOGIN_EMAIL, "");
                        String pw = getArguments().getString(AUTO_LOGIN_PASS, "");
                        emailEditText.setText(email);
                        passwordEditText.setText(pw);
                        presenter.login(email, pw);
                        break;
                    default:
                        showSmartLock();
                        break;
                }
            } else {
                showSmartLock();
            }
        }
    }

    private void showSmartLock() {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, SmartLockActivity.RC_READ);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_SMART_LOCK);
    }

    private void goToRegisterInitial() {
        if (getActivity() != null) {
            analytics.eventClickRegisterFromLogin();
            Intent intent = RegisterInitialActivity.getCallingIntent(getActivity());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            getActivity().finish();
        }
    }


    @Override
    public void resetError() {
        setWrapperError(wrapperEmail, null);
        setWrapperError(wrapperPassword, null);
    }

    @Override
    public void showLoadingLogin() {
        showLoading(true);
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

        loginView.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (loginView != null) {
                            loginView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                        }
                    }
                });

    }

    @Override
    public void dismissLoadingLogin() {
        showLoading(false);
    }

    @Override
    public void showErrorPassword(int resId) {
        setWrapperError(wrapperPassword, getString(resId));
        passwordEditText.requestFocus();
        analytics.eventLoginErrorPassword();
    }

    @Override
    public void showErrorEmail(int resId) {
        setWrapperError(wrapperEmail, getString(resId));
        emailEditText.requestFocus();
        analytics.eventLoginErrorEmail();

    }

    private void saveSmartLock(int state, String email, String password) {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, state);
        if (state == SmartLockActivity.RC_SAVE_SECURITY_QUESTION || state == SmartLockActivity.RC_SAVE) {
            bundle.putString(SmartLockActivity.USERNAME, email);
            bundle.putString(SmartLockActivity.PASSWORD, password);
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_SAVE_SMART_LOCK);
    }

    @Override
    public void onSuccessLogin() {
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();

            analytics.eventSuccessLogin(actionLoginMethod);
            ((LoginRegisterRouter) getActivity().getApplicationContext()).setTrackingUserId
                    (userSession.getUserId(), getActivity().getApplicationContext());
            ((LoginRegisterRouter) getActivity().getApplicationContext()).onLoginSuccess();
        }
    }

    @Override
    public void onErrorLogin(String errorMessage) {
        if (!TextUtils.isEmpty(actionLoginMethod)) {
            analytics.eventFailedLogin(actionLoginMethod);
        }

        dismissLoadingLogin();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void setAutoCompleteAdapter(ArrayList<String> listId) {
        autoCompleteAdapter.clear();
        autoCompleteAdapter.addAll(listId);
        autoCompleteAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingDiscover() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = loginLayout.getChildCount() - 1;
        if (loginLayout != null && !(loginLayout.getChildAt(lastPos) instanceof ProgressBar)) {
            loginLayout.addView(pb, loginLayout.getChildCount());
        }
    }

    @Override
    public void dismissLoadingDiscover() {
        int lastPos = loginLayout.getChildCount() - 1;
        if (loginLayout != null && loginLayout.getChildAt(lastPos) instanceof ProgressBar) {
            loginLayout.removeViewAt(loginLayout.getChildCount() - 1);
        }
    }

    @Override
    public void onErrorDiscoverLogin(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage,
                () -> presenter.discoverLogin()).showRetrySnackbar();
    }

    @Override
    public void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> listProvider) {
        if (!GlobalConfig.isSellerApp()) listProvider.add(2, getLoginPhoneNumberBean());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 15);
        loginButtonsContainer.removeAllViews();
        for (int i = 0; i < listProvider.size(); i++) {
            int colorInt = Color.parseColor(COLOR_WHITE);
            LoginTextView tv = new LoginTextView(getActivity(), colorInt);
            tv.setTag(listProvider.get(i).getId());
            tv.setText(listProvider.get(i).getName());
            if (!TextUtils.isEmpty(listProvider.get(i).getImage())) {
                tv.setImage(listProvider.get(i).getImage());
            } else if (listProvider.get(i).getImageResource() != 0) {
                tv.setImageResource(listProvider.get(i).getImageResource());
            }
            tv.setRoundCorner(10);

            setDiscoverListener(listProvider.get(i), tv);
            if (loginButtonsContainer != null) {
                loginButtonsContainer.addView(tv, loginButtonsContainer.getChildCount(), layoutParams);
            }
        }

        enableArrow();
    }

    @Override
    public GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener() {
        return new GetFacebookCredentialSubscriber.GetFacebookCredentialListener() {

            @Override
            public void onErrorGetFacebookCredential(Exception e) {
                if (isAdded() && getActivity() != null) {
                    NetworkErrorHelper.showSnackbar(getActivity(), ErrorHandler.getErrorMessage
                            (getContext(), e));
                }
            }

            @Override
            public void onSuccessGetFacebookCredential(AccessToken accessToken, String email) {
                presenter.loginFacebook(accessToken, email);
            }
        };
    }

    @Override
    public void onGoToAddName() {
        if (getActivity() != null) {
            Intent intent = ((ApplinkRouter) getActivity().getApplicationContext()).getApplinkIntent(getActivity
                    (), ApplinkConst.ADD_NAME_PROFILE);
            startActivityForResult(intent, REQUEST_ADD_NAME);
        }
    }

    @Override
    public void disableArrow() {
        loadMoreFab.setVisibility(View.GONE);
    }

    @Override
    public void enableArrow() {
        ViewTreeObserver observer = loginView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            int viewHeight = loginView.getMeasuredHeight();
            int contentHeight = loginView.getChildAt(0).getHeight();
            if (viewHeight - contentHeight < 0
                    && !isLastItem()) {
                loadMoreFab.setVisibility(View.VISIBLE);
            } else {
                loadMoreFab.setVisibility(View.GONE);
            }
        });
    }

    private boolean isLastItem() {
        return loginView.getChildAt(0).getBottom() <= (loginView.getHeight() + loginView
                .getScrollY());
    }

    private void setDiscoverListener(final DiscoverItemViewModel discoverItemViewModel,
                                     LoginTextView tv) {
        if (discoverItemViewModel.getId().equalsIgnoreCase(FACEBOOK)) {
            tv.setOnClickListener(v -> onLoginFacebookClick());
        } else if (discoverItemViewModel.getId().equalsIgnoreCase(GPLUS)) {
            tv.setOnClickListener(v -> onLoginGoogleClick());
        } else if (discoverItemViewModel.getId().equalsIgnoreCase(PHONE_NUMBER)) {
            tv.setOnClickListener(v -> onLoginPhoneNumberClick());
        } else {
            tv.setOnClickListener(v -> onLoginWebviewClick(discoverItemViewModel.getName(),
                    discoverItemViewModel.getUrl()));
        }
    }

    private void onLoginWebviewClick(String name, String url) {
        actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_WEBVIEW + name;

        analytics.eventClickLoginWebview(name);

        if (getFragmentManager() != null && getActivity() != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            WebViewLoginFragment newFragment = WebViewLoginFragment
                    .createInstance(url, name);
            newFragment.setTargetFragment(this, REQUEST_LOGIN_WEBVIEW);
            newFragment.show(fragmentTransaction, "dialog");

            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        }
    }

    private void onLoginPhoneNumberClick() {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_PHONE;

            analytics.eventClickLoginPhoneNumber(getActivity().getApplicationContext());

            Intent intent = ((LoginRegisterPhoneRouter) getActivity().getApplicationContext())
                    .getCheckLoginPhoneNumberIntent(getActivity());
            startActivityForResult(intent, REQUEST_LOGIN_PHONE_NUMBER);
        }

    }

    private void onLoginGoogleClick() {
        if (getActivity() != null) {
            actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_GOOGLE;

            analytics.eventClickLoginGoogle(getActivity().getApplicationContext());

            Intent signinIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signinIntent, REQUEST_LOGIN_GOOGLE);
        }

    }

    private void onLoginFacebookClick() {
        if (getActivity() != null) {
            actionLoginMethod = LoginRegisterAnalytics.ACTION_LOGIN_FACEBOOK;

            analytics.eventClickLoginFacebook(getActivity().getApplicationContext());
            presenter.getFacebookCredential(this, callbackManager);
        }
    }

    private DiscoverItemViewModel getLoginPhoneNumberBean() {
        DiscoverItemViewModel phoneNumberBean = new DiscoverItemViewModel(
                PHONE_NUMBER,
                getString(R.string.phone_number),
                "",
                "",
                COLOR_WHITE
        );
        phoneNumberBean.setImageResource(R.drawable.ic_phone);
        return phoneNumberBean;
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
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getActivity() != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
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
            } else if (requestCode == REQUEST_LOGIN_GOOGLE && data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleGoogleSignInResult(task);
            } else if (requestCode == REQUEST_LOGIN_WEBVIEW && resultCode == Activity.RESULT_OK) {
                presenter.loginWebview(data);
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
                onSuccessLogin();
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin();
                getActivity().setResult(Activity.RESULT_CANCELED);
            } else if (requestCode == REQUEST_LOGIN_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {
                onSuccessLogin();
            } else if (requestCode == REQUEST_LOGIN_PHONE_NUMBER && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin();
                getActivity().setResult(Activity.RESULT_CANCELED);
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
                onSuccessLogin();
            } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin();
                getActivity().setResult(Activity.RESULT_CANCELED);
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_OK) {
                onSuccessLogin();
            } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_CANCELED) {
                dismissLoadingLogin();
                getActivity().setResult(Activity.RESULT_CANCELED);
            } else if (requestCode == REQUEST_VERIFY_PHONE) {
                onSuccessLogin();
            } else if (requestCode == REQUEST_ADD_NAME && resultCode == Activity.RESULT_OK) {
                onSuccessLogin();
            } else if (requestCode == REQUEST_ADD_NAME && resultCode == Activity.RESULT_CANCELED) {
                userSession.logoutSession();
                dismissLoadingLogin();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * Please refer to the
     * {@link com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes class reference for
     * status code
     */
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String accessToken = account.getIdToken();
            String email = account.getEmail();
            presenter.loginGoogle(accessToken, email);
        } catch (ApiException e) {
            onErrorLoginSosmed(LoginRegisterAnalytics.GOOGLE,
                    String.format(getString(R.string.failed_login_google),
                            String.valueOf(e.getStatusCode()))
            );
        }
    }

    @Override
    public LoginSuccessRouter getLoginRouter() {
        return new LoginSuccessRouter() {

            @Override
            public void onGoToCreatePasswordPage(GetUserInfoData info) {
                if (getActivity() != null) {
                    Intent intent = ((ApplinkRouter) getActivity().getApplicationContext()).getApplinkIntent(getActivity
                            (), ApplinkConst.CREATE_PASSWORD);
                    intent.putExtra("name", info.getFullName());
                    intent.putExtra("user_id", String.valueOf(info.getUserId()));
                    startActivityForResult(intent, REQUESTS_CREATE_PASSWORD);
                }
            }

            @Override
            public void onGoToPhoneVerification() {
                if (getActivity() != null) {
                    Intent intent = ((ApplinkRouter) getActivity().getApplicationContext())
                            .getApplinkIntent(getActivity(), ApplinkConst.PHONE_VERIFICATION);
                    startActivityForResult(intent, REQUEST_VERIFY_PHONE);
                }
            }

            @Override
            public void onGoToSecurityQuestion(SecurityPojo securityDomain, String fullName,
                                               String email, String phone) {
                Intent intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                        getActivity(), RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION, phone, email);
                startActivityForResult(intent, REQUEST_SECURITY_QUESTION);

            }

            @Override
            public void onForbidden() {
                dismissLoadingLogin();
                ForbiddenActivity.startActivity(getActivity());
            }

            @Override
            public void onGoToActivationPage(String email) {
                Intent intent = ActivationActivity.getCallingIntent(getActivity(),
                        email, passwordEditText.getText().toString());
                startActivityForResult(intent, REQUEST_ACTIVATE_ACCOUNT);
            }

            @Override
            public void onErrorLogin(String errorMessage) {
                LoginFragment.this.onErrorLogin(errorMessage);
            }

            @Override
            public void logUnknownError(Throwable message) {
                try {
                    Crashlytics.logException(message);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onSuccessLoginEmail() {
        dismissLoadingLogin();
        analytics.eventSuccessLoginEmail();
        if (getActivity() != null) {
            ((LoginRegisterRouter) getActivity().getApplicationContext()).setMoEUserAttributesLogin
                    (userSession.getUserId(),
                            userSession.getName(),
                            userSession.getEmail(),
                            userSession.getPhoneNumber(),
                            userSession.isGoldMerchant(),
                            userSession.getShopName(),
                            userSession.getShopId(),
                            userSession.hasShop(),
                            LoginRegisterAnalytics.LABEL_EMAIL
                    );
        }

        onSuccessLogin();
    }

    @Override
    public void setSmartLock() {
        saveSmartLock(SmartLockActivity.RC_SAVE_SECURITY_QUESTION,
                emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void onErrorLoginSosmed(String loginMethodName, String errorMessage) {
        onErrorLogin(errorMessage);
    }

    @Override
    public void onSuccessLoginSosmed(String loginMethod) {
        dismissLoadingLogin();

        analytics.eventSuccessLoginSosmed(loginMethod);
        if (getActivity() != null) {
            ((LoginRegisterRouter) getActivity().getApplicationContext()).setMoEUserAttributesLogin
                    (userSession.getUserId(),
                            userSession.getName(),
                            userSession.getEmail(),
                            userSession.getPhoneNumber(),
                            userSession.isGoldMerchant(),
                            userSession.getShopName(),
                            userSession.getShopId(),
                            userSession.hasShop(),
                            LoginRegisterAnalytics.LABEL_EMAIL
                    );
        }
        onSuccessLogin();
    }

    @Override
    public boolean isFromRegister() {
        return getActivity() != null
                && getActivity().getIntent() != null
                && getActivity().getIntent().getBooleanExtra(IS_FROM_REGISTER, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void stopTrace() {
        //Not implemented here
    }
}
