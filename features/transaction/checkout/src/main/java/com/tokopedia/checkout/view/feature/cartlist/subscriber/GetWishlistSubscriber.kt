package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.wishlist.common.response.GetWishlistResponse

import rx.Subscriber

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

class GetWishlistSubscriber(private val view: ICartListView?, private val presenter: ICartListPresenter) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (view != null) {
            if (graphqlResponse?.getData<Any>(GetWishlistResponse::class.java) != null) {
                val getWishlistResponse = graphqlResponse.getData<GetWishlistResponse>(GetWishlistResponse::class.java)
                if (getWishlistResponse != null && getWishlistResponse.gqlWishList != null &&
                        getWishlistResponse.gqlWishList.wishlistDataList != null &&
                        getWishlistResponse.gqlWishList.wishlistDataList.size > 0) {
                    view.renderWishlist(getWishlistResponse.gqlWishList.wishlistDataList)
                }
            }
        }
    }
}
