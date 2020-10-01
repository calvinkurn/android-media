package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

class GetWishlistSubscriber(private val view: ICartListView?) : Subscriber<GetWishlistResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.setHasTriedToLoadWishList()
        view?.stopAllCartPerformanceTrace()
    }

    override fun onNext(getWishlistResponse: GetWishlistResponse) {
        view?.let {
            if (getWishlistResponse.gqlWishList?.wishlistDataList?.isNotEmpty() == true) {
                it.renderWishlist(getWishlistResponse.gqlWishList?.wishlistDataList, true)
            }
            it.setHasTriedToLoadWishList()
            it.stopAllCartPerformanceTrace()
        }
    }
}
