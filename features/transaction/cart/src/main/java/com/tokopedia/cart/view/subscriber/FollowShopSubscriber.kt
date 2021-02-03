package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import rx.Subscriber
import timber.log.Timber

class FollowShopSubscriber(private val view: ICartListView?, private val presenter: ICartListPresenter) : Subscriber<DataFollowShop>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            Timber.e(e)
            it.hideProgressLoading()
            it.showToastMessageRed(e)
        }
    }

    override fun onNext(data: DataFollowShop) {
        view?.let {
            it.hideProgressLoading()
            it.showToastMessageGreen(data.followShop.message)
            presenter.processInitialGetCartData("0", false, false)
        }
    }
}