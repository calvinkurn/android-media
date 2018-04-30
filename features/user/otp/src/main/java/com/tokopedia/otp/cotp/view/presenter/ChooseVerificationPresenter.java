package com.tokopedia.otp.cotp.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.otp.common.network.OtpErrorHandler;
import com.tokopedia.otp.cotp.domain.GetVerificationMethodListUseCase;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 1/18/18.
 */

public class ChooseVerificationPresenter extends BaseDaggerPresenter<SelectVerification.View>
        implements SelectVerification.Presenter {

    private final GetVerificationMethodListUseCase getVerificationMethodListUseCase;
    private final UserSession userSession;

    @Inject
    public ChooseVerificationPresenter(UserSession userSession,
                                       GetVerificationMethodListUseCase
                                               getVerificationMethodListUseCase) {
        this.getVerificationMethodListUseCase = getVerificationMethodListUseCase;
        this.userSession = userSession;
    }

    @Override
    public void getMethodList(String phoneNumber, int otpType) {
        getView().showLoading();

        String userId = userSession.isLoggedIn() ? userSession.getUserId() : userSession
                .getTemporaryUserId();
        getVerificationMethodListUseCase.execute(GetVerificationMethodListUseCase
                .getParam(phoneNumber,
                        otpType,
                        userId), new Subscriber<ListVerificationMethod>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().dismissLoading();
                getView().onErrorGetList(OtpErrorHandler.getErrorMessage(e, getView().getContext
                        ()));
            }

            @Override
            public void onNext(ListVerificationMethod listVerificationMethod) {
                getView().dismissLoading();
                if (listVerificationMethod.getList().isEmpty()) {
                    getView().onErrorGetList("");
                } else {
                    getView().onSuccessGetList(listVerificationMethod);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getVerificationMethodListUseCase.unsubscribe();
    }
}
