package com.tokopedia.loginphone.verifyotptokocash.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginphone.choosetokocashaccount.domain.LoginPhoneNumberUseCase;
import com.tokopedia.loginphone.choosetokocashaccount.view.subscriber.LoginPhoneNumberSubscriber;
import com.tokopedia.loginphone.verifyotptokocash.domain.interactor.RequestOtpTokoCashUseCase;
import com.tokopedia.loginphone.verifyotptokocash.domain.interactor.VerifyOtpTokoCashUseCase;
import com.tokopedia.sessioncommon.data.loginphone.UserDetail;
import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;
import com.tokopedia.loginphone.verifyotptokocash.view.subscriber.RequestOtpTokoCashSubscriber;
import com.tokopedia.loginphone.verifyotptokocash.view.viewlistener.TokoCashVerificationContract;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 11/30/17.
 */

public class TokoCashVerificationPresenter extends BaseDaggerPresenter<TokoCashVerificationContract.View> implements
        TokoCashVerificationContract.Presenter {

    private final RequestOtpTokoCashUseCase requestTokoCashOTPUseCase;
    private final VerifyOtpTokoCashUseCase verifyTokoCashOTPUseCase;
    private final LoginPhoneNumberUseCase loginTokoCashUseCase;
    private final UserSessionInterface userSessionInterface;

    @Inject
    public TokoCashVerificationPresenter(RequestOtpTokoCashUseCase requestTokoCashOTPUseCase,
                                         VerifyOtpTokoCashUseCase verifyTokoCashOTPUseCase,
                                         LoginPhoneNumberUseCase loginPhoneNumberUseCase,
                                         UserSessionInterface userSessionInterface) {
        this.requestTokoCashOTPUseCase = requestTokoCashOTPUseCase;
        this.verifyTokoCashOTPUseCase = verifyTokoCashOTPUseCase;
        this.loginTokoCashUseCase = loginPhoneNumberUseCase;
        this.userSessionInterface = userSessionInterface;
    }

    public void attachView(TokoCashVerificationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestTokoCashOTPUseCase.unsubscribe();
        verifyTokoCashOTPUseCase.unsubscribe();
        loginTokoCashUseCase.unsubscribe();
    }

    @Override
    public void requestOTP(String phoneNumber, String type) {
        if (!TextUtils.isEmpty(phoneNumber) && getView().isCountdownFinished()) {
            getView().showLoadingProgress();
            switch (type) {
                case RequestOtpUseCase.MODE_SMS:
                    requestTokoCashOTPUseCase.execute(RequestOtpTokoCashUseCase.getParam(phoneNumber,
                            RequestOtpTokoCashUseCase.TYPE_SMS), new
                            RequestOtpTokoCashSubscriber(getView()));
                    break;
                case RequestOtpUseCase.MODE_CALL:
                    requestTokoCashOTPUseCase.execute(RequestOtpTokoCashUseCase.getParam(phoneNumber,
                            RequestOtpTokoCashUseCase.TYPE_PHONE), new
                            RequestOtpTokoCashSubscriber(getView()));
                    break;
            }
        }
    }

    @Override
    public void verifyOtp(String phoneNumber, String otpCode) {
        getView().dropKeyboard();
        getView().showLoadingProgress();
        verifyTokoCashOTPUseCase.execute(VerifyOtpTokoCashUseCase.getParam(phoneNumber, otpCode),
                new Subscriber<VerifyOtpTokoCashPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoadingProgress();
                        getView().onErrorVerifyOtpCode(ErrorHandlerSession.getErrorMessage
                                (getView().getContext(), e));

                    }

                    @Override
                    public void onNext(VerifyOtpTokoCashPojo verifyOtpTokoCashPojo) {
                        getView().dismissLoadingProgress();
                        if (verifyOtpTokoCashPojo.isVerified()
                                        && !verifyOtpTokoCashPojo.getUserDetails().isEmpty()
                                        && verifyOtpTokoCashPojo.getUserDetails().size() == 1) {
                            getView().autoLoginPhoneNumber(verifyOtpTokoCashPojo);
                        } else if (verifyOtpTokoCashPojo.isVerified()
                                && !verifyOtpTokoCashPojo.getUserDetails().isEmpty()) {
                            getView().onSuccessVerifyOTP(verifyOtpTokoCashPojo);
                        } else {
                            getView().onErrorNoAccountTokoCash();
                        }
                    }
                });
    }

    @Override
    public void autoLogin(String key, VerifyOtpTokoCashPojo viewModel) {
        UserDetail accountTokocash = viewModel.getUserDetails().get(0);
        loginWithTokocash(key, accountTokocash);
    }

    private void loginWithTokocash(String key, UserDetail accountTokocash) {
        getView().showLoadingProgress();
        loginTokoCashUseCase.execute(LoginPhoneNumberUseCase.getParam(
                key,
                accountTokocash.getEmail(),
                String.valueOf(accountTokocash.getTkpdUserId()),
                userSessionInterface.getDeviceId()
        ), new LoginPhoneNumberSubscriber(getView().getContext(),
                getView().getLoginRouter(),
                accountTokocash.getEmail(),
                getView()));
    }


}
