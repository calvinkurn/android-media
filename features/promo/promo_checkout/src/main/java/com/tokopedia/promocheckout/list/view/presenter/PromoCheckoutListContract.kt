package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel

interface PromoCheckoutListContract {

    interface View : BaseListViewListener<PromoCheckoutListModel>{
        fun renderListLastSeen(arrayList: MutableList<PromoCheckoutLastSeenModel>)
        fun showGetListLastSeenError(e: Throwable)
    }

    interface Presenter : CustomerPresenter<View>{
        fun getListPromo(serviceId: String, categoryId: Int, page: Int, resources: Resources)
        fun getListLastSeen(resources: Resources)
    }
}