package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber
import timber.log.Timber

class AddCartToWishlistSubscriber(private val view: ICartListView?,
                                  private val productId: String,
                                  private val cartId: String,
                                  private val isLastItem: Boolean,
                                  private val source: String) : Subscriber<AddCartToWishlistData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            Timber.e(e)
            it.showToastMessageRed(e)
        }
    }

    override fun onNext(addCartToWishlistData: AddCartToWishlistData) {
        view?.let { view ->
            if (addCartToWishlistData.isSuccess) {
                view.onAddCartToWishlistSuccess(productId, cartId, isLastItem, source)
            } else {
                view.showToastMessageRed(addCartToWishlistData.message ?: "")
            }
        }
    }
}