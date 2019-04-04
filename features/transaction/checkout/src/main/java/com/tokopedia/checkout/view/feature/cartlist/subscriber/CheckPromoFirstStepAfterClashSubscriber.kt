package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 25/03/19.
 */

class CheckPromoFirstStepAfterClashSubscriber(val view: ICartListView?,
                                              val presenter: ICartListPresenter,
                                              val checkPromoCodeStackingCodeMapper: CheckPromoStackingCodeMapper)
    : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideProgressLoading()
        view?.showToastMessageRed(ErrorHandler.getErrorMessage(view.activity, e));
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideProgressLoading()
        val responseGetPromoStack = checkPromoCodeStackingCodeMapper.call(response)
        if (responseGetPromoStack.status != "OK" || responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
            val message = responseGetPromoStack.data.message.text
            view?.showToastMessageRed(message);
        } else {
            if (responseGetPromoStack.data.clashings.isClashedPromos) {
                view?.onClashCheckPromo(responseGetPromoStack.data.clashings)
            } else {
                view?.onSuccessCheckPromoFirstStep(responseGetPromoStack)
            }
        }
    }

}