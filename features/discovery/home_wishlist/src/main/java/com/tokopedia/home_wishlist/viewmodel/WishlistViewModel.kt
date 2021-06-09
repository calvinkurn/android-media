package com.tokopedia.home_wishlist.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.domain.GetWishlistParameter
import com.tokopedia.home_wishlist.model.action.*
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.util.SingleObserverLiveEvent
import com.tokopedia.home_wishlist.util.Status
import com.tokopedia.home_wishlist.util.WishlistLiveData
import com.tokopedia.home_wishlist.view.ext.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.smart_recycler_helper.SmartVisitable
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistActionData
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.request.WishlistAdditionalParamRequest
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * A Class ViewModel For Recommendation Page.
 *
 * @param userSessionInterface the handler of user session
 * @param getWishlistUseCase use case helper for get data wishlist
 * @param wishlistCoroutineDispatcherProvider the dispatcher for coroutine
 * @param addWishListUseCase use case helper for add wishlist
 * @param removeWishListUseCase use case helper for remove wishlist
 * @param bulkRemoveWishlistUseCase use case helper for bulk remove wishlist
 * @param getRecommendationUseCase use case helper for get recommendation
 * @param getSingleRecommendationUseCase use case helper for get single recommendation
 * @param topAdsImageViewUseCase use case helper for get topads banner
 */
@SuppressLint("SyntheticAccessor")
open class WishlistViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getWishlistUseCase: GetWishlistDataUseCase,
        private val wishlistCoroutineDispatcherProvider: CoroutineDispatchers,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val bulkRemoveWishlistUseCase: BulkRemoveWishlistUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val getSingleRecommendationUseCase: GetSingleRecommendationUseCase,
        private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
        private val updateCartCounterUseCase: UpdateCartCounterUseCase
) : ViewModel(), CoroutineScope{

    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = wishlistCoroutineDispatcherProvider.main + masterJob

    private var tempSelectedProductIdInPdp: Int? = null
    private var tempSelectedPositionInPdp: Int? = null
    private var tempSelectedParentPositionInPDP: Int? = null

    private val listVisitableMarked : HashMap<Int, WishlistItemDataModel> = hashMapOf()
    private val listRecommendationCarouselOnMarked : HashMap<Int, WishlistDataModel> = hashMapOf()

    private val wishlistData = WishlistLiveData<List<WishlistDataModel>>(listOf())

    private val recommendationPositionInPage = 4
    private val maxItemInPage = 21
    private val newMinItemRegularRule = 24

    private var keywordSearch: String = ""

    var currentPage = 1
        private set

    private var additionalParams: WishlistAdditionalParamRequest = WishlistAdditionalParamRequest()

    val wishlistLiveData: LiveData<List<WishlistDataModel>> get() = wishlistData
    val isInBulkModeState: LiveData<Boolean> get() = isInBulkMode
    val isWishlistState: LiveData<Status> get() = wishlistState
    val isWishlistErrorInFirstPageState: LiveData<Boolean> get() = isWishlistErrorInFirstPage

    val addToCartActionData = SingleObserverLiveEvent<Event<AddToCartActionData>>()
    val updateCartCounterActionData = SingleObserverLiveEvent<Event<Int>>()
    val productClickActionData = SingleObserverLiveEvent<Event<ProductClickActionData>>()
    val removeWishlistActionData = SingleObserverLiveEvent<Event<RemoveWishlistActionData>>()
    val bulkRemoveWishlistActionData = SingleObserverLiveEvent<Event<BulkRemoveWishlistActionData>>()
    val loadMoreWishlistAction = SingleObserverLiveEvent<Event<LoadMoreWishlistActionData>>()
    val addWishlistRecommendationActionData = SingleObserverLiveEvent<Event<AddWishlistRecommendationData>>()
    val removeWishlistRecommendationActionData = SingleObserverLiveEvent<Event<RemoveWishlistRecommendationData>>()
    val bulkSelectCountActionData = SingleObserverLiveEvent<Event<Int>>().apply { value = Event(0) }

    private val isInBulkMode = SingleObserverLiveEvent<Boolean>().default(false)
    private val wishlistState = SingleObserverLiveEvent<Status>().default(Status.LOADING)
    private val isWishlistErrorInFirstPage = SingleObserverLiveEvent<Boolean>().default(false)


    private fun loadInitialPage(){
        wishlistState.value = Status.LOADING
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
    fun getWishlistData(keyword: String = "", additionalParams: WishlistAdditionalParamRequest = WishlistAdditionalParamRequest(), shouldShowInitialPage: Boolean = false){
        if (shouldShowInitialPage) loadInitialPage()

        keywordSearch = keyword
        currentPage = 1
        this.additionalParams = additionalParams

        launchCatchError(wishlistCoroutineDispatcherProvider.main, block = {
            val data = getWishlistUseCase.getData(
                    GetWishlistParameter(keyword, currentPage, additionalParams))
            if (!data.isSuccess) {
                isWishlistErrorInFirstPage.value = true

                wishlistData.value = listOf(ErrorWishlistDataModel(data.errorMessage))
                currentPage--
                return@launchCatchError
            }

            if(data.items.isEmpty()){
                wishlistState.value = Status.EMPTY

                wishlistData.value = listOf(
                        if(keyword.isEmpty()) EmptyWishlistDataModel() else EmptySearchWishlistDataModel(keyword)
                )
                getRecommendationOnEmptyWishlist(0)
            } else {
                wishlistState.value = Status.SUCCESS

                val visitableWishlist = data.items.mappingWishlistToVisitable(isInBulkMode.value ?: false)

                when {
                    data.items.size < recommendationPositionInPage -> {
                        wishlistData.value = getRecommendationWishlist(visitableWishlist, currentPage, data.items.map { it.id }, data.items.size)
                    }

                    // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                    data.items.size == recommendationPositionInPage -> {
                        wishlistData.value = getTopadsAndRecommendationWishlist(visitableWishlist, currentPage, data.items.map { it.id }, data.items.size)
                    }

                    // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                    data.items.size > recommendationPositionInPage -> {
                        if (data.totalData > newMinItemRegularRule) {
                            wishlistData.value = getTopAdsBannerData(visitableWishlist, currentPage, data.items.map { it.id }, recommendationPositionInPage)
                        } else {
                            wishlistData.value = getTopadsAndRecommendationWishlist(visitableWishlist, currentPage, data.items.map { it.id }, data.items.size)
                        }
                    }
                }
            }
        }){
            isWishlistErrorInFirstPage.value = true
            wishlistData.value = listOf(ErrorWishlistDataModel())
            currentPage--
        }
    }

    /**
     * Void [getNextPageWishlistData]
     *
     * Calls this function to get next page data of existing product request params
     */
    fun getNextPageWishlistData(){
        wishlistData.value = wishlistData.value.copy().combineVisitable(listOf(LoadMoreDataModel()))
        currentPage++

        launchCatchError(wishlistCoroutineDispatcherProvider.main, block = {
            val data = getWishlistUseCase.getData(GetWishlistParameter(keywordSearch, currentPage, additionalParams))
            if (!data.isSuccess || data.items.isEmpty()) {
                wishlistData.value = removeLoadMore()
                currentPage--
                loadMoreWishlistAction.value = Event(LoadMoreWishlistActionData(
                        isSuccess = data.isSuccess,
                        hasNextPage = data.hasNextPage,
                        message = data.errorMessage))
                return@launchCatchError
            } else {
                val newPageVisitableData = removeLoadMore().combineVisitable(data.items.mappingWishlistToVisitable(isInBulkMode.value ?: false))

                if (data.items.size >= recommendationPositionInPage && currentPage % 2 == 0) {
                    wishlistData.value = getRecommendationWishlist(newPageVisitableData, currentPage, data.items.map { it.id }, recommendationPositionInPage)
                } else {
                    wishlistData.value = getTopAdsBannerData(newPageVisitableData, currentPage, data.items.map { it.id }, recommendationPositionInPage)
                }

                if (!data.hasNextPage) {
                    wishlistState.value = Status.DONE
                }

                loadMoreWishlistAction.value = Event(LoadMoreWishlistActionData(
                        isSuccess = true,
                        hasNextPage = data.hasNextPage,
                        message = data.errorMessage))
            }
        }){
            wishlistData.value = removeLoadMore()
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
                    GetRecommendationRequestParam(pageNumber = page, pageName = "empty_wishlist")
            )
            val newList = emptyDataVisitable.toMutableList()
            if(page == 0) newList.add(RecommendationTitleDataModel(widget.title, ""))
            newList.addAll(widget.recommendationItemList.map { RecommendationItemDataModel(widget.title, it) })
            wishlistData.value = newList
        }){
        }
    }

    /**
     * Void [addToCartProduct]
     * @param productPosition product wishlist data position in root recyclerView
     *
     * Send request to add to cart, result will be notified to addToCartActionData
     */
    fun addToCartProduct(productPosition: Int) {
        if(productPosition < wishlistData.value.size && productPosition != -1) {
            val visitableItem = wishlistData.value.getOrNull(productPosition)
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
                    addToCartRequestParams.atcFromExternalSource = AddToCartRequestParams.ATC_FROM_WISHLIST
                    addToCartRequestParams.productName = it.name
                    addToCartRequestParams.category = it.categoryBreadcrumb
                    addToCartRequestParams.price = it.price
                    addToCartRequestParams.userId = userSessionInterface.userId

                    val requestParams = RequestParams.create()
                    requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)

                    addToCartUseCase.execute(requestParams, object : Subscriber<AddToCartDataModel>() {
                        override fun onNext(addToCartResult: AddToCartDataModel?) {
                            val productId: Long
                            val isSuccess: Boolean
                            val cartId: String
                            val message: String

                            if (addToCartResult?.status.equals(AddToCartDataModel.STATUS_OK, true)
                                    && addToCartResult?.data?.success == 1) {
                                isSuccess = true
                                cartId = addToCartResult.data.cartId
                                message = addToCartResult.data.message[0]
                                productId = addToCartResult.data.productId
                                updateCartCounter()
                            } else {
                                isSuccess = false
                                message = addToCartResult?.errorMessage?.get(0) ?: ""
                                productId = addToCartResult?.data?.productId ?: 0
                                cartId = addToCartResult?.data?.cartId ?: ""
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
                                            message = ""
                                    )
                            )
                        }

                    })
                }
            }
        }
    }

    private fun updateCartCounter(){
        updateCartCounterUseCase.createObservable(RequestParams.create())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Int>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(count: Int) {
                        updateCartCounterActionData.value = Event(count)
                    }
                })
    }

    private suspend fun getTopadsAndRecommendationWishlist(wishlistVisitable: List<WishlistDataModel>, page: Int, productIds: List<String>, recomIndex: Int): List<WishlistDataModel> =
            withContext(wishlistCoroutineDispatcherProvider.io){
                try{
                    if (wishlistVisitable.isNotEmpty()) {
                        val recommendationPositionInPreviousPage = ((currentPage - 3) * maxItemInPage) + recommendationPositionInPage
                        var pageToken = ""
                        if(recommendationPositionInPreviousPage >= 0 && wishlistVisitable.getOrNull(recommendationPositionInPreviousPage) is BannerTopAdsDataModel){
                            pageToken = (wishlistVisitable[recommendationPositionInPreviousPage] as BannerTopAdsDataModel).topAdsDataModel.nextPageToken ?: ""
                        }
                        val topadsResult = topAdsImageViewUseCase.getImageData(
                                topAdsImageViewUseCase.getQueryMap(
                                        "",
                                        "6",
                                        pageToken,
                                        1,
                                        3,
                                        ""
                                )
                        )

                        val recommendationResult = getRecommendationUseCase.getData(
                                GetRecommendationRequestParam(
                                        pageNumber = page,
                                        productIds = productIds,
                                        pageName = WISHLIST_PAGE_NAME
                                )
                        )

                        if (topadsResult.isNotEmpty() && recommendationResult.isNotEmpty()) {
                            return@withContext mappingTopadsBannerWithRecommendationToWishlist(
                                    topadsBanner = topadsResult.first(),
                                    wishlistVisitable = wishlistVisitable,
                                    listRecommendation = recommendationResult,
                                    recommendationPositionInPage = recommendationPositionInPage,
                                    currentPage = currentPage,
                                    isInBulkMode = isInBulkMode.value ?: false,
                                    listRecommendationCarouselOnMarked = listRecommendationCarouselOnMarked,
                                    maxItemInPage = maxItemInPage,
                                    recommendationIndex = recomIndex+1
                            )
                        } else if (recommendationResult.isNotEmpty()) {
                            return@withContext recommendationResult.mappingRecommendationToWishlist(
                                    currentPage = page,
                                    wishlistVisitable = wishlistVisitable,
                                    recommendationPositionInPage = recomIndex,
                                    maxItemInPage = maxItemInPage,
                                    isInBulkMode = isInBulkMode.value ?: false,
                                    listRecommendationCarouselOnMarked = listRecommendationCarouselOnMarked
                            )
                        }
                    }
                    return@withContext wishlistVisitable
                } catch (e: Throwable){
                    return@withContext wishlistVisitable
                }
            }

    /**
     * Void [getRecommendationWishlist]
     * @param page pageNumber
     * @return List of WishlistDataModel
     */
    private suspend fun getRecommendationWishlist(wishlistVisitable: List<WishlistDataModel>, page: Int, productIds: List<String>, recomIndex: Int): List<WishlistDataModel> =
            withContext(wishlistCoroutineDispatcherProvider.io){
                try{
                    val recommendationData = getRecommendationUseCase.getData(
                            GetRecommendationRequestParam(
                                    pageNumber = page,
                                    productIds = productIds,
                                    pageName = WISHLIST_PAGE_NAME
                            )
                    )
                    if(recommendationData.isNotEmpty()) {
                        return@withContext recommendationData.mappingRecommendationToWishlist(
                                currentPage = page,
                                wishlistVisitable = wishlistVisitable,
                                recommendationPositionInPage = recomIndex,
                                maxItemInPage = maxItemInPage,
                                isInBulkMode = isInBulkMode.value ?: false,
                                listRecommendationCarouselOnMarked = listRecommendationCarouselOnMarked
                        )
                    }
                    return@withContext wishlistVisitable
                } catch (e: Throwable){
                    return@withContext wishlistVisitable
                }
            }

    /**
     * Void [getTopAdsBannerData]
     */
    private suspend fun getTopAdsBannerData(wishlistVisitable: List<WishlistDataModel>, currentPage: Int, productIds: List<String>, topAdsIndex: Int): List<WishlistDataModel>{
        return withContext(wishlistCoroutineDispatcherProvider.io){
            try{
                if(wishlistVisitable.isNotEmpty()) {
                    val recommendationPositionInPreviousPage = ((currentPage - 3) * maxItemInPage) + recommendationPositionInPage
                    var pageToken = ""
                    if(recommendationPositionInPreviousPage >= 0 && wishlistVisitable.getOrNull(recommendationPositionInPreviousPage) is BannerTopAdsDataModel){
                        pageToken = (wishlistVisitable[recommendationPositionInPreviousPage] as BannerTopAdsDataModel).topAdsDataModel.nextPageToken ?: ""
                    }
                    val results = topAdsImageViewUseCase.getImageData(
                            topAdsImageViewUseCase.getQueryMap(
                                    "",
                                    "6",
                                    pageToken,
                                    1,
                                    3,
                                    ""
                            )
                    )
                    if (results.isNotEmpty()) {
                        return@withContext wishlistVisitable.mappingTopadsBannerToWishlist(
                                topadsBanner = results.first(),
                                recommendationPositionInPage= recommendationPositionInPage,
                                currentPage = currentPage,
                                isInBulkMode = isInBulkMode.value ?: false,
                                listRecommendationCarouselOnMarked = listRecommendationCarouselOnMarked,
                                maxItemInPage = maxItemInPage
                        )
                    } else {
                        return@withContext getRecommendationWishlist(
                                wishlistVisitable = wishlistVisitable,
                                page = currentPage,
                                productIds = productIds,
                                recomIndex = topAdsIndex
                        )
                    }
                }
                return@withContext wishlistVisitable
            } catch (e: Throwable){
                return@withContext wishlistVisitable
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
    open fun updateRecommendationItemWishlist(productId: Int, parentPosition: Int, childPosition: Int, newWishlistState: Boolean){
        val newWishlistData: MutableList<WishlistDataModel> = wishlistData.value.copy()

        if (parentPosition == DEFAULT_PARENT_POSITION_EMPTY_RECOM) {
            val newWishlistDataModel = newWishlistData.getOrNull(childPosition)
            if (newWishlistDataModel is RecommendationItemDataModel &&
                    newWishlistDataModel.recommendationItem.productId == productId) {
                val newRecomItem = newWishlistDataModel.recommendationItem.copy(isWishlist = newWishlistState)
                newWishlistData[childPosition] = newWishlistDataModel.copy(recommendationItem = newRecomItem)
                wishlistData.value = newWishlistData
            }
        } else {
            val newCarouselDataModel = newWishlistData.getOrNull(parentPosition)
            if (newCarouselDataModel is RecommendationCarouselDataModel) {
                val oldRecomItemDataModel = newCarouselDataModel.list.getOrNull(childPosition)

                if (oldRecomItemDataModel?.recommendationItem?.productId != productId) return

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
        val newWishlistData: MutableList<SmartVisitable<*>> = wishlistData.value.toMutableList()
        if(parentPosition != -1) {
            (newWishlistData.getOrNull(parentPosition) as? RecommendationCarouselDataModel)?.let {
                if(!currentWishlistState){
                    val recommendationDataModel = (newWishlistData[parentPosition] as RecommendationCarouselDataModel).list[childPosition]
                    addWishlistForRecommendationItem(
                            recommendationDataModel.recommendationItem.productId.toString(),
                            recommendationDataModel.recommendationItem.isWishlist,
                            parentPosition,
                            childPosition)
                } else {
                    val recommendationDataModel = (newWishlistData[parentPosition] as RecommendationCarouselDataModel).list[childPosition]
                    removeWishlistForRecommendationItem(recommendationDataModel.recommendationItem.productId.toString(),
                            recommendationDataModel.recommendationItem.isWishlist,
                            parentPosition,
                            childPosition)
                }
            }
        } else {
            (newWishlistData.getOrNull(childPosition) as? RecommendationItemDataModel)?.let { recommendationDataModel ->
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
    }

    /**
     * Void [setEmptyWishlistRecommendationItemWishlist]
     * @param recommendationPosition recommendation item data position in wishlist recyclerView
     * @param currentWishlistState current wishlist state of selected product
     *
     * This function will take action based on wishlist current statue
     * Current status = false -> means this function will do add wishlist action
     * Current status = true -> means this function will do remove wishlist action
     * result will be notified to addWishlistRecommendationActionData or removeWishlistRecommendationActionData
     */
    fun setEmptyWishlistRecommendationItemWishlist(recommendationPosition: Int,
                                                   currentWishlistState: Boolean){
        val newWishlistData: MutableList<SmartVisitable<*>> = wishlistData.value.toMutableList()
        val recommendationVisitable = newWishlistData.getOrNull(recommendationPosition)

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
            listOfPosition.forEachIndexed { _, it ->
                wishlistData.value[it].run {
                    if (this is WishlistItemDataModel) {
                        listForBulkRemoveCandidate[this.productItem.id] = this
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
                            bulkSelectCountActionData.value = Event(0)

                            //update bulk mode first, so view can have empty state listener properly

                            //check is list is empty
                            if (updatedList.isEmpty()) {
                                updateBulkMode(false)
                                wishlistData.value = listOf(EmptyWishlistDataModel())
                                getRecommendationOnEmptyWishlist(0)
                                wishlistState.value = Status.EMPTY
                            } else {
                                wishlistData.value = updatedList
                                updateBulkMode(false)
                            }

                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable) {
                            bulkSelectCountActionData.value = Event(0)
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
            responseList: WishlistActionData,
            listForBulkRemoveCandidate: HashMap<String, WishlistItemDataModel>
            ): Triple<List<WishlistDataModel>, List<String>, Boolean> {
        val updatedList = mutableListOf<WishlistDataModel>()
        val newWishlistDataValue = wishlistData.value.copy()
        val recommendationCarouselDataModels = mutableListOf<WishlistDataModel>()
        val deletedIds = mutableListOf<String>()

        //save carousel instance temporarily
        for(i in recommendationPositionInPage until newWishlistDataValue.size-1 step maxItemInPage) {
            if (newWishlistDataValue[i] is RecommendationCarouselDataModel) {
                recommendationCarouselDataModels.add(newWishlistDataValue[i])
                newWishlistDataValue.removeAt(i)
            }
        }

        val ids = responseList.productId.split(",")
        ids.forEach { id ->
            val wishlistDataModel = listForBulkRemoveCandidate[id]
            listForBulkRemoveCandidate.remove(id)

            wishlistDataModel?.let {
                newWishlistDataValue.remove(wishlistDataModel)
                deletedIds.add(id)
            }
        }

        //bring back carousel
        var recomIndex = 4
        recommendationCarouselDataModels.forEach {
            newWishlistDataValue.add(recomIndex, it)
            recomIndex+=maxItemInPage
        }

        val isPartiallyFailed = listForBulkRemoveCandidate.isNotEmpty()

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
        wishlistData.value.getOrNull(position)?.let { selectedVisitable ->
            if (selectedVisitable is WishlistItemDataModel) {
                val productId = selectedVisitable.productItem.id
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
                                if (updatedList.isEmpty()) updatedList.add(EmptyWishlistDataModel())
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
        val newVisitable: MutableList<WishlistDataModel> = wishlistData.value.toMutableList().copy()
        for (i in 0 until newVisitable.size){
            when (val dataModel = newVisitable[i]) {
                is WishlistItemDataModel -> {
                    newVisitable[i] = dataModel.copy(isOnBulkRemoveProgress = isBulkMode)
                }
                is RecommendationCarouselDataModel -> {
                    if (isBulkMode) {
                        listRecommendationCarouselOnMarked[i] = newVisitable[i] as RecommendationCarouselDataModel
                    }
                }
                is BannerTopAdsDataModel -> {
                    if(isBulkMode) listRecommendationCarouselOnMarked[i] = newVisitable[i]
                }
            }
        }

        val listOfRecommendations = listRecommendationCarouselOnMarked.values

        if(!isBulkMode) {
            val sortFirst = listRecommendationCarouselOnMarked.toSortedMap()
            sortFirst.forEach{
                if (it.key <= newVisitable.size && it.key >= 0) {
                    newVisitable.add(it.key, it.value)
                }
            }
            clearAllMarkedWishlistItem()
        } else {
            newVisitable.removeAll(listOfRecommendations)
        }

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
            it.value.isOnChecked = false
        }
        listVisitableMarked.clear()
        listRecommendationCarouselOnMarked.clear()
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
        (wishlistDataTemp.getOrNull(productPosition) as? WishlistItemDataModel)?.let {
            val wishlistItemCheckUpdate = it.copy(isOnChecked = isChecked)
            wishlistDataTemp[productPosition] = wishlistItemCheckUpdate
            if(isChecked) {
                listVisitableMarked[productPosition] = wishlistItemCheckUpdate
            }
            else {
                listVisitableMarked.remove(productPosition)
            }
            bulkSelectCountActionData.value = Event(listVisitableMarked.size)
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
                updateRecommendationItemWishlist(productId?.toInt()?:-1, parentPosition, childPosition, !currentWishlistState)
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
                updateRecommendationItemWishlist(productId?.toInt()?:-1, parentPosition, childPosition, !currentWishlistState)
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

    fun onProductClick(productId: Int, parentPosition: Int, position: Int) {
        this.tempSelectedParentPositionInPDP = parentPosition
        this.tempSelectedPositionInPdp = position
        this.tempSelectedProductIdInPdp = productId

        productClickActionData.value = Event(
                ProductClickActionData(
                        parentPosition = parentPosition,
                        position = position,
                        productId = productId
                )
        )
    }

    fun onPDPActivityResultForWishlist(productId: Int, wishlistState: Boolean) {
        if (tempSelectedParentPositionInPDP != null &&
                tempSelectedPositionInPdp != null &&
                tempSelectedProductIdInPdp != null) {
            updateRecommendationItemWishlist(
                    productId,
                    tempSelectedParentPositionInPDP!!,
                    tempSelectedPositionInPdp!!,
                    wishlistState)
        }
    }

    private fun removeLoadMore(): List<WishlistDataModel>{
        val list = wishlistData.value.copy()
        list.removeAll { it is LoadMoreDataModel }
        return list
    }

    fun getUserId() = userSessionInterface.userId ?: ""

    companion object{
        private const val DEFAULT_PARENT_POSITION_EMPTY_RECOM = -1
        private const val WISHLIST_PAGE_NAME = "wishlist"
    }
}