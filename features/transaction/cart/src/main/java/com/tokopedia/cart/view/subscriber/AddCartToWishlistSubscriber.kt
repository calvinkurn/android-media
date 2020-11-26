package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber
import timber.log.Timber

class AddCartToWishlistSubscriber(private val view: ICartListView?,
                                  private val productId: String,
                                  private val cartId: String,
                                  private val isLastItem: Boolean,
                                  private val source: String,
                                  private val forceExpandCollapsedUnavailableItems: Boolean) : Subscriber<AddCartToWishlistData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            Timber.e(e)
            it.hideProgressLoading()
            it.showToastMessageRed(e)
        }
    }

    override fun onNext(data: AddCartToWishlistData) {
        view?.let { view ->
            view.hideProgressLoading()
            if (data.isSuccess) {
                view.onAddCartToWishlistSuccess(data.message, productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)
            } else {
                view.showToastMessageRed(data.message ?: "")
            }
        }
    }
}