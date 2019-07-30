package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel

interface PromoCheckoutListDigitalContract {

    interface View : PromoCheckoutListContract.View{
        fun onSuccessCheckPromoCode(data: DataUiModel)
    }

    interface Presenter : CustomerPresenter<View>{
        fun checkPromoCode(promoCode: String, promoDigitalModel: PromoDigitalModel)
        fun getListLastSeen(categoryIDs: List<Int>, resources: Resources)
    }
}