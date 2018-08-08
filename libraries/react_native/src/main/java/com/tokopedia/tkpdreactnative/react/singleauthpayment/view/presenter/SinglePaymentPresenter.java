package com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.domain.SinglePaymentGetPreferenceUseCase;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.domain.SinglePaymentSavePreferenceUseCase;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class SinglePaymentPresenter extends BaseDaggerPresenter<SinglePaymentConfirmationContract.View>
        implements SinglePaymentConfirmationContract.Presenter {

    private SinglePaymentSavePreferenceUseCase singlePaymentSavePreferenceUseCase;
    private SinglePaymentGetPreferenceUseCase singlePaymentGetPreferenceUseCase;

    public SinglePaymentPresenter(SinglePaymentSavePreferenceUseCase singlePaymentSavePreferenceUseCase,
                                            SinglePaymentGetPreferenceUseCase singlePaymentGetPreferenceUseCase) {
        this.singlePaymentSavePreferenceUseCase = singlePaymentSavePreferenceUseCase;
        this.singlePaymentGetPreferenceUseCase = singlePaymentGetPreferenceUseCase;
    }

    @Override
    public void savePreferenceHide(boolean isShow) {
        getView().showProgressLoading();
        singlePaymentSavePreferenceUseCase.execute(singlePaymentSavePreferenceUseCase.createRequestParams(isShow),
                getSubscriberPreferenceSave());
    }

    @Override
    public void getPreferenceHide() {
        getView().showProgressLoading();
        singlePaymentGetPreferenceUseCase.execute(getSubscriberPreferenceGet());
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
