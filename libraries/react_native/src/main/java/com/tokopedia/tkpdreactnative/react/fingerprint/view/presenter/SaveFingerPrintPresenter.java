package com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 4/5/18.
 */

public class SaveFingerPrintPresenter extends BaseDaggerPresenter<SaveFingerPrintContract.View> implements SaveFingerPrintContract.Presenter {
    private UserSessionInterface userSession;
    private SaveFingerPrintUseCase saveFingerPrintUseCase;

    public SaveFingerPrintPresenter(UserSessionInterface userSession, SaveFingerPrintUseCase saveFingerPrintUseCase) {
        this.userSession = userSession;
        this.saveFingerPrintUseCase = saveFingerPrintUseCase;
    }

    @Override
    public void registerFingerPrint(String transactionId, String publicKey, String date, String accountSignature, String userId) {
        getView().showProgressLoading();
        saveFingerPrintUseCase.execute(saveFingerPrintUseCase.createRequestParams(transactionId, publicKey, date, accountSignature, userId),
                getSubscriberSaveFingerprint());
    }

    private Subscriber<Boolean> getSubscriberSaveFingerprint() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressLoading();
                getView().onErrorNetworkRegisterFingerPrint(e);
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().hideProgressLoading();
                if(isSuccess){
                    getView().onSuccessRegisterFingerPrint();
                }else{
                    getView().onErrorNetworkRegisterFingerPrint(new RuntimeException());
                }
            }
        };
    }

    @Override
    public void detachView() {
        saveFingerPrintUseCase.unsubscribe();
        super.detachView();
    }

    public String getUserId() {
        return userSession.getUserId();
    }
}
