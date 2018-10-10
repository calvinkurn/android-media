package com.tokopedia.mitra.account.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.logout.domain.model.LogoutDomain;
import com.tokopedia.logout.domain.usecase.LogoutUseCase;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.account.contract.MitraAccountContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MitraAccountPresenter extends BaseDaggerPresenter<MitraAccountContract.View> implements MitraAccountContract.Presenter {
    private UserSession abstractionUserSession;
    private com.tokopedia.user.session.UserSession sessionUserSession;
    LogoutUseCase logoutUseCase;

    @Inject
    public MitraAccountPresenter(UserSession abstractionUserSession, com.tokopedia.user.session.UserSession sessionUserSession, LogoutUseCase logoutUseCase) {
        this.abstractionUserSession = abstractionUserSession;
        this.sessionUserSession = sessionUserSession;
        this.logoutUseCase = logoutUseCase;
    }

    @Override
    public void onViewCreated() {
        getView().renderName(abstractionUserSession.getName());
//        getView().renderPhoneNumber(abstractionUserSession.getPhoneNumber());
    }

    @Override
    public void onLogoutClicked() {
        getView().showLogoutConfirmationDialog();
    }

    @Override
    public void onLogoutConfirmed() {
        getView().hideAccountPage();
        getView().showLogoutLoading();
        logoutUseCase.createObservable(LogoutUseCase.Companion.getParam(sessionUserSession))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<LogoutDomain>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().showAccountPage();
                                    getView().hideLogoutLoading();
                                    getView().showLogoutErrorMessage(R.string.mitra_account_logout_failed);
                                }
                            }

                            @Override
                            public void onNext(LogoutDomain logoutDomain) {
                                if (logoutDomain.is_success()) {
                                    getView().navigateToHomepage();
                                } else {
                                    getView().showLogoutErrorMessage(R.string.mitra_account_logout_failed);
                                }
                                getView().showAccountPage();
                                getView().hideLogoutLoading();
                            }
                        });
    }
}
