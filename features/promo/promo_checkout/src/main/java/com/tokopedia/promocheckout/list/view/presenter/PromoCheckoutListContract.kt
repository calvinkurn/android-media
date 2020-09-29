package com.tokopedia.promocheckout.list.view.presenter

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel

interface PromoCheckoutListContract {

    interface View : BaseListViewListener<PromoCheckoutListModel> {
        fun showProgressLoading()
        fun hideProgressLoading()
        fun renderListLastSeen(data: List<PromoCheckoutLastSeenModel>, isDeals: Boolean)
        fun showGetListLastSeenError(e: Throwable)
        fun onSuccessCheckPromo(data: DataUiModel)
        fun onErrorCheckPromo(e: Throwable)
        fun onErrorEmptyPromo()
        override fun getContext(): Context?
        fun changeTitle(title: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getListPromo(serviceId: String, categoryId: Int, page: Int, resources: Resources)
        fun getListLastSeen(categoryIDs: List<Int>, resources: Resources)
    }
}