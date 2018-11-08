package com.tokopedia.loginphone.checkregisterphone.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.utils.StripedUnderlineUtil;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.checkloginphone.view.activity.CheckLoginPhoneNumberActivity;
import com.tokopedia.loginphone.checkregisterphone.di.DaggerCheckRegisterPhoneComponent;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.checkregisterphone.view.listener.CheckRegisterPhoneNumber;
import com.tokopedia.loginphone.checkregisterphone.view.presenter.CheckRegisterPhoneNumberPresenter;
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;

import javax.inject.Inject;

/**
 * @author by yfsx on 26/2/18.
 */

public class CheckRegisterPhoneNumberFragment extends BaseDaggerFragment
        implements CheckRegisterPhoneNumber.View {

    private static final int REQUEST_VERIFY_PHONE = 101;

    private EditText phoneNumber;
    private TextView nextButton;
    private TextView message;
    private TextView bottomInfo;
    private TkpdHintTextInputLayout phoneNumberLayout;
    private View mainView;
    private ProgressBar progressBar;

    @Inject
    CheckRegisterPhoneNumberPresenter presenter;

    @Inject
    LoginPhoneNumberAnalytics analytics;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new CheckRegisterPhoneNumberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_REGISTER_WITH_PHONE_NUMBER;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {

            BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication())
                    .getBaseAppComponent();

            LoginRegisterPhoneComponent loginRegisterPhoneComponent =
                    DaggerLoginRegisterPhoneComponent.builder()
                            .baseAppComponent(appComponent).build();

            DaggerCheckRegisterPhoneComponent daggerCheckPhoneComponent = (DaggerCheckRegisterPhoneComponent)
                    DaggerCheckRegisterPhoneComponent.builder()
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
        View view = inflater.inflate(R.layout.fragment_register_phone, parent, false);
        phoneNumber = view.findViewById(R.id.phone_number);
        message = view.findViewById(R.id.message);
        nextButton = view.findViewById(R.id.next_btn);
        bottomInfo = view.findViewById(R.id.botton_info);
        phoneNumberLayout = view.findViewById(R.id.wrapper_name);
        mainView = view.findViewById(R.id.main_view);
        progressBar = view.findViewById(R.id.progress_bar);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        phoneNumber.requestFocus();
        phoneNumber.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                message.setVisibility(View.VISIBLE);
                presenter.checkPhoneNumber(phoneNumber.getText().toString());
                handled = true;
                phoneNumberLayout.setErrorEnabled(false);
            }
            return handled;
        });

        nextButton.setOnClickListener(view -> {
            phoneNumberLayout.setErrorEnabled(false);
            message.setVisibility(View.VISIBLE);
            presenter.checkPhoneNumber(phoneNumber.getText().toString());
        });

        bottomInfo.setText(MethodChecker.fromHtml(getString(R.string.bottom_info_terms_and_privacy)));
        bottomInfo.setMovementMethod(LinkMovementMethod.getInstance());
        nextButton.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        StripedUnderlineUtil.stripUnderlines(bottomInfo);
    }

    @Override
    public void showErrorPhoneNumber(int resId) {
        showErrorPhoneNumber(getString(resId));
    }

    @Override
    public void goToVerifyAccountPage(String phoneNumber) {

        Intent intent = VerificationActivity.getCallingIntent(
                getActivity(),
                phoneNumber,
                RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER,
                true,
                RequestOtpUseCase.MODE_SMS
        );
        startActivityForResult(intent, REQUEST_VERIFY_PHONE);
    }

    @Override
    public void goToLoginPhoneNumber() {
        if (getActivity() != null) {
            startActivity(CheckLoginPhoneNumberActivity.getCallingIntentFromRegister(getActivity(),
                    phoneNumber.getText().toString()));
            getActivity().finish();
        }
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
    public void showErrorPhoneNumber(String errorMessage) {
        dismissLoading();
        message.setVisibility(View.GONE);
        phoneNumberLayout.setErrorEnabled(true);
        phoneNumberLayout.setError(errorMessage);
    }

    @Override
    public void showAlreadyRegisteredDialog(String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.phone_number_already_registered));
        builder.setMessage(String.format(getResources().getString(R.string.phone_number_already_registered_info), phoneNumber));
        builder.setPositiveButton(getResources().getString(R.string.phone_number_already_registered_yes), (dialog, i) -> goToLoginPhoneNumber());
        builder.setNegativeButton(getResources().getString(R.string.phone_number_already_registered_no), (dialog, i) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    @Override
    public void showConfirmationPhoneNumber(final String phoneNumber) {
        final String realPhoneNumberString = this.phoneNumber.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.valueOf(phoneNumber));
        builder.setMessage(getResources().getString(R.string.phone_number_not_registered_info));
        builder.setPositiveButton(getResources().getString(R.string.phone_number_not_registered_yes), (dialog, i) -> goToVerifyAccountPage(realPhoneNumberString));
        builder.setNegativeButton(getResources().getString(R.string.phone_number_not_registered_no), (dialog, i) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getActivity() != null) {
            if (requestCode == REQUEST_VERIFY_PHONE
                    && resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
