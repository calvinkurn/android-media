package com.tokopedia.phoneverification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.phoneverification.domain.interactor.VerifyPhoneNumberUseCase;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;
import com.tokopedia.phoneverification.view.subscriber.VerifyPhoneNumberSubscriber;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * @author by alvinatin on 25/10/18.
 */

public class VerifyPhoneNumberPresenter extends BaseDaggerPresenter<PhoneVerification.View>
        implements PhoneVerification.Presenter {

    private final VerifyPhoneNumberUseCase verifyPhoneNumberUseCase;
    private final UserSession userSession;

    @Inject
    public VerifyPhoneNumberPresenter(VerifyPhoneNumberUseCase verifyPhoneNumberUseCase,
                                      UserSession userSession) {
        this.verifyPhoneNumberUseCase = verifyPhoneNumberUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(PhoneVerification.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        verifyPhoneNumberUseCase.unsubscribe();
    }

    @Override
    public void verifyPhoneNumber(String phoneNumber) {
        verifyPhoneNumberUseCase.execute(
                VerifyPhoneNumberUseCase.getParam(userSession.getUserId(), phoneNumber),
                new VerifyPhoneNumberSubscriber(getView()));
    }
}
