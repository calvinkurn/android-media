package com.tokopedia.tkpdreactnative.react.fingerprint.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintGetPreferenceUseCase;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintSavePreferenceUseCase;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class FingerprintConfirmationPresenter extends BaseDaggerPresenter<FingerprintConfirmationContract.View>
        implements FingerprintConfirmationContract.Presenter {

    private FingerprintSavePreferenceUseCase fingerprintSavePreferenceUseCase;
    private FingerprintGetPreferenceUseCase fingerprintGetPreferenceUseCase;

    public FingerprintConfirmationPresenter(FingerprintSavePreferenceUseCase fingerprintSavePreferenceUseCase,
                                            FingerprintGetPreferenceUseCase fingerprintGetPreferenceUseCase) {
        this.fingerprintSavePreferenceUseCase = fingerprintSavePreferenceUseCase;
        this.fingerprintGetPreferenceUseCase = fingerprintGetPreferenceUseCase;
    }

    @Override
    public void savePreferenceHide(boolean isShow) {
        getView().showProgressLoading();
        fingerprintSavePreferenceUseCase.execute(fingerprintSavePreferenceUseCase.createRequestParams(isShow),
                getSubscriberPreferenceSave());
    }

    @Override
    public void getPreferenceHide() {
        getView().showProgressLoading();
        fingerprintGetPreferenceUseCase.execute(getSubscriberPreferenceGet());
    }

    private Subscriber<Boolean> getSubscriberPreferenceSave() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().hideProgressLoading();
                    getView().onErrorSavePreference();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().hideProgressLoading();
                getView().onSuccessSavePreference();
            }
        };
    }

    private Subscriber<Boolean> getSubscriberPreferenceGet() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().hideProgressLoading();
                    getView().onErrorGetPreference();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().hideProgressLoading();
                getView().onGetPreference(aBoolean);
            }
        };
    }
}
