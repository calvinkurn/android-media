package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

class GetWishlistSubscriber(private val view: ICartListView?, private val presenter: ICartListPresenter) : Subscriber<GetWishlistResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.setHasTriedToLoadWishList()
        view?.stopAllCartPerformanceTrace()
    }

    override fun onNext(getWishlistResponse: GetWishlistResponse) {
        if (view != null) {
            if (getWishlistResponse.gqlWishList?.wishlistDataList?.isNotEmpty() == true) {
                view.renderWishlist(getWishlistResponse.gqlWishList?.wishlistDataList)
            }
            view.setHasTriedToLoadWishList()
            view.stopAllCartPerformanceTrace()
        }
    }
}
