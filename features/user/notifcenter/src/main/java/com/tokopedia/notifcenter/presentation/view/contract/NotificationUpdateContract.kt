package com.tokopedia.notifcenter.presentation.view.contract

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.notifcenter.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.notifcenter.domain.pojo.ProductData
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationViewBean

interface NotificationUpdateContract {
    interface View : BaseListViewListener<Visitable<*>>, CustomerView {
        fun showMessageAtcError(e: Throwable?)
        fun showMessageAtcSuccess(message: String)
    }

    interface Presenter: CustomerPresenter<View> {
        fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationViewBean) -> Unit, onErrorInitiateData: (Throwable) -> Unit)
        fun getFilter(onSuccessGetFilter: (ArrayList<NotificationUpdateFilterViewBean>) -> Unit)
        fun clearNotifCounter()
        fun markReadNotif(notifId: String)
        fun markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate: () -> Unit)
        fun resetFilter()
        fun getTotalUnreadCounter(onSuccessGetTotalUnreadCounter: (NotificationUpdateTotalUnread) -> Unit)
        fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)
        fun updateFilter(filter: HashMap<String, Int>)
    }
}
