package com.tokopedia.home_wishlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.common.WishlistDispatcherProvider
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.util.AddToCartAction
import com.tokopedia.home_wishlist.util.Event
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistData
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import rx.Subscriber
import javax.inject.Inject

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
        private val wishlistCoroutineDispatcherProvider: WishlistDispatcherProvider,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val bulkRemoveWishlistUseCase: BulkRemoveWishlistUseCase
) : BaseViewModel(wishlistCoroutineDispatcherProvider.ui()){

    private val listVisitable = mutableListOf<Visitable<*>>()
    private var keywordSearch = ""
    val wishlistData = MediatorLiveData<List<Visitable<*>>>()
    val addToCartEventData = MediatorLiveData<Event<AddToCartAction>>()

    fun loadInitialPage(){
        wishlistData.postValue(listOf(LoadingDataModel()))
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
        getWishlistData()
    }

    fun getWishlistData(keyword: String = ""){
        keywordSearch = keyword
        listVisitable.clear()
        launchCatchError(block = {
            val data = wishlistRepository.getData(keyword, WishlistRepository.DEFAULT_START_PAGE)
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

    fun removeWishlistedProduct(productId: String) {
        removeWishListUseCase.createObservable(
                productId,
                userSessionInterface.userId,
                object : WishListActionListener {
                    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                        //no-op
                    }

                    override fun onSuccessAddWishlist(productId: String?) {
                        //no-op
                    }

                    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
//                        callback.invoke(false, Throwable(errorMessage))
                    }

                    override fun onSuccessRemoveWishlist(productId: String?) {
                        val updatedList = mutableListOf<Visitable<*>>()
                        wishlistData.value?.forEach {
                            if (it is WishlistItemDataModel) {
                                if (it.productItem.id != productId) {
                                    updatedList.add(it)
                                }
                            } else {
                                updatedList.add(it)
                            }
                        }
                        wishlistData.value = updatedList
                    }

                }
        )
    }
    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

    fun addToCartProduct(productPosition: Int) {
        val visitableItem = wishlistData.value?.get(productPosition)
        if (visitableItem is WishlistItemDataModel) {
            val wishlistItem = visitableItem.productItem
            wishlistItem.let {
                val addToCartRequestParams = AddToCartRequestParams()
                addToCartRequestParams.productId = it.id.toLong()
                addToCartRequestParams.shopId = it.shop.id.toInt()
                addToCartRequestParams.quantity = it.minimumOrder
                addToCartRequestParams.notes = ""

                val requestParams = RequestParams.create()
                requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)

                addToCartUseCase.execute(requestParams, object: Subscriber<AddToCartDataModel>() {
                    override fun onNext(addToCartResult: AddToCartDataModel?) {
                        var position = productPosition
                        var productId = 0
                        var isSuccess = false
                        var cartId = 0
                        var message = ""

                        if (addToCartResult?.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartResult?.data?.success == 1) {
                            isSuccess = true
                            cartId = addToCartResult.data.cartId
                            message = addToCartResult.data.message[0]
                            productId = addToCartResult.data.productId
                        } else {
                            isSuccess = false
                            message = addToCartResult?.errorMessage?.get(0)?:""
                            productId = addToCartResult?.data?.productId?:0
                            cartId = addToCartResult?.data?.cartId?:0
                        }
                        addToCartEventData.value = Event(
                                AddToCartAction(
                                        position = position,
                                        productId = productId,
                                        isSuccess = isSuccess,
                                        cartId = cartId,
                                        message = message
                                )
                        )
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        error(e)
                    }

                })
            }
        }
    }

    fun bulkRemoveWishlist(listOfPosition: List<Int>) {
        val requestParams = RequestParams.create()
        val productRequest = mutableListOf<Pair<String, Int>>()
        val arrayForBulkRemoveCandidate = arrayOfNulls<WishlistItemDataModel>(listOfPosition.size)

        listOfPosition.forEachIndexed { index, it ->
            wishlistData.value?.get(it)?.run {
            if (this is WishlistItemDataModel) {
                arrayForBulkRemoveCandidate[index] = this
                productRequest.add(Pair(this.productItem.id, index))
            }
        } }

        requestParams.putObject(BulkRemoveWishlistUseCase.PARAM_PRODUCT_IDS, productRequest)
        requestParams.putString(BulkRemoveWishlistUseCase.PARAM_USER_ID, userSessionInterface.userId)

        bulkRemoveWishlistUseCase.execute(
                requestParams,
                object: Subscriber<List<WishlistData>>() {
                    override fun onNext(responselist: List<WishlistData>) {
                        val updatedList = wishlistData.value?.toMutableList()

                        responselist.forEach {
                            if (it.isSuccess) {
                                updatedList?.remove(arrayForBulkRemoveCandidate[it.position] as Visitable<*>)
                            }
                        }
                        wishlistData.value = updatedList
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                }
        )
    }
}