package com.tokopedia.otp.cotp.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.domain.interactor.ValidateOtpLoginUseCase;
import com.tokopedia.otp.cotp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.otp.cotp.view.subscriber.RequestOtpSubscriber;
import com.tokopedia.otp.cotp.view.subscriber.ValidateOtpLoginSubscriber;
import com.tokopedia.otp.cotp.view.subscriber.VerifyOtpSubscriber;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    private final RequestOtpUseCase requestOtpUseCase;
    private final ValidateOtpLoginUseCase validateOtpLoginUseCase;
    private final ValidateOtpUseCase validateOtpUseCase;
    private final UserSession userSession;

    @Inject
    public VerificationPresenter(UserSession userSession,
                                 RequestOtpUseCase requestOtpUseCase,
                                 ValidateOtpLoginUseCase validateOtpLoginUseCase,
                                 ValidateOtpUseCase validateOtpUseCase) {
        this.requestOtpUseCase = requestOtpUseCase;
        this.validateOtpLoginUseCase = validateOtpLoginUseCase;
        this.validateOtpUseCase = validateOtpUseCase;
        this.userSession = userSession;
    }

    public void attachView(Verification.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestOtpUseCase.unsubscribe();
        validateOtpUseCase.unsubscribe();
        validateOtpLoginUseCase.unsubscribe();
    }

    @Override
    public void requestOTP(VerificationViewModel viewModel) {
        if (getView().isCountdownFinished()) {
            getView().showLoadingProgress();
            int otpType = viewModel.getOtpType();
            switch (otpType) {
                case RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION:
                    handleOtpSecurityQuestion(viewModel);
                    break;
                default:
                    handleOtp(viewModel);
                    break;

            }
        }
    }

    private void handleOtp(VerificationViewModel viewModel) {
        switch (viewModel.getType()) {
            case RequestOtpUseCase.MODE_EMAIL:
                if (!TextUtils.isEmpty(viewModel.getEmail())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamEmail(
                            viewModel.getEmail(),
                            viewModel.getOtpType(),
                            userSession.getUserId()
                    ), new RequestOtpSubscriber(getView()));
                }
            default:
                if (!TextUtils.isEmpty(viewModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParam(
                            viewModel.getType(),
                            viewModel.getPhoneNumber(),
                            viewModel.getOtpType(),
                            userSession.getUserId()
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
        }
    }

    private void handleOtpSecurityQuestion(VerificationViewModel viewModel) {
        switch (viewModel.getType()) {
            case RequestOtpUseCase.MODE_EMAIL:
                if (!TextUtils.isEmpty(viewModel.getEmail())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamEmail(
                            viewModel.getEmail(),
                            viewModel.getOtpType(),
                            userSession.getTemporaryUserId()
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
            default:
                if (!TextUtils.isEmpty(viewModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParam(
                            viewModel.getType(),
                            viewModel.getPhoneNumber(),
                            viewModel.getOtpType(),
                            userSession.getTemporaryUserId()
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
        }
    }

    @Override
    public void verifyOtp(int otpType, String phoneNumber, String email, String otpCode) {
        getView().dropKeyboard();
        getView().showLoadingProgress();


        switch (otpType) {
            case RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION:
                validateOtpLoginUseCase.execute(ValidateOtpLoginUseCase.getParam(
                        otpType,
                        otpCode,
                        userSession.getTemporaryUserId(),
                        userSession.getDeviceId()
                ), new ValidateOtpLoginSubscriber(getView()));
                break;
            case RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER:
                validateOtpUseCase.execute(ValidateOtpUseCase.getRegisterPhoneNumberParam(
                        phoneNumber,
                        otpType,
                        otpCode
                ), new VerifyOtpSubscriber(getView()));
                break;
            default:
                validateOtpUseCase.execute(ValidateOtpUseCase.getParam(
                        userSession.getUserId(),
                        otpType,
                        otpCode
                ), new VerifyOtpSubscriber(getView()));
                break;

        }
    }
}
