package com.tokopedia.loginregister.registerinitial.view.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.view.LoginTextView;
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;
import com.tokopedia.loginregister.registerinitial.view.customview.PartialRegisterInputView;
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterContract;
import com.tokopedia.loginregister.registerinitial.view.presenter.RegisterInitialPresenter;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 10/24/18.
 */
public class RegisterInitialFragment extends BaseDaggerFragment
        implements RegisterContract.View,
        PartialRegisterInputView.PartialRegisterInputViewListener {

    private static final int REQUEST_REGISTER_WEBVIEW = 100;
    private static final int REQUEST_REGISTER_EMAIL = 101;
    private static final int REQUEST_CREATE_PASSWORD = 102;
    private static final int REQUEST_SECURITY_QUESTION = 103;
    private static final int REQUEST_REGISTER_PHONE_NUMBER = 104;
    private static final int REQUEST_VERIFY_PHONE = 105;
    private static final int REQUEST_WELCOME_PAGE = 106;
    private static final int REQUEST_ADD_NAME_REGISTER_PHONE = 107;
    private static final int REQUEST_VERIFY_PHONE_TOKOCASH = 108;
    private static final int REQUEST_CHOOSE_ACCOUNT = 109;
    private static final int REQUEST_NO_TOKOCASH_ACCOUNT = 110;
    private static final int REQUEST_ADD_NAME = 111;

    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String PHONE_NUMBER = "phonenumber";

    private TextView optionTitle;
    private PartialRegisterInputView partialRegisterInputView;
    private LinearLayout registerContainer, llLayout;
    private LoginTextView registerButton, registerPhoneNumberButton;
    private TextView loginButton;
    private ScrollView container;
    private RelativeLayout progressBar;

    private String socmedMethod = "";
    private String email = "";
    private String phoneNumber = "";

    @Inject
    RegisterInitialPresenter presenter;

    @Inject
    UserSessionInterface userSession;

    @Inject
    LoginRegisterAnalytics analytics;

    CallbackManager callbackManager;


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

    }

    @Override
    protected String getScreenName() {
        return LoginRegisterAnalytics.SCREEN_REGISTER;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        if (savedInstanceState != null && savedInstanceState.containsKey(PHONE_NUMBER)) {
            phoneNumber = savedInstanceState.getString(PHONE_NUMBER);
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
        llLayout = view.findViewById(R.id.ll_layout);
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

    private void initData() {
        presenter.getProvider();
        partialRegisterInputView.setListener(this);
    }

    protected void prepareView() {
        if(getActivity()!= null) {
//        UserAuthenticationAnalytics.setActiveRegister();
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
//                UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterEmail());
//                UnifyTracking.eventMoRegistrationStart(AppEventTracking.GTMCacheValue.EMAIL);
//                goToRegisterEmailPage();

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
//                showProgressBar();
//                Intent intent = RegisterPhoneNumberActivity.getCallingIntent(getActivity());
//                startActivityForResult(intent, REQUEST_REGISTER_PHONE_NUMBER);
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
                goToLoginPage();
            }
        });
    }

    @Override
    public void onRegisterClick(String id) {
//        presenter.validateRegister(id);
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

    @Override
    public void showLoadingDiscover() {

    }

    @Override
    public void onErrorDiscoverRegister(String errorMessage) {

    }

    @Override
    public void onSuccessDiscoverRegister(ArrayList<DiscoverItemViewModel> discoverViewModel) {

    }

    @Override
    public void dismissLoadingDiscover() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void dismissProgressBar() {

    }

    @Override
    public void onErrorRegisterSosmed(String errorMessage) {

    }

    @Override
    public GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener() {
        return null;
    }

    @Override
    public void onForbidden() {

    }

    @Override
    public void showRegisteredEmailDialog(String email) {

    }

    @Override
    public void showRegisteredPhoneDialog(String phone) {

    }

    @Override
    public void showProceedWithPhoneDialog(String phone) {

    }

    @Override
    public void goToLoginPage() {

    }

    @Override
    public void goToRegisterEmailPageWithEmail(String email) {

    }

    @Override
    public void onErrorValidateRegister(String message) {

    }

    @Override
    public void setTempPhoneNumber(String maskedPhoneNumber) {

    }
}
