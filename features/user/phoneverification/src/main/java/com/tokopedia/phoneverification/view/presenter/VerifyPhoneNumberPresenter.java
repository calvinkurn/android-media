package com.tokopedia.phoneverification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.phoneverification.domain.interactor.VerifyPhoneNumberUseCase;
import com.tokopedia.phoneverification.view.listener.PhoneVerification;
import com.tokopedia.phoneverification.view.subscriber.VerifyPhoneNumberSubscriber;

import javax.inject.Inject;

/**
 * @author by alvinatin on 25/10/18.
 */

public class VerifyPhoneNumberPresenter extends BaseDaggerPresenter<PhoneVerification.View>
        implements PhoneVerification.Presenter {

    private final VerifyPhoneNumberUseCase verifyPhoneNumberUseCase;

    @Inject
    public VerifyPhoneNumberPresenter(VerifyPhoneNumberUseCase verifyPhoneNumberUseCase) {
        this.verifyPhoneNumberUseCase = verifyPhoneNumberUseCase;
    }

    @Inject


    @Override
    public void detachView() {
        super.detachView();
        verifyPhoneNumberUseCase.unsubscribe();
    }

    @Override
    public void verifyPhoneNumber(String phoneNumber) {
        verifyPhoneNumberUseCase.execute(
                VerifyPhoneNumberUseCase.getParam(getView().getUserSession().getUserId(), phoneNumber),
                new VerifyPhoneNumberSubscriber(getView()));
    }
}
