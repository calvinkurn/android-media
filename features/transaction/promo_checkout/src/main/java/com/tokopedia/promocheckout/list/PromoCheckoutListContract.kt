package com.tokopedia.promocheckout.list

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.list.model.PromoCheckoutListModel

interface PromoCheckoutListContract {

    interface View : BaseListViewListener<PromoCheckoutListModel>{

    }

    interface Presenter : CustomerPresenter<View>{
        fun getListPromo(page: Int)
    }
}