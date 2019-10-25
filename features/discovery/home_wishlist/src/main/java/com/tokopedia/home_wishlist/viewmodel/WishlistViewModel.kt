package com.tokopedia.home_wishlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * A Class ViewModel For Recommendation Page.
 *
 * @param graphqlRepository gql repository for getResponse from network with GQL request
 * @param userSessionInterface the handler of user session
 * @param getRecommendationUseCase use case for Recommendation Widget
 * @param addWishListUseCase use case for add wishlist
 * @param removeWishListUseCase use case for remove wishlist
 * @param topAdsWishlishedUseCase use case for add wishlist topads product item
 * @param dispatcher the dispatcher for coroutine
 * @param primaryProductQuery the raw query for get primary product
 */
open class WishlistViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val wishlistRepository: WishlistRepository,
        @Named("Main") val dispatcher: CoroutineDispatcher

) : BaseViewModel(dispatcher) {

    private val _searchData = MutableLiveData<Pair<String, Int>>().apply { value = Pair("", 0) }

    /**
     * public variable
     */
    val wishlistData = Transformations.switchMap(_searchData){pair ->
        wishlistRepository.load(pair.first, pair.second)
    }


    val xSource = "recom_landing_page"
    val pageName = "recom_1,recom_2,recom_3"

    fun load(page: Int){

    }

    fun reload(){
        _searchData.value = Pair("reload", 0)
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

}