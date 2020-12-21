package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import rx.Subscriber
import timber.log.Timber

class FollowShopSubscriber(private val view: ICartListView?, private val presenter: ICartListPresenter) : Subscriber<DataFollowShop>() {

    companion object {
        const val CTA_WORDING = "Oke"
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            Timber.e(e)
            it.hideProgressLoading()
            it.showToastMessageRed(e, CTA_WORDING)
        }
    }

    override fun onNext(data: DataFollowShop) {
        view?.let {
            it.hideProgressLoading()
            it.showToastMessageGreen(data.followShop.message, CTA_WORDING)
            presenter.processInitialGetCartData("0", false, false)
        }
    }
}