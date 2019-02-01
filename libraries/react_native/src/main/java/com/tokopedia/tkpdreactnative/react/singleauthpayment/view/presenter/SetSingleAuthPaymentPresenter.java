package com.tokopedia.tkpdreactnative.react.singleauthpayment.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Subscriber;

/**
 * Created by kris on 4/24/18. Tokopedia
 */

public class SetSingleAuthPaymentPresenter
        extends BaseDaggerPresenter<SetSingleAuthPaymentContract.View>
        implements SetSingleAuthPaymentContract.Presenter{

    private static final String USER_ID_KEY = "USER_ID_KEY";
    private static final String DEVICE_ID_KEY = "DEVICE_ID_KEY";
    private static final String UPDATED_STATE = "UPDATED_STATE";

    private ReactNativeRouter reactNativeRouter;
    private UserSession userSession;
    private UseCase<String> creditCardFingerPrintUseCase;


    public SetSingleAuthPaymentPresenter(ReactNativeRouter reactNativeRouter,
                                         UserSession userSession) {
        this.reactNativeRouter = reactNativeRouter;
        this.userSession = userSession;
    }

    @Override
    public void setSingleAuthenticationMode() {
        getView().showProgressLoading();
        creditCardFingerPrintUseCase = reactNativeRouter.setCreditCardSingleAuthentication();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(USER_ID_KEY, userSession.getUserId());
        requestParams.putString(DEVICE_ID_KEY, userSession.getDeviceId());
        requestParams.putInt(UPDATED_STATE, 1);
        creditCardFingerPrintUseCase.execute(requestParams, creditCardAuthenticationSubscriber());
    }

    private Subscriber<String> creditCardAuthenticationSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressLoading();
                getView().onErrorNetworkSingleAuth(e.getMessage());
            }

            @Override
            public void onNext(String message) {
                getView().hideProgressLoading();
                getView().onSuccessSingleAuth(message);
            }
        };
    }

    @Override
    public void detachView() {
        if(creditCardFingerPrintUseCase != null) {
            creditCardFingerPrintUseCase.unsubscribe();
        }
        super.detachView();
    }
}
