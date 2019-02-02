package com.tokopedia.loginphone.checkloginphone.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.checkloginphone.di.DaggerCheckLoginPhoneComponent;
import com.tokopedia.loginphone.checkloginphone.view.activity.CheckLoginPhoneNumberActivity;
import com.tokopedia.loginphone.checkloginphone.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.loginphone.checkloginphone.view.listener.CheckLoginPhoneNumberContract;
import com.tokopedia.loginphone.checkloginphone.view.presenter.CheckLoginPhoneNumberPresenter;
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel;
import com.tokopedia.loginphone.choosetokocashaccount.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.loginphone.verifyotptokocash.view.activity.TokoCashOtpActivity;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.TokoCashVerificationFragment;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity;

import javax.inject.Inject;

/**
 * @author by nisie on 11/23/17.
 */

public class CheckLoginPhoneNumberFragment extends BaseDaggerFragment
        implements CheckLoginPhoneNumberContract.View {

    private static final int REQUEST_VERIFY_PHONE = 101;
    private static final int REQUEST_CHOOSE_ACCOUNT = 102;

    private EditText phoneNumber;
    private TextView nextButton;
    private TextView errorText;
    private TextView changeInactiveNumber;
    private View mainView;
    private ProgressBar progressBar;

    @Inject
    CheckLoginPhoneNumberPresenter presenter;

    @Inject
    LoginPhoneNumberAnalytics analytics;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new CheckLoginPhoneNumberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_LOGIN_PHONE_NUMBER;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {

            BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication())
                    .getBaseAppComponent();

            LoginRegisterPhoneComponent loginRegisterPhoneComponent =
                    DaggerLoginRegisterPhoneComponent.builder()
                            .baseAppComponent(appComponent).build();

            DaggerCheckLoginPhoneComponent daggerCheckPhoneComponent = (DaggerCheckLoginPhoneComponent)
                    DaggerCheckLoginPhoneComponent.builder()
                            .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
                            .build();

            daggerCheckPhoneComponent.inject(this);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_login_phone_number, parent, false);
        phoneNumber = view.findViewById(R.id.phone_number);
        nextButton = view.findViewById(R.id.next_btn);
        errorText = view.findViewById(R.id.error);
        changeInactiveNumber = view.findViewById(R.id.change_inactive);
        mainView = view.findViewById(R.id.main_view);
        progressBar = view.findViewById(R.id.progress_bar);
        presenter.attachView(this);
        prepareView();
        return view;
    }

    private void prepareView() {
        phoneNumber.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                analytics.trackLoginWithPhone();
                presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
                handled = true;
            }
            return handled;
        });

        nextButton.setOnClickListener(v -> {
            errorText.setText("");
            analytics.trackLoginWithPhone();
            presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
        });

        if (getArguments() != null) {
            if (getArguments().get(CheckLoginPhoneNumberActivity.PARAM_PHONE_NUMBER) != null) {
                String phoneNumberString = getArguments().getString(CheckLoginPhoneNumberActivity.PARAM_PHONE_NUMBER);
                phoneNumber.setText(phoneNumberString);
                nextButton.performClick();
            }
        }

        SpannableString changeInactiveString = new SpannableString(changeInactiveNumber.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (getActivity() != null) {
                    Intent intent = ((ApplinkRouter) getActivity().getApplicationContext()).getApplinkIntent(getActivity
                            (), ApplinkConst.CHANGE_INACTIVE_PHONE);
                    startActivity(intent);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                ds.setUnderlineText(false);
            }
        };

        changeInactiveString.setSpan(clickableSpan, 37, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        changeInactiveNumber.setText(changeInactiveString);
        changeInactiveNumber.setMovementMethod(LinkMovementMethod.getInstance());
        changeInactiveNumber.setHighlightColor(Color.TRANSPARENT);
    }


    @Override
    public void showErrorPhoneNumber(int resId) {
        showErrorPhoneNumber(getString(resId));
    }

    @Override
    public void goToVerifyAccountPage(String phoneNumber) {
        startActivityForResult(TokoCashOtpActivity.getCallingIntent(
                getActivity(),
                phoneNumber,
                true,
                RequestOtpUseCase.MODE_SMS
        ), REQUEST_VERIFY_PHONE);
    }

    @Override
    public void goToNoTokocashAccountPage() {
        startActivity(NotConnectedTokocashActivity.getNoTokocashAccountIntent(
                getActivity(),
                phoneNumber.getText().toString()));
    }

    @Override
    public void dismissLoading() {
        progressBar.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }

    @Override
    public void onForbidden() {
        ForbiddenActivity.startActivity(getActivity());
    }

    @Override
    public void showErrorPhoneNumber(String errorMessage) {
        errorText.setText(errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getActivity() != null) {
            if (requestCode == REQUEST_VERIFY_PHONE
                    && resultCode == Activity.RESULT_OK
                    && data != null
                    && data.getParcelableExtra(ChooseTokoCashAccountViewModel.ARGS_DATA) != null) {
                ChooseTokoCashAccountViewModel chooseTokoCashAccountViewModel =
                        data.getParcelableExtra(ChooseTokoCashAccountViewModel.ARGS_DATA);
                if (!chooseTokoCashAccountViewModel.getListAccount().isEmpty()) {
                    goToChooseAccountPage(chooseTokoCashAccountViewModel);
                } else {
                    goToNoTokocashAccountPage();
                }
            } else if (requestCode == REQUEST_VERIFY_PHONE
                    && resultCode == TokoCashVerificationFragment.RESULT_SUCCESS_AUTO_LOGIN) {
                onSuccessLoginPhoneNumber();
            } else if (requestCode == REQUEST_CHOOSE_ACCOUNT
                    && resultCode == Activity.RESULT_OK) {
                onSuccessLoginPhoneNumber();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void onSuccessLoginPhoneNumber() {
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    private void goToChooseAccountPage(ChooseTokoCashAccountViewModel data) {
        startActivityForResult(ChooseTokocashAccountActivity.getCallingIntent(
                getActivity(),
                data),
                REQUEST_CHOOSE_ACCOUNT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
