package com.tokopedia.loginregister.registerinitial.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.text.TextDrawable;
import com.tokopedia.loginregister.LoginRegisterPhoneRouter;
import com.tokopedia.loginregister.LoginRegisterRouter;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.common.view.LoginTextView;
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;
import com.tokopedia.loginregister.loginthirdparty.webview.WebViewLoginFragment;
import com.tokopedia.loginregister.registeremail.view.activity.RegisterEmailActivity;
import com.tokopedia.loginregister.registerinitial.di.DaggerRegisterInitialComponent;
import com.tokopedia.loginregister.registerinitial.view.customview.PartialRegisterInputView;
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialContract;
import com.tokopedia.loginregister.registerinitial.view.presenter.RegisterInitialPresenter;
import com.tokopedia.loginregister.welcomepage.WelcomePageActivity;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.SecurityPojo;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import static com.tokopedia.sessioncommon.data.Token.GOOGLE_API_KEY;

/**
 * @author by nisie on 10/24/18.
 */
public class RegisterInitialFragment extends BaseDaggerFragment
        implements RegisterInitialContract.View,
        PartialRegisterInputView.PartialRegisterInputViewListener {

    private static final int ID_ACTION_LOGIN = 112;

    private static final int REQUEST_REGISTER_WEBVIEW = 100;
    private static final int REQUEST_REGISTER_EMAIL = 101;
    private static final int REQUEST_CREATE_PASSWORD = 102;
    private static final int REQUEST_SECURITY_QUESTION = 103;
    private static final int REQUEST_REGISTER_PHONE_NUMBER = 104;
    private static final int REQUEST_VERIFY_PHONE_REGISTER_PHONE = 105;
    private static final int REQUEST_WELCOME_PAGE = 106;
    private static final int REQUEST_ADD_NAME_REGISTER_PHONE = 107;
    private static final int REQUEST_VERIFY_PHONE_TOKOCASH = 108;
    private static final int REQUEST_CHOOSE_ACCOUNT = 109;
    private static final int REQUEST_NO_TOKOCASH_ACCOUNT = 110;
    private static final int REQUEST_ADD_NAME = 111;
    private static final int REQUEST_LOGIN_GOOGLE = 112;

    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String PHONE_NUMBER = "phonenumber";

    private TextView optionTitle;
    private PartialRegisterInputView partialRegisterInputView;
    private LinearLayout registerContainer;
    private LoginTextView registerButton, registerPhoneNumberButton;
    private TextView loginButton;
    private ScrollView container;
    private RelativeLayout progressBar;

    private String phoneNumber = "";

    @Inject
    RegisterInitialPresenter presenter;

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    UserSessionInterface userSession;

    @Inject
    LoginRegisterAnalytics analytics;

    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;

    public static RegisterInitialFragment createInstance() {
        return new RegisterInitialFragment();
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
                        .builder().loginRegisterComponent(getComponent(LoginRegisterComponent.class))
                        .build();

        daggerLoginComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return LoginRegisterAnalytics.SCREEN_REGISTER_INITIAL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        if (getActivity() != null) {
            GoogleSignInOptions gso =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(GOOGLE_API_KEY)
                            .requestEmail()
                            .requestProfile()
                            .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(PHONE_NUMBER)) {
            phoneNumber = savedInstanceState.getString(PHONE_NUMBER);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (userSession.isLoggedIn() && getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_initial_register, parent, false);
        optionTitle = view.findViewById(R.id.register_option_title);
        partialRegisterInputView = view.findViewById(R.id
                .register_input_view);
        registerContainer = view.findViewById(R.id.register_container);
        registerButton = view.findViewById(R.id.register);
        registerPhoneNumberButton = view.findViewById(R.id.register_phone_number);
        loginButton = view.findViewById(R.id.login_button);
        container = view.findViewById(R.id.container);
        progressBar = view.findViewById(R.id.progress_bar);
        prepareView();
        setViewListener();
        presenter.attachView(this);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, ID_ACTION_LOGIN, 0, "");
        MenuItem menuItem = menu.findItem(ID_ACTION_LOGIN);
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
            drawable.setText(getResources().getString(R.string.login));
            drawable.setTextColor(getResources().getColor(R.color.tkpd_main_green));
            drawable.setTextSize(14);
        }
        return drawable;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == ID_ACTION_LOGIN) {
            analytics.eventClickOnLoginFromRegister();
            goToLoginPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        presenter.getProvider();
        partialRegisterInputView.setListener(this);
    }

    protected void prepareView() {
        if (getActivity() != null) {
            registerButton.setVisibility(View.GONE);
            registerPhoneNumberButton.setVisibility(View.GONE);
            partialRegisterInputView.setVisibility(View.GONE);

            if (!GlobalConfig.isSellerApp()) {
                optionTitle.setText(R.string.register_option_title);
                optionTitle.setTypeface(Typeface.DEFAULT);
                optionTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }

            registerButton.setColor(Color.WHITE);
            registerButton.setBorderColor(MethodChecker.getColor(getActivity(), R.color.black_38));
            registerButton.setRoundCorner(10);
            registerButton.setImageResource(R.drawable.ic_email);
            registerButton.setOnClickListener(v -> {
                analytics.eventClickRegisterEmail();
                TrackApp.getInstance().getMoEngage().sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_EMAIL);
                goToRegisterEmailPage();

            });

            if (GlobalConfig.isSellerApp()) {
                registerButton.setVisibility(View.VISIBLE);
            } else {
                partialRegisterInputView.setVisibility(View.VISIBLE);
            }
            registerPhoneNumberButton.setColor(Color.WHITE);
            registerPhoneNumberButton.setBorderColor(MethodChecker.getColor(getActivity(), R.color
                    .black_38));
            registerPhoneNumberButton.setRoundCorner(10);
            registerPhoneNumberButton.setImageResource(R.drawable.ic_phone);
            registerPhoneNumberButton.setOnClickListener(v -> {
                showProgressBar();

                if (getActivity() != null && getActivity().getApplicationContext() != null) {
                    Intent intent = ((LoginRegisterPhoneRouter) getActivity().getApplicationContext())
                            .getCheckRegisterPhoneNumberIntent(getActivity());
                    startActivityForResult(intent, REQUEST_REGISTER_PHONE_NUMBER);
                }
            });
            String sourceString = getActivity().getResources().getString(R.string
                    .span_already_have_tokopedia_account);

            Spannable spannable = new SpannableString(sourceString);

            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View view) {

                                  }

                                  @Override
                                  public void updateDrawState(TextPaint ds) {
                                      ds.setColor(MethodChecker.getColor(
                                              getActivity(), R.color.tkpd_main_green
                                              )
                                      );
                                      ds.setTypeface(Typeface.create("sans-serif-medium", Typeface
                                              .NORMAL));
                                  }
                              }
                    , sourceString.indexOf("Masuk")
                    , sourceString.length()
                    , 0);

            loginButton.setText(spannable, TextView.BufferType.SPANNABLE);
        }
    }

    protected void setViewListener() {
        loginButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
                analytics.eventClickOnLoginFromRegister();
                goToLoginPage();
            }
        });
    }

    @Override
    public void goToLoginPage() {
        Intent intent = LoginActivity.getCallingIntent(getActivity());
        startActivity(intent);
    }

    private void goToRegisterEmailPage() {
        showProgressBar();
        Intent intent = RegisterEmailActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_REGISTER_EMAIL);
    }

    @Override
    public void goToRegisterEmailPageWithEmail(String email) {
        showProgressBar();
        Intent intent = RegisterEmailActivity.getCallingIntentWithEmail(getActivity(), email);
        startActivityForResult(intent, REQUEST_REGISTER_EMAIL);
    }

    private void goToVerificationPhoneRegister(String phone) {
        Intent intent = VerificationActivity.getCallingIntent(
                getActivity(),
                phone,
                RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER,
                true,
                RequestOtpUseCase.MODE_SMS
        );
        startActivityForResult(intent, REQUEST_VERIFY_PHONE_REGISTER_PHONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (getActivity() != null) {
            if (requestCode == REQUEST_REGISTER_WEBVIEW) {
                handleRegisterWebview(resultCode, data);
            } else if (requestCode == REQUEST_LOGIN_GOOGLE && data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                if (task != null) {
                    handleGoogleSignInResult(task);
                }
            } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else if (requestCode == REQUEST_REGISTER_PHONE_NUMBER && resultCode == Activity
                    .RESULT_OK) {
                goToAddName();
            } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity
                    .RESULT_CANCELED) {
                dismissProgressBar();
                getActivity().setResult(Activity.RESULT_CANCELED);
                userSession.clearToken();
            } else if (requestCode == REQUEST_REGISTER_PHONE_NUMBER && resultCode == Activity
                    .RESULT_CANCELED) {
                dismissProgressBar();
                getActivity().setResult(Activity.RESULT_CANCELED);
            } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity
                    .RESULT_CANCELED) {
                dismissProgressBar();
                getActivity().setResult(Activity.RESULT_CANCELED);
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity
                    .RESULT_CANCELED) {
                dismissProgressBar();
                getActivity().setResult(Activity.RESULT_CANCELED);
            } else if (requestCode == REQUEST_VERIFY_PHONE_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                goToAddName();
            } else if (requestCode == REQUEST_ADD_NAME_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
                startActivityForResult(WelcomePageActivity.newInstance(getActivity()),
                        REQUEST_WELCOME_PAGE);
            } else if (requestCode == REQUEST_WELCOME_PAGE) {
                if (resultCode == Activity.RESULT_OK) {
                    goToProfileCompletionPage();
                } else {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            } else if (requestCode == REQUEST_VERIFY_PHONE_TOKOCASH && resultCode == Activity
                    .RESULT_OK) {
                ChooseTokoCashAccountViewModel chooseTokoCashAccountViewModel = getChooseAccountData
                        (data);
                if (chooseTokoCashAccountViewModel != null && !chooseTokoCashAccountViewModel
                        .getListAccount().isEmpty()) {
                    goToChooseAccountPage(chooseTokoCashAccountViewModel);
                } else {
                    goToNoTokocashAccountPage(phoneNumber);
                }
            } else if (requestCode == REQUEST_CHOOSE_ACCOUNT
                    && resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else if (requestCode == REQUEST_ADD_NAME && resultCode == Activity.RESULT_OK) {
                startActivityForResult(WelcomePageActivity.newInstance(getActivity()),
                        REQUEST_WELCOME_PAGE);
            } else if (requestCode == REQUEST_ADD_NAME && resultCode == Activity.RESULT_CANCELED) {
                userSession.logoutSession();
                dismissProgressBar();
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
        if (getContext() != null) {
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String accessToken = account.getIdToken();
                String email = account.getEmail();
                presenter.registerGoogle(accessToken, email);
            } catch (NullPointerException e) {
                onErrorRegisterSosmed(LoginRegisterAnalytics.GOOGLE,
                        ErrorHandlerSession.getDefaultErrorCodeMessage(
                                ErrorHandlerSession.ErrorCode.GOOGLE_FAILED_ACCESS_TOKEN,
                                getContext()));
            } catch (ApiException e) {
                onErrorRegisterSosmed(LoginRegisterAnalytics.GOOGLE,
                        String.format(getString(R.string.loginregister_failed_login_google),
                                String.valueOf(e.getStatusCode())));
            }
        }
    }


    private void goToAddName() {
        if (getActivity() != null) {
            String applink = ApplinkConst.ADD_NAME_REGISTER;
            applink = applink.replace("{phone}", phoneNumber);
            Intent intent = ((ApplinkRouter) getActivity().getApplicationContext()).getApplinkIntent(getActivity
                    (), applink);
            startActivityForResult(intent, REQUEST_ADD_NAME_REGISTER_PHONE);
        }
    }

    private void handleRegisterWebview(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            KeyboardHandler.DropKeyboard(getActivity(), getView());
        } else {
            presenter.registerWebview(data);
        }
    }

    private void goToProfileCompletionPage() {
        if (getActivity() != null) {
            ((ApplinkRouter) getActivity().getApplicationContext()).goToApplinkActivity(getActivity
                    (), ApplinkConst.PROFILE_COMPLETION);
        }
    }

    @Override
    public void onActionPartialClick(String id) {
        presenter.validateRegister(id);
    }

    @Override
    public void showLoadingDiscover() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = registerContainer.getChildCount() - 1;
        if (!(registerContainer.getChildAt(lastPos) instanceof ProgressBar)) {
            registerContainer.addView(pb, registerContainer.getChildCount());
        }
    }

    @Override
    public void onErrorDiscoverRegister(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                errorMessage, () -> presenter.getProvider()).showRetrySnackbar();
        loginButton.setEnabled(false);
    }

    @Override
    public void onSuccessDiscoverRegister(ArrayList<DiscoverItemViewModel> listProvider) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.dp_52));
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());

        layoutParams.setMargins(0, topMargin, 0, 0);

        for (int i = 0; i < listProvider.size(); i++) {
            DiscoverItemViewModel item = listProvider.get(i);
            if (!item.getId().equals(PHONE_NUMBER)) {
                LoginTextView loginTextView = new LoginTextView(getActivity()
                        , MethodChecker.getColor(getActivity(), R.color.white));
                loginTextView.setText(item.getName());
                loginTextView.setBorderColor(MethodChecker.getColor(getActivity(), R.color
                        .black_38));
                loginTextView.setImage(item.getImage());
                loginTextView.setRoundCorner(10);

                setDiscoverOnClickListener(item, loginTextView);

                if (registerContainer != null) {
                    registerContainer.addView(loginTextView, registerContainer.getChildCount(),
                            layoutParams);
                }
            }
        }

    }

    private void setDiscoverOnClickListener(final DiscoverItemViewModel discoverItemViewModel,
                                            LoginTextView loginTextView) {

        switch (discoverItemViewModel.getId().toLowerCase()) {
            case FACEBOOK:
                loginTextView.setOnClickListener(v -> onRegisterFacebookClick());
                break;
            case GPLUS:
                loginTextView.setOnClickListener(v -> onRegisterGooglelick());
                break;
            default:
                loginTextView.setOnClickListener(v -> onRegisterWebviewClick(discoverItemViewModel));
                break;
        }
    }

    private void onRegisterFacebookClick() {
        if (getActivity() != null) {
            analytics.eventClickRegisterFacebook(getActivity().getApplicationContext());
            TrackApp.getInstance().getMoEngage().sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_FACEBOOK);
            presenter.getFacebookCredential(this, callbackManager);
        }

    }

    private void onRegisterGooglelick() {
        if (getActivity() != null) {
            analytics.eventClickRegisterGoogle(getActivity().getApplicationContext());
            TrackApp.getInstance().getMoEngage().sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_GMAIL);
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, REQUEST_LOGIN_GOOGLE);
        }

    }

    private void onRegisterWebviewClick(DiscoverItemViewModel discoverItemViewModel) {
        if (getActivity() != null) {
            if (getFragmentManager() != null) {
                analytics.eventClickRegisterWebview(getActivity().getApplicationContext(), discoverItemViewModel.getName());
                TrackApp.getInstance().getMoEngage().sendRegistrationStartEvent(LoginRegisterAnalytics.LABEL_WEBVIEW);
                WebViewLoginFragment webViewLoginFragment = WebViewLoginFragment.createInstance(
                        discoverItemViewModel.getUrl(), discoverItemViewModel.getName());
                webViewLoginFragment.setTargetFragment(RegisterInitialFragment.this, REQUEST_REGISTER_WEBVIEW);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                webViewLoginFragment.show(fragmentTransaction, WebViewLoginFragment.class.getSimpleName());
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        }
    }


    @Override
    public void dismissLoadingDiscover() {
        int lastPos = registerContainer.getChildCount() - 1;
        if (registerContainer.getChildAt(lastPos) instanceof ProgressBar) {
            registerContainer.removeViewAt(registerContainer.getChildCount() - 1);
        }
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (container != null) {
            container.setVisibility(View.GONE);
        }
        if (loginButton != null) {
            loginButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismissProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        }
        if (loginButton != null) {
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorRegisterSosmed(String methodName, String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRegisterSosmed(String methodName) {
        analytics.eventSuccessRegisterSosmed(methodName);
        startActivityForResult(WelcomePageActivity.newInstance(getActivity()),
                REQUEST_WELCOME_PAGE);
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
    public LoginSuccessRouter getLoginRouter() {
        return new LoginSuccessRouter() {
            @Override
            public void onGoToActivationPage(String email) {

            }

            @Override
            public void onForbidden() {
                ForbiddenActivity.startActivity(getActivity());
            }

            @Override
            public void onErrorLogin(String errorMessage) {
                NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
            }

            @Override
            public void onGoToCreatePasswordPage(GetUserInfoData info) {
                if (getActivity() != null) {
                    Intent intent = ((ApplinkRouter) getActivity().getApplicationContext()).getApplinkIntent(getActivity
                            (), ApplinkConst.CREATE_PASSWORD);
                    intent.putExtra("name", info.getFullName());
                    intent.putExtra("user_id", String.valueOf(info.getUserId()));
                    startActivityForResult(intent, REQUEST_CREATE_PASSWORD);
                }
            }

            @Override
            public void onGoToPhoneVerification() {
                if (getActivity() != null) {
                    ((ApplinkRouter) getActivity().getApplicationContext())
                            .goToApplinkActivity(getActivity(), ApplinkConst.PHONE_VERIFICATION);
                }
            }

            @Override
            public void onGoToSecurityQuestion(SecurityPojo securityPojo, String fullName, String email, String phone) {
                Intent intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                        getActivity(), RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION, phone, email);
                startActivityForResult(intent, REQUEST_SECURITY_QUESTION);
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
    public GetFacebookCredentialSubscriber.GetFacebookCredentialListener
    getFacebookCredentialListener() {
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
                presenter.registerFacebook(accessToken, email);
            }
        };
    }


    @Override
    public void showRegisteredEmailDialog(String email) {
        if (getActivity() != null) {
            final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
            dialog.setTitle(getString(R.string.email_already_registered));
            dialog.setDesc(
                    String.format(getResources().getString(
                            R.string.email_already_registered_info), email));
            dialog.setBtnOk(getString(R.string.already_registered_yes));
            dialog.setOnOkClickListener(v -> {
                analytics.eventProceedEmailAlreadyRegistered();
                dialog.dismiss();
                startActivity(LoginActivity.getIntentLoginFromRegister(getActivity(), email));
                getActivity().finish();
            });
            dialog.setBtnCancel(getString(R.string.already_registered_no));
            dialog.setOnCancelClickListener(v -> {
                analytics.eventCancelEmailAlreadyRegistered();
                dialog.dismiss();
            });
            dialog.show();
        }
    }

    @Override
    public void showRegisteredPhoneDialog(String phone) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.phone_number_already_registered));
        dialog.setDesc(
                String.format(getResources().getString(
                        R.string.reigster_page_phone_number_already_registered_info), phone));
        dialog.setBtnOk(getString(R.string.already_registered_yes));
        dialog.setOnOkClickListener(v -> {
            dialog.dismiss();
            phoneNumber = phone;
            goToVerifyAccountPage(phoneNumber);
        });
        dialog.setBtnCancel(getString(R.string.already_registered_no));
        dialog.setOnCancelClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void goToVerifyAccountPage(String phoneNumber) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {

            Intent intent = ((LoginRegisterPhoneRouter) getActivity().getApplicationContext())
                    .getTokoCashOtpIntent(getActivity(),
                            phoneNumber,
                            true,
                            RequestOtpUseCase.MODE_SMS);

            startActivityForResult(intent, REQUEST_VERIFY_PHONE_TOKOCASH);
        }
    }


    private void goToNoTokocashAccountPage(String phoneNumber) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            Intent intent = ((LoginRegisterPhoneRouter) getActivity().getApplicationContext())
                    .getNoTokocashAccountIntent(
                            getActivity(),
                            phoneNumber);
            startActivityForResult(intent, REQUEST_NO_TOKOCASH_ACCOUNT);
        }
    }

    private void goToChooseAccountPage(ChooseTokoCashAccountViewModel data) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {

            Intent intent = ((LoginRegisterPhoneRouter) getActivity().getApplicationContext())
                    .getChooseTokocashAccountIntent(getActivity(), data);

            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT);
        }
    }

    private ChooseTokoCashAccountViewModel getChooseAccountData(Intent data) {
        return data.getParcelableExtra(ChooseTokoCashAccountViewModel.ARGS_DATA);
    }


    @Override
    public void showProceedWithPhoneDialog(String phone) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(phone);
        dialog.setDesc(getResources().getString(R.string.phone_number_not_registered_info));
        dialog.setBtnOk(getString(R.string.proceed_with_phone_number));
        dialog.setOnOkClickListener(v -> {
            analytics.eventProceedRegisterWithPhoneNumber();
            dialog.dismiss();
            goToVerificationPhoneRegister(phone);
        });
        dialog.setBtnCancel(getString(R.string.already_registered_no));
        dialog.setOnCancelClickListener(v -> {
            analytics.eventCancelRegisterWithPhoneNumber();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onErrorValidateRegister(String message) {
        partialRegisterInputView.onErrorValidate(message);
        phoneNumber = "";
    }

    @Override
    public void setTempPhoneNumber(String maskedPhoneNumber) {
        //use masked phone number form backend when needed
        //we need unmasked phone number (without dash) to be provided to backend
        this.phoneNumber = partialRegisterInputView.getTextValue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(PHONE_NUMBER, phoneNumber);
        super.onSaveInstanceState(outState);
    }
}
