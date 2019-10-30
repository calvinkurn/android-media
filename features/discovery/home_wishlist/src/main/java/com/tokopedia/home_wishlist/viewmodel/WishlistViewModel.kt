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
                val recommendationData = wishlistRepository.getRecommendationData(1, data.map { it.id })
                if(recommendationData.isNotEmpty()) listVisitable.addAll(mappingRecommendationToWishlist(recommendationList = recommendationData, wishlistVisitable = visitableWishlist))
                wishlistData.postValue(listVisitable.copy())
            }
        }){
            wishlistData.postValue(listOf(ErrorWishlistDataModel(it.message)))
        }
    }

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

    fun addWishlist(parentPosition: Int, childPosition: Int){
        val parent = listVisitable[parentPosition]
        if(parent is RecommendationCarouselDataModel && parent.list.size >= childPosition ) {
            val child = (listVisitable[parentPosition] as RecommendationCarouselDataModel).list[childPosition]
            addWishListUseCase.createObservable(child.recommendationItem.productId.toString(), userSessionInterface.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    action.value = Event(
                            WishlistAction(
                                    message = errorMessage ?: "",
                                    isSuccess = false,
                                    typeAction = TypeAction.ADD_WISHLIST
                            )
                    )
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    val newList = parent.list.copy()
                    newList[childPosition] = newList[childPosition].copy(
                            recommendationItem = newList[childPosition].recommendationItem.copy(isWishlist = true)
                    )
                    listVisitable[parentPosition] = (listVisitable[parentPosition] as RecommendationCarouselDataModel).copy(
                            list = newList
                    )
                    wishlistData.postValue(listVisitable)
                    action.value = Event(
                            WishlistAction(
                                    message = "",
                                    isSuccess = true,
                                    typeAction = TypeAction.ADD_WISHLIST
                            )
                    )
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

                override fun onSuccessRemoveWishlist(productId: String?) {}
            })
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
                        action.value = Event(
                                WishlistAction(
                                        message = "",
                                        isSuccess = false,
                                        typeAction = TypeAction.BULK_DELETE_WISHLIST
                                )
                        )
                        wishlistData.value = updatedList
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        action.value = Event(
                                WishlistAction(
                                        message = e.message ?: "",
                                        isSuccess = false,
                                        typeAction = TypeAction.BULK_DELETE_WISHLIST
                                )
                        )
                    }

                }
        )
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

    fun updateWishlist(productId: Int, position: Int, wishlistStatus: Boolean){
        if(position <= listVisitable.size - 1){
            val visitable = listVisitable[position]
            if(visitable is RecommendationCarouselDataModel){
                visitable.list.find { it.recommendationItem.productId == productId }?.let {
                    it.recommendationItem.isWishlist = wishlistStatus
                    wishlistData.postValue(listVisitable.copy())
                }
            }
        }
    }

    fun updateBulkMode(isBulkMode: Boolean){
        for (i in 0 until listVisitable.size){
            when (val dataModel = listVisitable[i]) {
                is WishlistItemDataModel -> {
                    listVisitable[i] = dataModel.copy(isBulkMode = isBulkMode)
                }
                is RecommendationCarouselDataModel -> {
                    listVisitable[i] = dataModel.copy(isBulkMode = isBulkMode)
                }
            }
        }
        wishlistData.postValue(listVisitable.copy())
    }

    fun setWishlistOnMarkDelete(productPosition: Int, isChecked: Boolean){
        if(productPosition <= listVisitable.size && listVisitable[productPosition] is WishlistItemDataModel){
            listVisitable[productPosition] = (listVisitable[productPosition] as WishlistItemDataModel).copy(
                    isChecked = isChecked
            )
        }
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