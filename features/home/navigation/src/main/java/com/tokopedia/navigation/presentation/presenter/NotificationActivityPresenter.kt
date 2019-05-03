package com.tokopedia.navigation.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.navigation.domain.ClearCounterNotificationUpdateUseCase
import com.tokopedia.navigation.domain.GetNotificationTotalUnreadUseCase
import com.tokopedia.navigation.presentation.view.listener.NotificationActivityContract
import com.tokopedia.navigation.presentation.view.subscriber.NotificationUpdateActionSubscriber
import javax.inject.Inject

class NotificationActivityPresenter @Inject constructor(
        private var getNotificationTotalUnreadUseCase: GetNotificationTotalUnreadUseCase,
        private var clearCounterNotificationUpdateUseCase: ClearCounterNotificationUpdateUseCase)
    : BaseDaggerPresenter<NotificationActivityContract.View>()
        , NotificationActivityContract.Presenter {


    override fun clearNotifCounter() {
        clearCounterNotificationUpdateUseCase.execute(NotificationUpdateActionSubscriber(
                null,
                null
        ))
    }


}
