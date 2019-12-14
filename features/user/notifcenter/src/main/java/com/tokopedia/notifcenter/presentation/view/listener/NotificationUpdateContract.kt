package com.tokopedia.notifcenter.presentation.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.notifcenter.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.notifcenter.domain.pojo.ProductData
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateViewModel

interface NotificationUpdateContract {

    interface View : BaseListViewListener<Visitable<*>>, CustomerView {
        fun showMessageAtcError(e: Throwable?)
        fun showMessageAtcSuccess(message: String)
    }

    interface Presenter: CustomerPresenter<View> {
        fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationUpdateViewModel) -> Unit, onErrorInitiateData: (Throwable) -> Unit)
        fun getFilter(onSuccessGetFilter: (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit)
        fun clearNotifCounter()
        fun markReadNotif(notifId: String)
        fun markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate: () -> Unit)
        fun resetFilter()
        fun getTotalUnreadCounter(onSuccessGetTotalUnreadCounter: (NotificationUpdateTotalUnread) -> Unit)
        fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)
        fun updateFilter(filter: HashMap<String, Int>)
    }
}
