package com.tokopedia.home_wishlist.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.common.WishlistDispatcherProvider
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.util.Event
import com.tokopedia.home_wishlist.util.SingleLiveEvent
import com.tokopedia.home_wishlist.util.TypeAction
import com.tokopedia.home_wishlist.util.WishlistAction
import com.tokopedia.home_wishlist.view.ext.copy
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistData
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * A Class ViewModel For Recommendation Page.
 *
 * @param wishlistRepository gql repository for getResponse from network with GQL request
 * @param userSessionInterface the handler of user session
 * @param wishlistCoroutineDispatcherProvider the dispatcher for coroutine
 */
open class WishlistViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val wishlistRepository: WishlistRepository,
        wishlistCoroutineDispatcherProvider: WishlistDispatcherProvider,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val bulkRemoveWishlistUseCase: BulkRemoveWishlistUseCase
) : BaseViewModel(wishlistCoroutineDispatcherProvider.ui()){

    internal val listVisitable = mutableListOf<Visitable<*>>()
    private var keywordSearch = ""
    val wishlistData = MediatorLiveData<List<Visitable<*>>>()
    val action = SingleLiveEvent<Event<WishlistAction>>()

    fun loadInitialPage(){
        wishlistData.postValue(listOf(LoadingDataModel()))
    }

    fun loadNextPage(page: Int){
        wishlistData.value = combineVisitable(listVisitable, listOf(LoadMoreDataModel()))
        launchCatchError(block = {
            val data = wishlistRepository.getData(keywordSearch, page)
            if(data.isNotEmpty()){
                val visitableWishlist = mappingWishlistToVisitable(data)
                wishlistData.postValue(combineVisitable(listVisitable, visitableWishlist).copy())
                val recommendationData = wishlistRepository.getRecommendationData(page, data.map { it.id })
                if(recommendationData.isNotEmpty()) listVisitable.addAll(mappingRecommendationToWishlist(recommendationList = recommendationData, wishlistVisitable = visitableWishlist))
                wishlistData.postValue(listVisitable.copy())
            }
        }){

        }
    }

    /**
     * Void [getWishlistData]
     * @param keyword new keyword value for get wishlist data
     *
     * Calls this function will override search keyword, also reset page number state,
     * and clear all existing wishlist data
     */
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
                val recommendationData = wishlistRepository.getRecommendationData(1, data.map { it.id })
                if(recommendationData.isNotEmpty()) listVisitable.addAll(mappingRecommendationToWishlist(recommendationList = recommendationData, wishlistVisitable = visitableWishlist))
                wishlistData.postValue(listVisitable.copy())
            }
        }){
            wishlistData.postValue(listOf(ErrorWishlistDataModel(it.message)))
        }
    }

    /**
     * Void [addToCartProduct]
     * @param productPosition product wishlist data position in root recyclerview
     *
     * Send request to add to cart, result will be notified to addToCartActionData
     */
    fun addToCartProduct(productPosition: Int) {
        val visitableItem = wishlistData.value?.get(productPosition)
        if (visitableItem is WishlistItemDataModel) {
            listVisitable[productPosition] = visitableItem.copy(isOnAddToCart = true)
            wishlistData.postValue(listVisitable.copy())
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
                        val productId: Int
                        val isSuccess: Boolean
                        val cartId: Int
                        val message: String

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
                        action.value = Event(
                                WishlistAction(
                                        position = productPosition,
                                        productId = productId,
                                        isSuccess = isSuccess,
                                        cartId = cartId,
                                        message = message,
                                        typeAction = TypeAction.ADD_TO_CART
                                )
                        )
                    }

                    override fun onCompleted() {
                        listVisitable[productPosition] = visitableItem.copy(isOnAddToCart = false)
                        wishlistData.postValue(listVisitable.copy())
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        error(e)
                    }

                })
            }
        }
    }

    /**
     * Void [updateRecommendationItemWishlist]
     * @param parentPosition recommendation carousel data position in root recyclerview
     * @param childPosition recommendation item data position in recommendation carousel recyclerview
     * @param newWishlistState new wishlist state to update wishlist status
     *
     * This function will update selected recommendation item with new wishlist state
     */
    fun updateRecommendationItemWishlist(parentPosition: Int, childPosition: Int, newWishlistState: Boolean){
        val newWishlistData: MutableList<Visitable<*>> = (wishlistData.value?:listOf()).toMutableList()
        val newCarouselDataModel = newWishlistData[parentPosition]
        if (newCarouselDataModel is RecommendationCarouselDataModel) {
            val oldRecomItemDataModel = newCarouselDataModel.list[childPosition]
            val oldRecomItem = oldRecomItemDataModel.recommendationItem

            val newRecomItem = oldRecomItem.copy(isWishlist = newWishlistState)
            val newRecomItemDataModel = oldRecomItemDataModel.copy(recommendationItem = newRecomItem)
            val newItemList = newCarouselDataModel.list.copy()
            newItemList[childPosition] = newRecomItemDataModel

            val newRecomCarouselDataModel = newCarouselDataModel.copy(list = newItemList)
            newWishlistData[parentPosition] = newRecomCarouselDataModel
            wishlistData.value = newWishlistData
        }
    }

    /**
     * Void [setRecommendationItemWishlist]
     * @param parentPosition recommendation carousel data position in root recyclerview
     * @param childPosition recommendation item data position in recommendation carousel recyclerview
     * @param currentWishlistState current wishlist state of selected product
     *
     * This function will take action based on wishlist current statue
     * Current status = false -> means this function will do add wishlist action
     * Current status = true -> means this function will do remove wishlist action
     * result will be notified to addWishlistRecommendationActionData or removeWishlistRecommendationActionData
     */
    fun setRecommendationItemWishlist(parentPosition: Int, childPosition: Int, currentWishlistState: Boolean){
        val newWishlistData: MutableList<Visitable<*>> = (wishlistData.value?:listOf()).toMutableList()
        val parentVisitable = newWishlistData[parentPosition]

        if(parentVisitable is RecommendationCarouselDataModel
                && parentVisitable.list.size >= childPosition
                && !currentWishlistState) {
            val recommendationDataModel = (newWishlistData[parentPosition] as RecommendationCarouselDataModel).list[childPosition]
            addWishListUseCase.createObservable(recommendationDataModel.recommendationItem.productId.toString(), userSessionInterface.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    addWishlistRecommendationActionData.value = Event(
                            AddWishlistRecommendationData(
                                    message = errorMessage ?: "",
                                    isSuccess = false
                            )
                    )
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    updateRecommendationItemWishlist(parentPosition, childPosition, !recommendationDataModel.recommendationItem.isWishlist)
                    addWishlistRecommendationActionData.value = Event(
                            AddWishlistRecommendationData(
                                    message = "",
                                    isSuccess = true
                            )
                    )
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

                override fun onSuccessRemoveWishlist(productId: String?) {}
            })
        }  else if(parentVisitable is RecommendationCarouselDataModel
                && parentVisitable.list.size >= childPosition
                && currentWishlistState) {
            val recommendationDataModel = (newWishlistData[parentPosition] as RecommendationCarouselDataModel).list[childPosition]
            removeWishListUseCase.createObservable(recommendationDataModel.recommendationItem.productId.toString(), userSessionInterface.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}

                override fun onSuccessAddWishlist(productId: String?) {}

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    removeWishlistRecommendationActionData.value = Event(
                            RemoveWishlistRecommendationData(
                                    message = errorMessage ?: "",
                                    isSuccess = false
                            )
                    )
                }

                override fun onSuccessRemoveWishlist(productId: String?) {
                    updateRecommendationItemWishlist(parentPosition, childPosition, !recommendationDataModel.recommendationItem.isWishlist)
                    removeWishlistRecommendationActionData.value = Event(
                            RemoveWishlistRecommendationData(
                                    message = "",
                                    isSuccess = true
                            )
                    )
                }
            })
        }
    }

    /**
     * Void [bulkRemoveWishlist]
     * @param positions list of position to removed from wishlist
     *
     * This function will bulk remove selected wishlist data
     * result will be notified to bulkRemovewishlistActionData
     * result can be partially failed, since we use zip observable between single remove usecase
     */
    fun bulkRemoveWishlist(positions: List<Int> = listOf()) {
        var listOfPosition = getWishlistPositionOnMark()

        if (positions.isNotEmpty()) {
            listOfPosition = positions
        }
        val requestParams = RequestParams.create()
        val productRequest = mutableListOf<Pair<String, Int>>()
        val arrayForBulkRemoveCandidate = arrayOfNulls<WishlistItemDataModel>(listOfPosition.size)

        if (listOfPosition.size <= wishlistData.value?.size?:0) {
            listOfPosition.forEachIndexed { index, it ->
                wishlistData.value?.get(it)?.run {
                    if (this is WishlistItemDataModel) {
                        arrayForBulkRemoveCandidate[index] = this
                        productRequest.add(Pair(this.productItem.id, index))
                    }
                }
            }

            requestParams.putObject(BulkRemoveWishlistUseCase.PARAM_PRODUCT_IDS, productRequest)
            requestParams.putString(BulkRemoveWishlistUseCase.PARAM_USER_ID, userSessionInterface.userId)

            bulkRemoveWishlistUseCase.execute(
                    requestParams,
                    object: Subscriber<List<WishlistActionData>>() {
                        override fun onNext(responselist: List<WishlistActionData>) {
                            val updatedList = wishlistData.value?.toMutableList()

                            var isPartiallyFailed = false
                            responselist.forEach {
                                if (it.isSuccess) {
                                    updatedList?.remove(arrayForBulkRemoveCandidate[it.position] as Visitable<*>)
                                } else if (!isPartiallyFailed) isPartiallyFailed = !it.isSuccess
                            }

                            bulkRemovewishlistActionData.value = Event(
                                    BulkRemoveWishlistActionData(
                                            message = "",
                                            isSuccess = true,
                                            isPartiallyFailed = isPartiallyFailed
                                    )
                            )
                            wishlistData.value = updatedList
                        }

                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            bulkRemovewishlistActionData.value = Event(
                                    BulkRemoveWishlistActionData(
                                            message = e.message?:"",
                                            isSuccess = false,
                                            isPartiallyFailed = false
                                    )
                            )
                        }

                    }
            )
        } else {
            bulkRemovewishlistActionData.value = Event(
                    BulkRemoveWishlistActionData(
                            message = "Requested position is out of bounds of wishlist data",
                            isSuccess = false,
                            isPartiallyFailed = false
                    )
            )
        }
    }

    /**
     * Void [removeWishlistedProduct]
     * @param position selected position of product to be removed
     *
     * This function will remove selected wishlist data based on selected position
     */
    fun removeWishlistedProduct(position: Int) {
        val selectedVisitable = wishlistData.value?.get(position)
        selectedVisitable?.let {
            if (it is WishlistItemDataModel) {
                val productId = it.productItem.id
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
                                removeWishlistActionData.value = Event(
                                        RemoveWishlistActionData(
                                                message = errorMessage?:"",
                                                isSuccess = false,
                                                productId = productId?.toInt()?:0
                                        )
                                )
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
                                removeWishlistActionData.value = Event(
                                        RemoveWishlistActionData(
                                                message = "",
                                                isSuccess = true,
                                                productId = productId?.toInt()?:0
                                        )
                                )
                                wishlistData.value = updatedList
                            }

                        }
                )
            }
        }
    }

    /**
     * Void [updateBulkMode]
     * @param isBulkMode bulk mode flag
     *
     * This function will set bulk mode status of all data in wishlist data
     */
    fun updateBulkMode(isBulkMode: Boolean){
        val newVisitable: MutableList<Visitable<*>> = wishlistData.value?.toMutableList()?: mutableListOf()
        for (i in 0 until newVisitable.size){
            when (val dataModel = newVisitable[i]) {
                is WishlistItemDataModel -> {
                    listVisitable[i] = dataModel.copy(isBulkMode = isBulkMode)
                }
                is RecommendationCarouselDataModel -> {
                    newVisitable[i] = dataModel.copy(isOnBulkRemoveProgress = isBulkMode)
                }
            }
        }
        wishlistData.postValue(newVisitable.copy())
    }

    /**
     * Void [setWishlistOnMarkDelete]
     * @param productPosition position of selected product
     * @param isChecked mark status of selected product
     *
     * This function will set isOnChecked status of selected product
     */
    fun setWishlistOnMarkDelete(productPosition: Int, isChecked: Boolean){
        val wishslistDataTemp: MutableList<Visitable<*>> = wishlistData.value?.toMutableList()?: mutableListOf()
        if(productPosition < wishslistDataTemp.size && wishslistDataTemp[productPosition] is WishlistItemDataModel){
            wishslistDataTemp[productPosition] = (wishslistDataTemp[productPosition] as WishlistItemDataModel).copy(
                    isOnChecked = isChecked
            )
            wishlistData.value = wishslistDataTemp
        }
        listVisitableMarked.plus(listOf())
    }

    fun getWishlistPositionOnMark() = listVisitable.withIndex().filter { (_, visitable) ->  visitable is WishlistItemDataModel && visitable.isChecked }.map{ (index, _) -> index }

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
}