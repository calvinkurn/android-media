package com.tokopedia.home_wishlist.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
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

    private val listVisitable = mutableListOf<Visitable<*>>()
    private var keywordSearch = ""
    val wishlistData = MediatorLiveData<List<Visitable<*>>>()

    fun loadInitialPage(){
        wishlistData.postValue(listOf(LoadingDataModel()))
        search("")
    }

    fun loadNextPage(page: Int){
        wishlistData.postValue(listVisitable.plus(listOf(LoadMoreDataModel())))
        launchCatchError(block = {
            val data = wishlistRepository.getData(keywordSearch, page)
            if(data.isNotEmpty()){
                val visitableWishlist = mappingWishlistToVisitable(data)
                wishlistData.postValue(listVisitable.plus(mappingLoadingRecommendation(visitableWishlist)))
                val recommendationData = wishlistRepository.getRecommendationData(visitableWishlist.size / 4, page * 2 - 1, data.map { it.id })
                listVisitable.addAll(mappingRecommendationToWishlist(recommendationList = recommendationData, wishlistVisitable = visitableWishlist))
                wishlistData.postValue(ArrayList(listVisitable))
            }
        }){

        }
    }

    fun refresh(){
        search("")
    }

    fun search(text: String){
        keywordSearch = text
        listVisitable.clear()
        launchCatchError(block = {
            val data = wishlistRepository.getData(text, WishlistRepository.DEFAULT_START_PAGE)
            if(data.isEmpty()){
                wishlistData.postValue(listOf(EmptyWishlistDataModel()))
            }else {
                val visitableWishlist = mappingWishlistToVisitable(data)
                wishlistData.postValue(mappingLoadingRecommendation(visitableWishlist))
                val recommendationData = wishlistRepository.getRecommendationData(visitableWishlist.size / 4, 1, data.map { it.id })
                listVisitable.addAll(mappingRecommendationToWishlist(recommendationList = recommendationData, wishlistVisitable = visitableWishlist))
                wishlistData.postValue(ArrayList(listVisitable))
            }
        }){
            wishlistData.postValue(listOf(ErrorWishlistDataModel(it.message)))
        }
    }

    private fun mappingWishlistToVisitable(list: List<WishlistItem>): List<Visitable<*>>{
        return list.map{ WishlistItemDataModel(it) }
    }

    private fun mappingLoadingRecommendation(list: List<Visitable<*>>): List<Visitable<*>>{
        val newMappingList = ArrayList(list)
        val countRecommendationWidget = list.size / 4
        for(i in countRecommendationWidget downTo 1){
            newMappingList.add(i * 4, LoadMoreDataModel())
        }
        return newMappingList
    }

    private fun mappingRecommendationToWishlist(wishlistVisitable: List<Visitable<*>>, recommendationList: List<RecommendationWidget>): List<Visitable<*>>{
        val list = mutableListOf<Visitable<*>>()
        list.addAll(wishlistVisitable)
        for(i in recommendationList.size downTo 1) {
            list.add(i * 4,
                    RecommendationCarouselDataModel(
                            title = recommendationList[i-1].title,
                            list = recommendationList[i-1].recommendationItemList.map { RecommendationCarouselItemDataModel(it) },
                            seeMoreAppLink = recommendationList[i-1].seeMoreAppLink))
        }
        return list
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

}