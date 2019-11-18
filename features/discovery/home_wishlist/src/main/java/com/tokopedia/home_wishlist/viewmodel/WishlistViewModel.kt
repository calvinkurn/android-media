package com.tokopedia.home_wishlist.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.common.WishlistDispatcherProvider
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.domain.GetWishlistParameter
import com.tokopedia.home_wishlist.model.action.*
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.util.*
import com.tokopedia.home_wishlist.view.ext.copy
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistActionData
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CancellationException
import rx.Subscriber
import javax.inject.Inject
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.home_wishlist.view.ext.default
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * A Class ViewModel For Recommendation Page.
 *
 * @param wishlistRepository gql repository for getResponse from network with GQL request
 * @param userSessionInterface the handler of user session
 * @param wishlistCoroutineDispatcherProvider the dispatcher for coroutine
 */
open class WishlistViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getWishlistUseCase: GetWishlistDataUseCase,
        wishlistCoroutineDispatcherProvider: WishlistDispatcherProvider,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val bulkRemoveWishlistUseCase: BulkRemoveWishlistUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val getSingleRecommendationUseCase: GetSingleRecommendationUseCase
) : BaseViewModel(wishlistCoroutineDispatcherProvider.ui()){

    //hashmap<position, wishlistDataModel>
    private val listVisitableMarked : HashMap<Int, WishlistDataModel> = hashMapOf()
    private val wishlistData = WishlistLiveData<List<WishlistDataModel>>(listOf())

    /**
     * @param recommendationPositionInPage
     * @param maxItemInPage = 20 wishlist item + 1 recommendation carousel
     *
     * Used in getWishlistData and getNextPageData
     */
    private val recommendationPositionInPage = 4
    private val maxItemInPage = 21

    var keywordSearch: String = ""
        private set

    var currentPage = 1
        private set

    val wishlistLiveData: LiveData<List<WishlistDataModel>> get() = wishlistData
    val isInBulkModeState: LiveData<Boolean> get() = isInBulkMode
    val isWishlistEmptyState: LiveData<Boolean> get() = isWishlistEmpty
    val isWishlistErrorInFirstPageState: LiveData<Boolean> get() = isWishlistErrorInFirstPage

    val wishlistState = WishlistLiveData(Status.LOADING)

    val addToCartActionData = SingleObserverLiveEvent<Event<AddToCartActionData>>()
    val removeWishlistActionData = SingleObserverLiveEvent<Event<RemoveWishlistActionData>>()
    val bulkRemoveWishlistActionData = SingleObserverLiveEvent<Event<BulkRemoveWishlistActionData>>()
    val loadMoreWishlistAction = SingleObserverLiveEvent<Event<LoadMoreWishlistActionData>>()
    val addWishlistRecommendationActionData = SingleObserverLiveEvent<Event<AddWishlistRecommendationData>>()
    val removeWishlistRecommendationActionData = SingleObserverLiveEvent<Event<RemoveWishlistRecommendationData>>()

    private val isInBulkMode = SingleObserverLiveEvent<Boolean>().default(false)
    private val isWishlistEmpty = SingleObserverLiveEvent<Boolean>().default(false)
    private val isWishlistErrorInFirstPage = SingleObserverLiveEvent<Boolean>().default(false)

    val bulkSelectActionData = SingleObserverLiveEvent<Event<Int>>().apply { value = Event(0) }

    private fun loadInitialPage(){
        wishlistData.value = listOf(LoadingDataModel())
    }

    /**
     * Void [getWishlistData]
     * @param keyword new keyword value for get wishlist data
     * @param shouldShowInitialPage to load shimmering loading before get data
     *
     * Calls this function will override search keyword, also reset page number state,
     * and clear all existing wishlist data
     */
    fun getWishlistData(keyword: String = "", shouldShowInitialPage: Boolean = false){
        if (shouldShowInitialPage) loadInitialPage()

        keywordSearch = keyword
        currentPage = 1

        launchCatchError(block = {
            val data = getWishlistUseCase.getData(
                    GetWishlistParameter(keyword, currentPage))
            if (!data.isSuccess) {
                isWishlistErrorInFirstPage.value = true

                wishlistData.value = listOf(ErrorWishlistDataModel(data.errorMessage))
                currentPage--
                return@launchCatchError
            }

            if(data.items.isEmpty()){
                isWishlistEmpty.value = true

                wishlistState.value = Status.EMPTY
                wishlistData.value = listOf(
                        if(keyword.isEmpty()) EmptyWishlistDataModel() else EmptySearchWishlistDataModel(keyword)
                )
                getRecommendationOnEmptyWishlist(0)
            } else {
                isWishlistEmpty.value = false

                var visitableWishlist = mappingWishlistToVisitable(data.items)

                wishlistData.value = visitableWishlist

                if (data.items.size >= recommendationPositionInPage ) {
                    val recommendationData = getRecommendationUseCase.getData(
                            GetRecommendationRequestParam(
                                    pageNumber = currentPage,
                                    productIds = data.items.map { it.id },
                                    pageName = "wishlist"
                            )
                    )
                    if(recommendationData.isNotEmpty()) {
                        visitableWishlist = mappingRecommendationToWishlist(
                                currentPage = currentPage,
                                recommendationList = recommendationData,
                                wishlistVisitable = visitableWishlist,
                                recommendationPositionInPage = recommendationPositionInPage,
                                maxItemInPage = maxItemInPage
                        ).toMutableList()
                        wishlistData.value = visitableWishlist
                    }
                }
            }
        }){
            isWishlistErrorInFirstPage.value = true
            it.printStackTrace()
            if(it is CancellationException){
                wishlistData.value = listOf(ErrorWishlistDataModel("Tunggu sebentar, biar Toped bereskan. Coba lagi atau kembali nanti."))
            }else {
                wishlistData.value = listOf(ErrorWishlistDataModel(it.message))
            }

            wishlistState.value = Status.ERROR
            currentPage--
        }
    }

    /**
     * Void [getNextPageWishlistData]
     *
     * Calls this function to get next page data of existing product request params
     */
    fun getNextPageWishlistData(){
        var newPageVisitableData = wishlistData.value.copy().toMutableList()
        wishlistData.value = combineVisitable(newPageVisitableData.copy(), listOf(LoadMoreDataModel()))
        currentPage++

        launchCatchError(block = {
            val data = getWishlistUseCase.getData(GetWishlistParameter(keywordSearch, currentPage))
            if (!data.isSuccess) {
                wishlistData.value = newPageVisitableData
                currentPage--
                loadMoreWishlistAction.value = Event(LoadMoreWishlistActionData(
                        isSuccess = false,
                        hasNextPage = data.hasNextPage,
                        message = data.errorMessage))
                return@launchCatchError
            }
            if (data.items.isEmpty()) {
                wishlistData.value = newPageVisitableData
            }
            if(data.items.isNotEmpty()){
                newPageVisitableData = combineVisitable(newPageVisitableData, mappingWishlistToVisitable(data.items))
                wishlistData.value = newPageVisitableData
                if (data.items.size >= recommendationPositionInPage ) {
                    val recommendationData = getRecommendationUseCase.getData(
                            GetRecommendationRequestParam(
                                    pageNumber = currentPage,
                                    productIds = data.items.map { it.id },
                                    pageName = "wishlist"
                            )
                    )
                    if(recommendationData.isNotEmpty()) {
                        wishlistData.value = mappingRecommendationToWishlist(
                                currentPage = currentPage,
                                recommendationList = recommendationData,
                                wishlistVisitable = newPageVisitableData,
                                recommendationPositionInPage = recommendationPositionInPage,
                                maxItemInPage = maxItemInPage)
                    }
                }
                loadMoreWishlistAction.value = Event(LoadMoreWishlistActionData(
                        isSuccess = true,
                        hasNextPage = data.hasNextPage,
                        message = data.errorMessage))
            }
        }){
            wishlistData.value = newPageVisitableData
            loadMoreWishlistAction.value = Event(LoadMoreWishlistActionData(
                    isSuccess = false,
                    hasNextPage = false,
                    message = it.message
                    ?: ""))
            currentPage--
        }
    }

    /**
     * Void [getRecommendationOnEmptyWishlist]
     *
     * Calls this function to get recommendation on empty wishlist
     */
    fun getRecommendationOnEmptyWishlist(page: Int){
        val emptyDataVisitable = wishlistData.value
        wishlistData.value = wishlistData.value.plus(LoadMoreDataModel())
        launchCatchError(block = {
            val widget = getSingleRecommendationUseCase.getData(
                    GetRecommendationRequestParam(pageNumber = page)
            )
            val newList = emptyDataVisitable.toMutableList()
            if(page == 0) newList.add(RecommendationTitleDataModel(widget.title, ""))
            newList.addAll(widget.recommendationItemList.map { RecommendationItemDataModel(widget.title, it) })
            wishlistData.value = newList
        }){
            it.message
        }
    }

    /**
     * Void [addToCartProduct]
     * @param productPosition product wishlist data position in root recyclerView
     *
     * Send request to add to cart, result will be notified to addToCartActionData
     */
    fun addToCartProduct(productPosition: Int) {
        val visitableItem = wishlistData.value[productPosition]
        if (visitableItem is WishlistItemDataModel) {
            val wishlistItemCandidateToAddToCart = visitableItem.productItem
            val tempWishlist = visitableItem.copy(isOnAddToCartProgress = true)
            val tempListVisitable = wishlistData.value.copy().toMutableList()

            tempListVisitable[productPosition] = tempWishlist
            wishlistData.value = tempListVisitable.copy()

            wishlistItemCandidateToAddToCart.let {
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

                        if (addToCartResult?.status.equals(AddToCartDataModel.STATUS_OK, true)
                                && addToCartResult?.data?.success == 1) {
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
                        addToCartActionData.value = Event(
                                AddToCartActionData(
                                        position = productPosition,
                                        productId = productId,
                                        cartId = cartId,
                                        isSuccess = isSuccess,
                                        message = message,
                                        item = visitableItem.productItem
                                )
                        )
                        tempListVisitable[productPosition] = visitableItem.copy(isOnAddToCartProgress = false)
                        wishlistData.value = tempListVisitable.copy()
                    }

                    override fun onCompleted() {
                        tempListVisitable[productPosition] = visitableItem.copy(isOnAddToCartProgress = false)
                        wishlistData.value = tempListVisitable.copy()
                    }

                    override fun onError(e: Throwable) {
                        tempListVisitable[productPosition] = visitableItem.copy(isOnAddToCartProgress = false)
                        wishlistData.value = tempListVisitable.copy()
                        addToCartActionData.value = Event(
                                AddToCartActionData(
                                        position = productPosition,
                                        isSuccess = false,
                                        message = e.message ?: ""
                                )
                        )
                    }

                })
            }
        }
    }

    /**
     * Void [updateRecommendationItemWishlist]
     * @param parentPosition recommendation carousel data position in root recyclerView
     * @param childPosition recommendation item data position in recommendation carousel recyclerView
     * @param newWishlistState new wishlist state to update wishlist status
     *
     * This function will update selected recommendation item with new wishlist state
     */
    open fun updateRecommendationItemWishlist(parentPosition: Int, childPosition: Int, newWishlistState: Boolean){
        val DEFAULT_PARENT_POSITION_EMPTY_RECOM = -1
        val newWishlistData: MutableList<WishlistDataModel> = wishlistData.value.toMutableList()

        if (parentPosition == DEFAULT_PARENT_POSITION_EMPTY_RECOM) {
            val newWishlistDataModel = newWishlistData[childPosition]
            if (newWishlistDataModel is RecommendationItemDataModel) {
                val newRecomItem = newWishlistDataModel.recommendationItem.copy(isWishlist = newWishlistState)
                newWishlistData[childPosition] = newWishlistDataModel.copy(recommendationItem = newRecomItem)
                wishlistData.value = newWishlistData
            }
        } else {
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
        val newWishlistData: MutableList<Visitable<*>> = wishlistData.value.toMutableList()
        if(parentPosition != -1) {
            val parentVisitable = newWishlistData[parentPosition]

            if (parentVisitable is RecommendationCarouselDataModel
                    && parentVisitable.list.size >= childPosition
                    && !currentWishlistState) {
                val recommendationDataModel = (newWishlistData[parentPosition] as RecommendationCarouselDataModel).list[childPosition]
                addWishlistForRecommendationItem(
                        recommendationDataModel.recommendationItem.productId.toString(),
                        recommendationDataModel.recommendationItem.isWishlist,
                        parentPosition,
                        childPosition)
            } else if (parentVisitable is RecommendationCarouselDataModel
                    && parentVisitable.list.size >= childPosition
                    && currentWishlistState) {
                val recommendationDataModel = (newWishlistData[parentPosition] as RecommendationCarouselDataModel).list[childPosition]
                removeWishlistForRecommendationItem(recommendationDataModel.recommendationItem.productId.toString(),
                        recommendationDataModel.recommendationItem.isWishlist,
                        parentPosition,
                        childPosition)
            }
        }else if(newWishlistData.size > childPosition && newWishlistData[childPosition] is RecommendationItemDataModel){
            val recommendationDataModel = (newWishlistData[childPosition] as RecommendationItemDataModel)
            if(!currentWishlistState){
                addWishlistForRecommendationItem(
                        recommendationDataModel.recommendationItem.productId.toString(),
                        recommendationDataModel.recommendationItem.isWishlist,
                        parentPosition,
                        childPosition)
            }else {
                removeWishlistForRecommendationItem(
                        recommendationDataModel.recommendationItem.productId.toString(),
                        recommendationDataModel.recommendationItem.isWishlist,
                        parentPosition,
                        childPosition)
            }
        }
    }

    /**
     * Void [setEmptyWishlistRecommendationItemWishlist]
     * @param childPosition recommendation item data position in wishlist recyclerview
     * @param currentWishlistState current wishlist state of selected product
     *
     * This function will take action based on wishlist current statue
     * Current status = false -> means this function will do add wishlist action
     * Current status = true -> means this function will do remove wishlist action
     * result will be notified to addWishlistRecommendationActionData or removeWishlistRecommendationActionData
     */
    fun setEmptyWishlistRecommendationItemWishlist(recommendationPosition: Int,
                                                   currentWishlistState: Boolean){
        val newWishlistData: MutableList<Visitable<*>> = wishlistData.value.toMutableList()
        val recommendationVisitable = newWishlistData[recommendationPosition]
        val DEFAULT_PARENT_POSITION_EMPTY_RECOM = -1

        if(recommendationVisitable is RecommendationItemDataModel
                && !currentWishlistState) {
            addWishlistForRecommendationItem(
                    recommendationVisitable.recommendationItem.productId.toString(),
                    recommendationVisitable.recommendationItem.isWishlist,
                    DEFAULT_PARENT_POSITION_EMPTY_RECOM,
                    recommendationPosition)

        }
        else if(recommendationVisitable is RecommendationItemDataModel
                && currentWishlistState) {
            removeWishlistForRecommendationItem(
                    recommendationVisitable.recommendationItem.productId.toString(),
                    recommendationVisitable.recommendationItem.isWishlist,
                    DEFAULT_PARENT_POSITION_EMPTY_RECOM,
                    recommendationPosition)
        }
    }

    /**
     * Void [bulkRemoveWishlist]
     * @param positions list of position to removed from wishlist
     *
     * This function will bulk remove selected wishlist data
     * result will be notified to bulkRemoveWishlistActionData
     * result can be partially failed
     */
    fun bulkRemoveWishlist(positions: List<Int> = listOf()) {
        var listOfPosition = getWishlistPositionOnMark()

        if (positions.isNotEmpty()) {
            listOfPosition = positions
        }
        val requestParams = RequestParams.create()
        val productRequestId = mutableListOf<String>()

        val listForBulkRemoveCandidate = hashMapOf<String, WishlistItemDataModel>()

        if (listOfPosition.size <= wishlistData.value.size) {
            listOfPosition.forEachIndexed { index, it ->
                wishlistData.value[it].run {
                    if (this is WishlistItemDataModel) {
                        listForBulkRemoveCandidate.put(this.productItem.id, this)
                        productRequestId.add(this.productItem.id)
                    }
                }
            }

            requestParams.putObject(BulkRemoveWishlistUseCase.PARAM_PRODUCT_IDS, productRequestId)
            requestParams.putString(BulkRemoveWishlistUseCase.PARAM_USER_ID, userSessionInterface.userId)

            bulkRemoveWishlistUseCase.execute(
                    requestParams,
                    object: Subscriber<WishlistActionData>() {
                        override fun onNext(responselist: WishlistActionData) {
                            val removeWishlistTriple = removeWishlistItemsInBulkRemove(
                                    responselist, listForBulkRemoveCandidate)
                            val updatedList = removeWishlistTriple.first
                            val deletedIds = removeWishlistTriple.second
                            val isPartiallyFailed = removeWishlistTriple.third

                            bulkRemoveWishlistActionData.value = Event(
                                    BulkRemoveWishlistActionData(
                                            message = "",
                                            isSuccess = !isPartiallyFailed,
                                            isPartiallyFailed = isPartiallyFailed,
                                            productIds = deletedIds
                                    )
                            )
                            bulkSelectActionData.value = Event(0)
                            wishlistData.value = updatedList
                            updateBulkMode(false)
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable) {
                            bulkSelectActionData.value = Event(0)
                            bulkRemoveWishlistActionData.value = Event(
                                    BulkRemoveWishlistActionData(
                                            message = e.message ?: "",
                                            isSuccess = false,
                                            isPartiallyFailed = false
                                    )
                            )
                            updateBulkMode(false)
                        }

                    }
            )
        } else {
            bulkRemoveWishlistActionData.value = Event(
                    BulkRemoveWishlistActionData(
                            message = "Requested position is out of bounds of wishlist data",
                            isSuccess = false,
                            isPartiallyFailed = false
                    )
            )
            updateBulkMode(false)
        }
    }

    private fun removeWishlistItemsInBulkRemove(
            responselist: WishlistActionData,
            listForBulkRemoveCandidate: HashMap<String, WishlistItemDataModel>
            ): Triple<List<WishlistDataModel>, List<String>, Boolean> {
        val updatedList = mutableListOf<WishlistDataModel>()
        val newWishlistDataValue = wishlistData.value.copy()
        val recommendationCarouselDataModels = mutableListOf<WishlistDataModel>()
        val deletedIds = mutableListOf<String>()

        //save carousel instance temporarily
        for(i in 4 until newWishlistDataValue.size-1 step maxItemInPage) {
            if (newWishlistDataValue[i] is RecommendationCarouselDataModel) {
                recommendationCarouselDataModels.add(newWishlistDataValue[i])
                newWishlistDataValue.removeAt(i)
            }
        }

        val ids = responselist.productId.split(",")
        ids.forEach {
            val wishlistDataModel = listForBulkRemoveCandidate[it]
            listForBulkRemoveCandidate.remove(it)

            wishlistDataModel?.let {wishlistDataModel->
                newWishlistDataValue.remove(wishlistDataModel)
                deletedIds.add(it)
            }
        }

        //bring back carousel
        var recomIndex = 4
        recommendationCarouselDataModels.forEach {
            newWishlistDataValue.add(recomIndex, it)
            recomIndex+=maxItemInPage
        }

        val isPartiallyFailed = !listForBulkRemoveCandidate.isEmpty()

        updatedList.addAll(newWishlistDataValue)
        return Triple(updatedList, deletedIds, isPartiallyFailed)
    }

    /**
     * Void [removeWishlistedProduct]
     * @param position selected position of product to be removed
     *
     * This function will remove selected wishlist data based on selected position
     */
    fun removeWishlistedProduct(position: Int) {
        val selectedVisitable = wishlistData.value[position]
        selectedVisitable.let {
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
                                                message = errorMessage ?: "",
                                                isSuccess = false,
                                                productId = productId?.toInt() ?: 0
                                        )
                                )
                            }

                            override fun onSuccessRemoveWishlist(productId: String?) {
                                val updatedList = removeWishlistItems(selectedVisitable)

                                removeWishlistActionData.value = Event(
                                        RemoveWishlistActionData(
                                                message = "",
                                                isSuccess = true,
                                                productId = productId?.toInt() ?: 0
                                        )
                                )
                                if(updatedList.isEmpty()) updatedList.add(EmptyWishlistDataModel())
                                wishlistData.value = updatedList
                            }

                        }
                )
            }
        }
    }

    private fun removeWishlistItems(selectedVisitable: WishlistDataModel): MutableList<WishlistDataModel> {
        val newWishlistDataValue = wishlistData.value.copy()

        val recommendationCarouselDataModels = mutableListOf<WishlistDataModel>()

        //save carousel instance temporarily
        for(i in 4 until newWishlistDataValue.size-1 step maxItemInPage) {
            if (newWishlistDataValue[i] is RecommendationCarouselDataModel) {
                recommendationCarouselDataModels.add(newWishlistDataValue[i])
                newWishlistDataValue.removeAt(i)
            }
        }

        newWishlistDataValue.remove(selectedVisitable)

        //bring back carousel
        var recomIndex = 4
        recommendationCarouselDataModels.forEach {
            newWishlistDataValue.add(recomIndex, it)
            recomIndex+=maxItemInPage
        }

        val updatedList = mutableListOf<WishlistDataModel>()
        updatedList.addAll(newWishlistDataValue)
        return updatedList
    }

    /**
     * Void [updateBulkMode]
     * @param isBulkMode bulk mode flag
     *
     * This function will set bulk mode status of all data in wishlist data
     */
    private fun updateBulkMode(isBulkMode: Boolean){
        isInBulkMode.value = isBulkMode
        val newVisitable: MutableList<WishlistDataModel> = wishlistData.value.toMutableList()
        for (i in 0 until newVisitable.size){
            when (val dataModel = newVisitable[i]) {
                is WishlistItemDataModel -> {
                    newVisitable[i] = dataModel.copy(isOnBulkRemoveProgress = isBulkMode)
                }
                is RecommendationCarouselDataModel -> {
                    newVisitable[i] = dataModel.copy(isOnBulkRemoveProgress = isBulkMode)
                }
            }
        }
        if(!isBulkMode) clearAllMarkedWishlistItem()
        wishlistData.value = newVisitable
    }

    fun enterBulkMode() {
        updateBulkMode(isBulkMode = true)
    }

    fun exitBulkMode() {
        updateBulkMode(isBulkMode = false)
    }

    private fun clearAllMarkedWishlistItem() {
        listVisitableMarked.forEach {
            if (it.value is WishlistItemDataModel) (it.value as WishlistItemDataModel).isOnChecked = false
        }
        listVisitableMarked.clear()
    }

    /**
     * Void [setWishlistOnMarkDelete]
     * @param productPosition position of selected product
     * @param isChecked mark status of selected product
     *
     * This function will set isOnChecked status of selected product
     */
    fun setWishlistOnMarkDelete(productPosition: Int, isChecked: Boolean){
        val wishlistDataTemp: MutableList<WishlistDataModel> = wishlistData.value.toMutableList()
        if(productPosition < wishlistDataTemp.size && wishlistDataTemp[productPosition] is WishlistItemDataModel){
            wishlistDataTemp[productPosition] = (wishlistDataTemp[productPosition] as WishlistItemDataModel).copy(
                    isOnChecked = isChecked
            )
            if(isChecked) {
                listVisitableMarked[productPosition] = wishlistDataTemp[productPosition]
            }
            else {
                listVisitableMarked.remove(productPosition)
            }
            bulkSelectActionData.value = Event(listVisitableMarked.size)
            wishlistData.value = wishlistDataTemp.copy()
        }
    }

    private fun addWishlistForRecommendationItem(productId: String,
                                                 currentWishlistState: Boolean,
                                                 parentPosition: Int,
                                                 childPosition: Int) {
        addWishListUseCase.createObservable(productId, userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                addWishlistRecommendationActionData.value = Event(
                        AddWishlistRecommendationData(
                                message = errorMessage ?: "",
                                isSuccess = false
                        )
                )
            }

            override fun onSuccessAddWishlist(productId: String?) {
                updateRecommendationItemWishlist(parentPosition, childPosition, !currentWishlistState)
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
    }

    private fun removeWishlistForRecommendationItem(productId: String,
                                                    currentWishlistState: Boolean,
                                                    parentPosition: Int,
                                                    childPosition: Int) {
        removeWishListUseCase.createObservable(productId, userSessionInterface.userId, object : WishListActionListener {
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
                updateRecommendationItemWishlist(parentPosition, childPosition, !currentWishlistState)
                removeWishlistRecommendationActionData.value = Event(
                        RemoveWishlistRecommendationData(
                                message = "",
                                isSuccess = true
                        )
                )
            }
        })
    }

    private fun getWishlistPositionOnMark() = listVisitableMarked.map { it.key }

    private fun mappingWishlistToVisitable(list: List<WishlistItem>): MutableList<WishlistDataModel>{
        return list.map{ WishlistItemDataModel( productItem = it,isOnBulkRemoveProgress = isInBulkMode.value?:false) }.toMutableList()
    }

    private fun mappingRecommendationToWishlist(
            currentPage: Int,
            wishlistVisitable: List<WishlistDataModel>,
            recommendationList: List<RecommendationWidget>,
            recommendationPositionInPage: Int,
            maxItemInPage: Int): List<WishlistDataModel>{
        val list = mutableListOf<WishlistDataModel>()

        val recommendationPositionInThisPage = ((currentPage-1) * maxItemInPage) + recommendationPositionInPage
        list.addAll(wishlistVisitable)
        list.add(recommendationPositionInThisPage,
                RecommendationCarouselDataModel(
                        id = recommendationList.first().tid,
                        title = recommendationList.first().title,
                        list = recommendationList.first().recommendationItemList.map {
                            RecommendationCarouselItemDataModel(it, recommendationList.first().title, getRecommendationParentPosition(
                                    maxItemInPage,
                                    recommendationPositionInPage,
                                    currentPage)) } as MutableList<RecommendationCarouselItemDataModel>,
                        isOnBulkRemoveProgress = isInBulkMode.value?:false,
                        seeMoreAppLink = recommendationList.first().seeMoreAppLink))
        return list
    }

    private fun getRecommendationParentPosition(maxItemInPage: Int,
                                                recommendationPositionInPage: Int,
                                                currentPage: Int): Int {
        return (currentPage - 1) * maxItemInPage + recommendationPositionInPage
    }

    private fun combineVisitable(firstList: List<WishlistDataModel>, secondList: List<WishlistDataModel>): MutableList<WishlistDataModel>{
        val newList = ArrayList(firstList)
        newList.addAll(secondList)
        return newList
    }
}