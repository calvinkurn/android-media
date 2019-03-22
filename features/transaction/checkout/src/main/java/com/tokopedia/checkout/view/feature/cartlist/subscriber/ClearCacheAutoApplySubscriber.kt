package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

class ClearCacheAutoApplySubscriber(val view: ICartListView?,
                                    val presenter: ICartListPresenter,
                                    val shopIndex: Int,
                                    val ignoreAPIResponse: Boolean) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideProgressLoading()
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideProgressLoading()
        val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
        if (ignoreAPIResponse) {
            if (responseData.success) {
                view?.onSuccessClearPromoStack(shopIndex)
            }
        } else {
            if (responseData.success) {
                view?.onSuccessClearPromoStack(shopIndex)
            } else {
                view?.onFailedClearPromoStack(ignoreAPIResponse, shopIndex)
            }
        }
    }

}