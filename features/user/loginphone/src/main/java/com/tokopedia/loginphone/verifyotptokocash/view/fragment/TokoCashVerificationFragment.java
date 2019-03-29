package com.tokopedia.loginphone.verifyotptokocash.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.checkloginphone.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel;
import com.tokopedia.loginphone.common.LoginPhoneNumberRouter;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent;
import com.tokopedia.loginphone.common.di.LoginRegisterPhoneComponent;
import com.tokopedia.loginphone.verifyotptokocash.di.DaggerOtpTokoCashComponent;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;
import com.tokopedia.loginphone.verifyotptokocash.view.activity.TokoCashOtpActivity;
import com.tokopedia.loginphone.verifyotptokocash.view.presenter.TokoCashVerificationPresenter;
import com.tokopedia.loginphone.verifyotptokocash.view.viewlistener.TokoCashVerificationContract;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.SecurityPojo;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;
import com.tokopedia.sessioncommon.view.forbidden.activity.ForbiddenActivity;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author by nisie on 11/30/17.
 */

public class TokoCashVerificationFragment extends VerificationFragment implements
        TokoCashVerificationContract.View {

    private static final int REQUEST_SECURITY_QUESTION = 101;
    public static final int RESULT_SUCCESS_AUTO_LOGIN = 333;

    @Inject
    TokoCashVerificationPresenter presenter;

    @Inject
    LoginPhoneNumberAnalytics loginPhoneNumberAnalytics;

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    UserSessionInterface userSessionInterface;

    public static Fragment createInstance(VerificationViewModel passModel) {
        Fragment fragment = new TokoCashVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PASS_DATA, passModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {

            BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication())
                    .getBaseAppComponent();

            LoginRegisterPhoneComponent loginRegisterPhoneComponent =
                    DaggerLoginRegisterPhoneComponent.builder()
                            .baseAppComponent(appComponent).build();

            DaggerOtpTokoCashComponent daggerOtpTokoCashComponent = (DaggerOtpTokoCashComponent)
                    DaggerOtpTokoCashComponent.builder()
                            .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
                            .build();

            daggerOtpTokoCashComponent.inject(this);
            presenter.attachView(this);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void prepareView() {
        super.prepareView();

        inputOtp.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE
                    && inputOtp.length() == MAX_OTP_LENGTH) {
                loginPhoneNumberAnalytics.trackVerifyOtpClick(viewModel.getMode());
                verifyOtp();
                return true;
            }
            return false;
        });

        verifyButton.setOnClickListener(v -> {
            loginPhoneNumberAnalytics.trackVerifyOtpClick(viewModel.getMode());
            verifyOtp();
        });
    }

    @Override
    protected void verifyOtp() {
        presenter.verifyOtp(viewModel.getPhoneNumber(), inputOtp.getText().toString());
    }

    @Override
    protected void requestOtp() {
        presenter.requestOTP(viewModel.getPhoneNumber(), viewModel.getType());
    }

    @Override
    public void onSuccessVerifyOTP(VerifyOtpTokoCashPojo verifyOtpTokoCashPojo) {
        if (getActivity() != null) {
            removeErrorOtp();
            resetCountDown();

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ChooseTokoCashAccountViewModel.ARGS_DATA,
                    new ChooseTokoCashAccountViewModel(verifyOtpTokoCashPojo.getUserDetails(),
                            viewModel.getPhoneNumber(),
                            verifyOtpTokoCashPojo.getKey()));
            intent.putExtras(bundle);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public void autoLoginPhoneNumber(VerifyOtpTokoCashPojo verifyOtpTokoCashPojo) {
        presenter.autoLogin(verifyOtpTokoCashPojo.getKey(), verifyOtpTokoCashPojo);
    }

    @Override
    public void onErrorNoAccountTokoCash() {
        if (getActivity() != null) {
            startActivity(NotConnectedTokocashActivity.getNoTokocashAccountIntent(getActivity(),
                    viewModel.getPhoneNumber()));
            getActivity().finish();
        }
    }

    @Override
    protected void setFinishedCountdownText() {
        super.setFinishedCountdownText();
        TextView resend = finishCountdownView.findViewById(R.id.resend);
        resend.setOnClickListener(v -> {
            loginPhoneNumberAnalytics.trackResendVerification(viewModel.getMode());

            inputOtp.setText("");
            removeErrorOtp();
            requestOtp();
        });

        TextView useOtherMethod = finishCountdownView.findViewById(R.id.use_other_method);
        useOtherMethod.setOnClickListener(v -> goToOtherVerificationMethod());
    }

    @Override
    protected void goToOtherVerificationMethod() {
        if (getActivity() instanceof TokoCashOtpActivity) {
            ((TokoCashOtpActivity) getActivity()).goToSelectVerificationMethod();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSuccessGetOTP() {
        onSuccessGetOTP(getString(R.string.success_get_otp));
    }


    @Override
    public LoginSuccessRouter getLoginRouter() {
        return new LoginSuccessRouter() {
            @Override
            public void onGoToActivationPage(String email) {
                // Not implemented in login phone number
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
                // Not implemented in login phone number
            }

            @Override
            public void onGoToPhoneVerification() {
                // Not implemented in login phone number
            }

            @Override
            public void onGoToSecurityQuestion(SecurityPojo securityPojo, String fullName, String email, String phone) {
                if (getActivity() != null) {
                    Intent intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                            getActivity(),
                            RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                            email,
                            phone);
                    startActivityForResult(intent, REQUEST_SECURITY_QUESTION);
                    getActivity().finish();
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            onSuccessLogin(userSessionInterface.getTemporaryUserId());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccessLogin(String userId) {
        if (getActivity() != null) {
            loginPhoneNumberAnalytics.eventSuccessLoginPhoneNumber();
            ((LoginPhoneNumberRouter) getActivity().getApplicationContext()).setTrackingUserId(userId,
                    getActivity().getApplicationContext());
            getActivity().setResult(RESULT_SUCCESS_AUTO_LOGIN);
            getActivity().finish();
        }
    }
}
