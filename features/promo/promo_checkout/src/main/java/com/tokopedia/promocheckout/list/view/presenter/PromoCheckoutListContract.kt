package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listpromocatalog.TokopointsCatalogHighlight
import com.tokopedia.promocheckout.list.model.listpromolastseen.GetPromoSuggestion

interface PromoCheckoutListContract {

    interface View : BaseListViewListener<PromoCheckoutListModel> {
        fun showProgressLoading()
        fun hideProgressLoading()
        fun renderListLastSeen(data: GetPromoSuggestion?)
        fun showGetListLastSeenError(e: Throwable)
        fun renderListExchangeCoupon(data: TokopointsCatalogHighlight)
        fun onSuccessCheckPromo(data: DataUiModel)
        fun onErrorCheckPromo(e: Throwable)
        fun onErrorEmptyPromo()
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter : CustomerPresenter<View> {
        fun getListPromo(serviceId: String, categoryId: Int, page: Int, resources: Resources )
        fun getListLastSeen(serviceId: String, resources: Resources)
    }
}