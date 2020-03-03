package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 25/03/19.
 */

class ClearCacheAutoApplyAfterClashSubscriber(val view: ICartListView?,
                                              val presenter: ICartListPresenter,
                                              val promoStackingGlobalData: PromoStackingData,
                                              val newPromoList: ArrayList<ClashingVoucherOrderUiModel>,
                                              val type: String) : Subscriber<ClearCacheAutoApplyStackResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideProgressLoading()
        view?.onFailedClearPromoStack(false)
    }

    override fun onNext(response: ClearCacheAutoApplyStackResponse) {
        view?.hideProgressLoading()
        if (response.successData.success) {
            view?.onSuccessClearPromoStackAfterClash()
            presenter.processApplyPromoStackAfterClash(promoStackingGlobalData, newPromoList, type)
        } else {
            view?.onFailedClearPromoStack(false)
        }
    }

}