package com.tokopedia.loginregister.activation.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.widget.PinEditText;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.activation.di.DaggerActivationComponent;
import com.tokopedia.loginregister.activation.view.activity.ActivationActivity;
import com.tokopedia.loginregister.activation.view.activity.ChangeEmailActivity;
import com.tokopedia.loginregister.activation.view.listener.ActivationContract;
import com.tokopedia.loginregister.activation.view.presenter.ActivationPresenter;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.data.LoginRegisterUrl;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by nisie on 1/31/17.
 */

public class ActivationFragment extends BaseDaggerFragment
        implements ActivationContract.View {

    private static final int REQUEST_AUTO_LOGIN = 101;
    private TextView activationText;
    private PinEditText verifyCode;
    private TextView activateButton;
    private TextView footer;
    private TextView errorOtp;
    private ImageView errorImage;
    private ImageView warningImage;
    private ProgressBar progressBar;
    private View mainView;

    private String email = "";
    private String password = "";
    private String source = "";

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    UserSessionInterface userSession;

    @Inject
    LoginRegisterAnalytics analytics;

    @Inject
    RegisterAnalytics registerAnalytics;

    @Inject
    ActivationPresenter presenter;

    public static ActivationFragment createInstance(Bundle args) {
        ActivationFragment fragment = new ActivationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initInjector() {
        DaggerActivationComponent daggetActivationComponent = (DaggerActivationComponent)
                DaggerActivationComponent
                .builder().loginRegisterComponent(getComponent(LoginRegisterComponent.class))
                .build();

        daggetActivationComponent.inject(this);

    }

    @Override
    protected String getScreenName() {
        return LoginRegisterAnalytics.Companion.getSCREEN_ACCOUNT_ACTIVATION();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            email = savedInstanceState.getString(ActivationActivity.INTENT_EXTRA_PARAM_EMAIL, "");
        } else if (getArguments() != null &&
                getArguments().getString(ActivationActivity.INTENT_EXTRA_PARAM_EMAIL) != null) {
            email = getArguments().getString(ActivationActivity.INTENT_EXTRA_PARAM_EMAIL, "");
        }

        if (savedInstanceState != null) {
            password = savedInstanceState.getString(ActivationActivity.INTENT_EXTRA_PARAM_PW, "");
        } else if (getArguments()!= null && getArguments().getString(ActivationActivity.INTENT_EXTRA_PARAM_PW) != null) {
            password = getArguments().getString(ActivationActivity.INTENT_EXTRA_PARAM_PW, "");
        }

        if (savedInstanceState != null) {
            source = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "");
        } else if (getArguments()!= null && getArguments().getString(ApplinkConstInternalGlobal.PARAM_SOURCE) != null) {
            source = getArguments().getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "");
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null
                && userSession != null
                && userSession.isLoggedIn()) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activation, container, false);
        activationText = view.findViewById(R.id.activation_text);
        verifyCode = view.findViewById(R.id.input_verify_code);
        activateButton = view.findViewById(R.id.verify_button);
        footer = view.findViewById(R.id.footer);
        errorImage = view.findViewById(R.id.error_image);
        warningImage = view.findViewById(R.id.register_icon);
        errorOtp = view.findViewById(R.id.error_otp);
        progressBar = view.findViewById(R.id.progress_bar);
        mainView = view.findViewById(R.id.main_view);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        activateButton.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        ImageHandler.LoadImage(warningImage, LoginRegisterUrl.PATH_IMAGE_ACTIVATION);
        setActivateText(email);

        Spannable spannable = new SpannableString(getString(R.string.activation_resend_email_2));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(MethodChecker.getColor(getActivity(),
                                          R.color.tkpd_main_green));
                              }
                          }
                , getString(R.string.activation_resend_email_2).indexOf("Kirim")
                , getString(
                        R.string.activation_resend_email_2).length()
                , 0);

        footer.setText(spannable, TextView.BufferType.SPANNABLE);
    }


    private void setActivateText(String email) {
        if (getActivity() != null) {
            activationText.setText(getString(R.string.activation_header_text_2));
            activationText.append(" ");
            activationText.append(getColoredString(email, getActivity().getResources().getColor(R.color
                    .black_70)));
            verifyCode.requestFocus();
            KeyboardHandler.DropKeyboard(getActivity(), verifyCode);
        }
    }

    public final Spannable getColoredString(CharSequence text, int color) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activateButton.setOnClickListener(v -> {
            registerAnalytics.trackClickActivationButton();
            presenter.activateAccount(email, verifyCode.getText().toString());
        });
        footer.setOnClickListener(v -> {
            registerAnalytics.trackClickResendButton();
            showChangeEmailDialog(email);
        });

        verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 5) {
                    activateButton.setEnabled(true);
                    MethodChecker.setBackground(activateButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.green_button_rounded));
                    activateButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
                } else {
                    activateButton.setEnabled(false);
                    MethodChecker.setBackground(activateButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.grey_button_rounded));
                    activateButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_12));
                }
            }
        });

        verifyCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                presenter.activateAccount(email, verifyCode.getText().toString());
                return true;
            }
            return false;
        });
        errorImage.setOnClickListener(v -> {
            verifyCode.setText("");
            removeErrorOtp();
        });
    }

    private void removeErrorOtp() {
        verifyCode.setError(false);
        errorOtp.setVisibility(View.INVISIBLE);
        errorImage.setVisibility(View.GONE);
    }

    private void showChangeEmailDialog(String email) {
        if (getActivity() != null) {
            String dialogMessage =
                    getString(R.string.message_resend_email_to) + " <b>" + email + "</b>";
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.resend_activation_email)
                    .setMessage(MethodChecker.fromHtml(dialogMessage))
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        registerAnalytics.trackClickOkResendButton();
                        presenter.resendActivation(
                                email
                        );
                    })
                    .setNegativeButton(R.string.button_change_email, (dialog, which) -> {
                        registerAnalytics.trackClickChangeEmail();
                        registerAnalytics.trackFailedClickResendButton(
                                getActivity().getString(R.string.change_email_error_condition));
                        goToChangeEmail(email);
                    })
                    .show();
        }
    }

    private void goToChangeEmail(String email) {
        startActivityForResult(
                ChangeEmailActivity.getCallingIntent(
                        getActivity(),
                        email),
                ChangeEmailFragment.ACTION_CHANGE_EMAIL);
    }

    @Override
    public void showLoadingProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }


    @Override
    public void onErrorResendActivation(String errorMessage) {
        finishLoadingProgress();
        registerAnalytics.trackFailedClickOkResendButton(errorMessage);
        registerAnalytics.trackFailedClickResendButton(errorMessage);
        if (errorMessage.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        }
    }

    @Override
    public void onSuccessResendActivation() {
        if (getActivity() != null) {
            registerAnalytics.trackSuccessClickOkResendButton();
            registerAnalytics.trackSuccessClickResendButton();
            KeyboardHandler.DropKeyboard(getActivity(), verifyCode);
            removeErrorOtp();
            finishLoadingProgress();
            ToasterNormal.show(getActivity(), getString(R.string.success_resend_activation));
        }
    }

    @Override
    public void onErrorActivateWithUnicode(String errorMessage) {
        if (getActivity() != null) {
            registerAnalytics.trackFailedClickActivationButton(errorMessage);
            verifyCode.setError(true);
            KeyboardHandler.DropKeyboard(getActivity(), verifyCode);
            finishLoadingProgress();
            if (errorMessage.equals("")) {
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
            }

            errorImage.setVisibility(View.VISIBLE);
            errorOtp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessActivateWithUnicode(TokenViewModel pojo) {
        registerAnalytics.trackSuccessClickActivationButton();
        Intent autoLoginIntent = LoginActivity.DeepLinkIntents.getAutomaticLogin(
                getActivity(),
                email,
                password,
                source);
        startActivityForResult(
                autoLoginIntent,
                REQUEST_AUTO_LOGIN
        );
    }

    @Override
    public void finishLoadingProgress() {
        progressBar.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangeEmailFragment.ACTION_CHANGE_EMAIL
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getExtras() != null) {
            email = data.getExtras().getString(ChangeEmailFragment.EXTRA_EMAIL, "");
            setActivateText(email);
        } else if (requestCode == REQUEST_AUTO_LOGIN
                && resultCode == Activity.RESULT_OK
                && getActivity() != null) {
            finishLoadingProgress();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
            RouteManager.route(getContext(), ApplinkConst.DISCOVERY_NEW_USER);
        } else if (requestCode == REQUEST_AUTO_LOGIN) {
            finishLoadingProgress();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ActivationActivity.INTENT_EXTRA_PARAM_EMAIL, email);
        outState.putString(ActivationActivity.INTENT_EXTRA_PARAM_PW, password);
        outState.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
