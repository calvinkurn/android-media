package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import com.tokopedia.home.account.data.pojo.NotifCenterSendNotifData;
import com.tokopedia.home.account.domain.SendNotifUseCase;
import com.tokopedia.home.account.presentation.listener.LogoutView;
import com.tokopedia.logout.domain.model.LogoutDomain;
import com.tokopedia.logout.domain.usecase.LogoutUseCase;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.user.session.UserSession;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import rx.Subscriber;

public class LogoutPresenter extends BaseDaggerPresenter<LogoutView>{

    private LogoutUseCase logoutUseCase;
    private SendNotifUseCase sendNotifUseCase;
    private UserSession userSession;
    private WalletPref walletPref;

    public LogoutPresenter(LogoutUseCase logoutUseCase, SendNotifUseCase sendNotifUseCase,
                           UserSession userSession, WalletPref walletPref) {
        this.logoutUseCase = logoutUseCase;
        this.sendNotifUseCase = sendNotifUseCase;
        this.userSession = userSession;
        this.walletPref = walletPref;
    }

    public void doLogout(){
        logoutUseCase.execute(LogoutUseCase.Companion.getParam(userSession), new Subscriber<LogoutDomain>() {
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
                    walletPref.clear();
                    getView().onSuccessLogout();
                }
            }
        });
    }

    public void sendNotif(Function1<NotifCenterSendNotifData, Unit> onSuccessSendNotif, Function1<Throwable, Unit> onErrorSendNotif){
        sendNotifUseCase.executeCoroutines(onSuccessSendNotif, onErrorSendNotif);
    }

    @Override
    public void detachView() {
        super.detachView();
        logoutUseCase.unsubscribe();
    }
}
