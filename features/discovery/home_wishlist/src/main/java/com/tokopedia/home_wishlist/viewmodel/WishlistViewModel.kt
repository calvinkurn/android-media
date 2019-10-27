package com.tokopedia.home_wishlist.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.view.ext.map
import com.tokopedia.home_wishlist.view.ext.switchMap
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * A Class ViewModel For Recommendation Page.
 *
 * @param wishlistRepository gql repository for getResponse from network with GQL request
 * @param userSessionInterface the handler of user session
 * @param dispatcher the dispatcher for coroutine
 */
open class WishlistViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val wishlistRepository: WishlistRepository,
        @Named("Main") val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher){

    private val _searchData = MutableLiveData<Pair<String, Int>>()
    private val _loadMoreData = MutableLiveData<Pair<String, Int>>()

    val initialResult = _searchData.switchMap {
        wishlistRepository.get(it.first, it.second)
    }

    val moreResultData = _loadMoreData.switchMap {
        wishlistRepository.get(it.first, it.second).switchMap {
            MutableLiveData<List<Visitable<*>>>().apply { value = initialResult.value?.plus(it) }
        }
    }

    fun loadInitialPage(){
        _searchData.value = Pair(_searchData.value?.first ?: "", WishlistRepository.DEFAULT_START_PAGE)
    }

    fun loadNextPage(page: Int){
        _loadMoreData.value = Pair(_loadMoreData.value?.first ?: "", page)
    }

    fun reload(){
        _searchData.value = Pair("", WishlistRepository.DEFAULT_START_PAGE)
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

}