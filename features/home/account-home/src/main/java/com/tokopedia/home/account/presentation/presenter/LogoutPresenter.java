package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import com.tokopedia.home.account.presentation.view.LogoutView;
import com.tokopedia.logout.domain.model.LogoutDomain;
import com.tokopedia.logout.domain.usecase.LogoutUseCase;
import com.tokopedia.user.session.UserSession;

import rx.Subscriber;

public class LogoutPresenter extends BaseDaggerPresenter<LogoutView>{

    private LogoutUseCase useCase;
    private UserSession userSession;

    public LogoutPresenter(LogoutUseCase useCase, UserSession userSession) {
        this.useCase = useCase;
        this.userSession = userSession;
    }

    public void doLogout(){
        useCase.execute(LogoutUseCase.Companion.getParam(userSession), new Subscriber<LogoutDomain>() {
            @Override
            public void onCompleted() {
                if (isViewAttached()){
                    getView().logoutFacebook();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()){
                    getView().onErrorLogout(throwable);
                }
            }

            @Override
            public void onNext(LogoutDomain logoutDomain) {
                if (logoutDomain.is_success() && isViewAttached()){
                    getView().onSuccessLogout();
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        useCase.unsubscribe();
    }
}
