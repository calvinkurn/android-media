package com.tokopedia.phoneverification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationPresenter extends BaseDaggerPresenter<PhoneVerification.View>
        implements PhoneVerification.Presenter {

//    private final RequestOtpUseCase requestOtpUseCase;
//    private final ValidateVerifyPhoneNumberUseCase validateVerifyPhoneNumberUseCase;
    private final UserSession userSession;
    private PhoneVerification.View viewListener;

    @Inject
    public PhoneVerificationPresenter(/*RequestOtpUseCase requestOtpUseCase,*/
//                                      ValidateVerifyPhoneNumberUseCase
//                                              validateVerifyPhoneNumberUseCase,
                                      UserSession userSession) {
//        this.requestOtpUseCase = requestOtpUseCase;
//        this.validateVerifyPhoneNumberUseCase = validateVerifyPhoneNumberUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(PhoneVerification.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
//        requestOtpUseCase.unsubscribe();
//        validateVerifyPhoneNumberUseCase.unsubscribe();
    }

    @Override
    public void verifyPhoneNumber(String otpCode, String phoneNumber) {
        if (isValid()) {
            viewListener.showProgressDialog();
            viewListener.setViewEnabled(false);

//            validateVerifyPhoneNumberUseCase.execute(ValidateVerifyPhoneNumberUseCase.getParam(
//                    ValidateOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION,
//                    otpCode,
//                    phoneNumber,
//                    userSession.getUserId()),
//                    new VerifyPhoneNumberSubscriber(viewListener));

        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (viewListener.getPhoneNumber().length() == 0) {
            viewListener.showErrorPhoneNumber(viewListener.getString(R.string
                    .error_field_required));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void requestOtp() {
        if (isValid()) {
            viewListener.setViewEnabled(false);
            viewListener.showProgressDialog();

//            requestOtpUseCase.execute(RequestOtpUseCase.getParam(
//                    RequestOtpUseCase.MODE_SMS,
//                    viewListener.getPhoneNumber(),
//                    RequestOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION,
//                    userSession.getUserId()), new RequestOTPPhoneverificationSubscriber
//                    (viewListener)
//            );
        }
    }

    @Override
    public void requestOtpWithCall() {
        if (isValid()) {
            viewListener.setViewEnabled(false);
            viewListener.showProgressDialog();

//            requestOtpUseCase.execute(RequestOtpUseCase.getParam(
//                    RequestOtpUseCase.MODE_CALL,
//                    viewListener.getPhoneNumber(),
//                    RequestOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION,
//                    userSession.getUserId()
//            ), new RequestOTPPhoneverificationSubscriber(viewListener));
        }
    }
}
