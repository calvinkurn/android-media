package com.tokopedia.loginregister.activation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginregister.activation.domain.pojo.ActionPojo;
import com.tokopedia.loginregister.activation.domain.usecase.ActivateUnicodeUseCase;
import com.tokopedia.loginregister.activation.domain.usecase.ResendActivationUseCase;
import com.tokopedia.loginregister.activation.view.listener.ActivationContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;
import com.tokopedia.sessioncommon.network.TokenErrorException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 10/19/18.
 */
public class ActivationPresenter extends BaseDaggerPresenter<ActivationContract.View> implements
        ActivationContract.Presenter {

    private ResendActivationUseCase resendActivationUseCase;
    private ActivateUnicodeUseCase activateUnicodeUseCase;

    @Inject
    public ActivationPresenter(ResendActivationUseCase resendActivationUseCase,
                               ActivateUnicodeUseCase activateUnicodeUseCase) {
        this.resendActivationUseCase = resendActivationUseCase;
        this.activateUnicodeUseCase = activateUnicodeUseCase;
    }

    @Override
    public void activateAccount(String email, String unicode) {
        getView().showLoadingProgress();
        activateUnicodeUseCase.execute(ActivateUnicodeUseCase.getParam(email, unicode),
                new Subscriber<TokenViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof TokenErrorException
                                && !((TokenErrorException) e).getErrorDescription().isEmpty()) {
                            getView().onErrorActivateWithUnicode(((TokenErrorException) e).getErrorDescription());
                        } else {
                            getView().onErrorActivateWithUnicode(ErrorHandlerSession.getErrorMessage
                                    (getView().getContext(), e));
                        }
                    }

                    @Override
                    public void onNext(TokenViewModel tokenViewModel) {
                        if (!tokenViewModel.getAccessToken().isEmpty())
                            getView().onSuccessActivateWithUnicode(tokenViewModel);
                        else {
                            getView().onErrorActivateWithUnicode(
                                    ErrorHandlerSession.getDefaultErrorCodeMessage(
                                            ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, getView().getContext()
                                    )
                            );
                        }
                    }
                });
    }

    @Override
    public void resendActivation(String email) {
        getView().showLoadingProgress();
        resendActivationUseCase.execute(ResendActivationUseCase.getParam(email),
                new Subscriber<ActionPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorResendActivation(ErrorHandlerSession.getErrorMessage
                                (getView().getContext(), e));
                    }

                    @Override
                    public void onNext(ActionPojo actionPojo) {
                        if (actionPojo.getIsSuccess() == 1)
                            getView().onSuccessResendActivation();
                        else {
                            getView().onErrorResendActivation(ErrorHandlerSession.getDefaultErrorCodeMessage(
                                    ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, getView().getContext()
                            ));
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        resendActivationUseCase.unsubscribe();
        activateUnicodeUseCase.unsubscribe();
    }
}
