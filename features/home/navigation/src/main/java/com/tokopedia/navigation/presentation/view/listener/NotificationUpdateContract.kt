package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel

interface NotificationUpdateContract {

    interface View : BaseListViewListener<Visitable<*>>, CustomerView {

    }

    interface Presenter: CustomerPresenter<View> {
        fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationUpdateViewModel) -> Unit, onErrorInitiateData: (Throwable) -> Unit)
        fun filterBy(selectedItemList: HashMap<Int, Int>, filterViewModel: ArrayList<NotificationUpdateFilterItemViewModel>)
        fun getFilter(onSuccessGetFilter: (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit)
        fun clearNotifCounter()
        fun markReadNotif(notifId: String)
        fun markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate: () -> Unit)
        fun resetFilter()
        fun getTotalUnreadCounter(onSuccessGetTotalUnreadCounter: (NotificationUpdateTotalUnread) -> Unit)
    }
}
