package com.tokopedia.checkout.view.feature.emptycart2.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class WishlistViewModel @Inject constructor(private val getWishlistUseCase: GetWishlistUseCase) : ViewModel() {

    val wishlistData = MutableLiveData<Result<MutableList<Wishlist>>>()

    fun unsubscribeSubscription() {
        getWishlistUseCase.unsubscribe()
    }

    fun getWishlist() {
        getWishlistUseCase.createObservable(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                wishlistData.value = Fail(RuntimeException())
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                if (graphqlResponse.getData<Any>(GetWishlistResponse::class.java) != null) {
                    val getWishlistResponse = graphqlResponse.getData<GetWishlistResponse>(GetWishlistResponse::class.java)
                    if (getWishlistResponse != null && getWishlistResponse.gqlWishList != null &&
                            getWishlistResponse.gqlWishList.wishlistDataList != null &&
                            getWishlistResponse.gqlWishList.wishlistDataList.size > 0) {
                        wishlistData.value = Success(getWishlistResponse.gqlWishList.wishlistDataList)
                    } else {
                        wishlistData.value = Fail(RuntimeException())
                    }
                } else {
                    wishlistData.value = Fail(RuntimeException())
                }
            }
        })
    }
}