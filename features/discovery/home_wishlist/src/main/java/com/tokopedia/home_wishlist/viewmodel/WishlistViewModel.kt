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
        wishlistData.value = combineVisitable(listVisitable, listOf(LoadMoreDataModel()))
        launchCatchError(block = {
            val data = wishlistRepository.getData(keywordSearch, page)
            if(data.isNotEmpty()){
                val visitableWishlist = mappingWishlistToVisitable(data)
                wishlistData.postValue(ArrayList(combineVisitable(listVisitable, visitableWishlist)))
                val recommendationData = wishlistRepository.getRecommendationData(page, data.map { it.id })
                if(recommendationData.isNotEmpty()) listVisitable.addAll(mappingRecommendationToWishlist(recommendationList = recommendationData, wishlistVisitable = visitableWishlist))
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
                val recommendationData = wishlistRepository.getRecommendationData(1, data.map { it.id })
                if(recommendationData.isNotEmpty()) listVisitable.addAll(mappingRecommendationToWishlist(recommendationList = recommendationData, wishlistVisitable = visitableWishlist))
                wishlistData.postValue(ArrayList(listVisitable))
            }
        }){
            wishlistData.postValue(listOf(ErrorWishlistDataModel(it.message)))
        }
    }

    fun onDeleteClick(productId: Int){

    }

    fun onAddToCart(productId: Int){

    }

    fun onAddWishlist(productId: Int){

    }

    fun updateWishlist(productId: Int, position: Int, wishlistStatus: Boolean){
        if(position <= listVisitable.size - 1){
            val visitable = listVisitable[position]
            if(visitable is RecommendationCarouselDataModel){
                visitable.list.find { it.recommendationItem.productId == productId }?.let {
                    it.recommendationItem.isWishlist = wishlistStatus
                    wishlistData.postValue(ArrayList(listVisitable))
                }
            }
        }
    }

    fun updateBulkMode(isBulkMode: Boolean){
        val newList = mutableListOf<Visitable<*>>()
        for (dataModel in listVisitable){
            when (dataModel) {
                is WishlistItemDataModel -> {
                    newList.add(dataModel.copy(isBulkMode = isBulkMode))
                }
                is RecommendationCarouselDataModel -> {
                    newList.add(dataModel.copy(isBulkMode = isBulkMode))
                }
                else -> newList.add(dataModel)
            }
        }
        wishlistData.postValue(newList)
    }

    fun onBulkDelete(callback: (Boolean, Throwable?) -> Unit){
        callback.invoke(true, null)
    }

    private fun mappingWishlistToVisitable(list: List<WishlistItem>): List<Visitable<*>>{
        return list.map{ WishlistItemDataModel(it) }
    }

    private fun mappingLoadingRecommendation(list: List<Visitable<*>>): List<Visitable<*>>{
        val newMappingList = ArrayList(list)
        newMappingList.add(4, LoadMoreDataModel())
        return newMappingList
    }

    private fun mappingRecommendationToWishlist(wishlistVisitable: List<Visitable<*>>, recommendationList: List<RecommendationWidget>): List<Visitable<*>>{
        val list = mutableListOf<Visitable<*>>()
        list.addAll(wishlistVisitable)
        list.add(4,
                RecommendationCarouselDataModel(
                        id = recommendationList.first().tid,
                        title = recommendationList.first().title,
                        list = recommendationList.first().recommendationItemList.map { RecommendationCarouselItemDataModel(it) },
                        seeMoreAppLink = recommendationList.first().seeMoreAppLink))
        return list
    }

    private fun combineVisitable(firstList: List<Visitable<*>>, secondList: List<Visitable<*>>): List<Visitable<*>>{
        val newList = ArrayList(firstList)
        newList.addAll(secondList)
        return newList
    }
    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

}