package com.tokopedia.notifcenter.presentation.presenter

import android.content.Context
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.notifcenter.data.entity.NotifCenterSendNotifData
import com.tokopedia.notifcenter.data.entity.NotificationUpdateUnread
import com.tokopedia.notifcenter.domain.*
import com.tokopedia.notifcenter.presentation.contract.NotificationActivityContract
import com.tokopedia.notifcenter.presentation.subscriber.GetNotificationUpdateUnreadSubscriber
import com.tokopedia.notifcenter.presentation.subscriber.NotificationUpdateActionSubscriber
import javax.inject.Inject

class NotificationActivityPresenter @Inject constructor(
        private var getNotificationTotalUnreadUseCase: GetNotificationTotalUnreadUseCase,
        private var getNotificationUpdateUnreadUseCase: GetNotificationUpdateUnreadUseCase,
        private var clearCounterNotificationUpdateUseCase: ClearCounterNotificationUpdateUseCase,
        private var getIsTabUpdateUseCase: GetIsTabUpdateUseCase,
        private var sendNotificationUseCase: SendNotificationUseCase
) : BaseDaggerPresenter<NotificationActivityContract.View>(), NotificationActivityContract.Presenter {

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
