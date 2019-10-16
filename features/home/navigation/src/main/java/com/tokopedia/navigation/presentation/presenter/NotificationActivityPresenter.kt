package com.tokopedia.navigation.presentation.presenter

import android.content.Context
import android.util.Log
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.navigation.domain.ClearCounterNotificationUpdateUseCase
import com.tokopedia.navigation.domain.GetIsTabUpdateUseCase
import com.tokopedia.navigation.domain.GetNotificationTotalUnreadUseCase
import com.tokopedia.navigation.domain.GetNotificationUpdateUnreadUseCase
import com.tokopedia.navigation.domain.SendNotificationUseCase
import com.tokopedia.navigation.domain.pojo.NotifCenterSendNotifData
import com.tokopedia.navigation.domain.pojo.NotificationUpdateUnread
import com.tokopedia.navigation.presentation.view.listener.NotificationActivityContract
import com.tokopedia.navigation.presentation.view.subscriber.GetNotificationUpdateUnreadSubscriber
import com.tokopedia.navigation.presentation.view.subscriber.NotificationUpdateActionSubscriber
import javax.inject.Inject

class NotificationActivityPresenter @Inject constructor(
        private var getNotificationTotalUnreadUseCase: GetNotificationTotalUnreadUseCase,
        private var getNotificationUpdateUnreadUseCase: GetNotificationUpdateUnreadUseCase,
        private var clearCounterNotificationUpdateUseCase: ClearCounterNotificationUpdateUseCase,
        private var getIsTabUpdateUseCase: GetIsTabUpdateUseCase,
        private var sendNotificationUseCase: SendNotificationUseCase)
    : BaseDaggerPresenter<NotificationActivityContract.View>()
        , NotificationActivityContract.Presenter {

    override fun getUpdateUnreadCounter(onSuccess: (NotificationUpdateUnread) -> Unit) {
        getNotificationUpdateUnreadUseCase.execute(GetNotificationUpdateUnreadSubscriber(onSuccess))
    }

    override fun clearNotifCounter() {
        clearCounterNotificationUpdateUseCase.execute(NotificationUpdateActionSubscriber())
    }

    override fun getIsTabUpdate(context: Context) {
        getIsTabUpdateUseCase.execute(
                {
                    val isTabUpdate = it.notifications.isTabUpdate
                    if (isTabUpdate) {
                        view.goToUpdateTab()
                    }
                }, {}
        )
    }

    override fun sendNotif(onSuccessSendNotif: (NotifCenterSendNotifData) -> Unit, onErrorSendNotif: (Throwable) -> Unit){
        sendNotificationUseCase.executeCoroutines(onSuccessSendNotif, onErrorSendNotif)
    }

    override fun detachView() {
        super.detachView()
        sendNotificationUseCase.cancelJobs()
        getNotificationTotalUnreadUseCase.unsubscribe()
        getNotificationUpdateUnreadUseCase.unsubsribe()
        clearCounterNotificationUpdateUseCase.unsubscribe()
        getIsTabUpdateUseCase.cancelJobs()
    }
}
