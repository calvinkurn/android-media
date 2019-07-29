package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel

interface PromoCheckoutListDigitalContract {

    interface View : PromoCheckoutListContract.View{
        fun showProgressLoading()
        fun hideProgressLoading()
        fun onErrorCheckPromoCode(e: Throwable)
        fun onSuccessCheckPromoStackingCode(data: DataUiModel)
        fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel)
        fun onErrorEmptyPromoCode()
    }

    interface Presenter : CustomerPresenter<View>{
        fun checkPromoStackingCode(promoCode: String, promoDigitalModel: PromoDigitalModel)
        fun getListLastSeen(categoryIDs: List<Int>, resources: Resources)
    }
}