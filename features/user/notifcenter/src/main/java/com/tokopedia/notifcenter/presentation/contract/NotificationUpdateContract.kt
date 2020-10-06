package com.tokopedia.notifcenter.presentation.contract

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.notifcenter.data.entity.NotificationUpdateTotalUnread
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean

interface NotificationUpdateContract {
    interface View : BaseListViewListener<Visitable<*>>, CustomerView {
        fun showToastMessageError(e: Throwable?)
    }

    interface Presenter: CustomerPresenter<View> {
        fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationViewData) -> Unit, onErrorInitiateData: (Throwable) -> Unit)
        fun getFilter(onSuccessGetFilter: (ArrayList<NotificationUpdateFilterViewBean>) -> Unit)
        fun clearNotifCounter()
        fun markReadNotif(notifId: String)
        fun markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate: () -> Unit)
        fun resetFilter()
        fun getTotalUnreadCounter(onSuccessGetTotalUnreadCounter: (NotificationUpdateTotalUnread) -> Unit)
        fun addProductToCart(userId: String, product: ProductData, onSuccessAddToCart: (DataModel) -> Unit)
        fun updateFilter(filter: HashMap<String, Int>)
    }
}
