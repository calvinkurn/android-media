package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import com.tokopedia.home.account.data.pojo.NotifCenterSendNotifData;
import com.tokopedia.home.account.domain.SendNotifUseCase;
import com.tokopedia.home.account.presentation.listener.LogoutView;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RedDotGimmickPresenter extends BaseDaggerPresenter<LogoutView>{

    private SendNotifUseCase sendNotifUseCase;

    public RedDotGimmickPresenter(SendNotifUseCase sendNotifUseCase) {
        this.sendNotifUseCase = sendNotifUseCase;
    }

    public void sendNotif(Function1<NotifCenterSendNotifData, Unit> onSuccessSendNotif, Function1<Throwable, Unit> onErrorSendNotif){
        sendNotifUseCase.executeCoroutines(onSuccessSendNotif, onErrorSendNotif);
    }

    @Override
    public void detachView() {
        super.detachView();
        sendNotifUseCase.cancelJobs();
    }
}
