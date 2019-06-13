package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 25/03/19.
 */

class ClearCacheAutoApplyAfterClashSubscriber(val view: ICartListView?,
                                              val presenter: ICartListPresenter,
                                              val newPromoList: ArrayList<ClashingVoucherOrderUiModel>,
                                              val type: String) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideProgressLoading()
        view?.onFailedClearPromoStack(false)
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideProgressLoading()
        val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
        if (responseData.successData.success) {
            view?.onSuccessClearPromoStackAfterClash()
            presenter.processApplyPromoStackAfterClash(newPromoList, type)
        } else {
            view?.onFailedClearPromoStack(false)
        }
    }

}