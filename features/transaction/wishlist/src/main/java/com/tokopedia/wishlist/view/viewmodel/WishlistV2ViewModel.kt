package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.DeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.ext.*
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_PAGE_NAME
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment.Companion.ATC_WISHLIST
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WishlistV2ViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                            private val wishlistV2UseCase: WishlistV2UseCase,
                                            private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
                                            private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
                                            private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
                                            private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
                                            private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<List<WishlistV2Data>>>()
    val wishlistV2Result: LiveData<Result<List<WishlistV2Data>>>
        get() = _wishlistV2Result

    private val _wishlistV2Filters = MutableLiveData<List<WishlistV2Response.Data.WishlistV2.SortFiltersItem>>()
    val wishlistV2Filters: LiveData<List<WishlistV2Response.Data.WishlistV2.SortFiltersItem>>
        get() = _wishlistV2Filters

    private val _wishlistV2TotalData = MutableLiveData<Int>()
    val wishlistV2TotalData: LiveData<Int>
        get() = _wishlistV2TotalData

    private val _wishlistV2HasNextPage = MutableLiveData<Boolean>()
    val wishlistV2HasNextPage: LiveData<Boolean>
        get() = _wishlistV2HasNextPage

    private val _wishlistV2Data = MutableLiveData<Result<List<WishlistV2TypeLayoutData>>>()
    val wishlistV2Data: LiveData<Result<List<WishlistV2TypeLayoutData>>>
        get() = _wishlistV2Data

    private val _wishlistData = MutableLiveData<WishlistV2Response.Data.WishlistV2>()
    val wishlistData: LiveData<WishlistV2Response.Data.WishlistV2>
        get() = _wishlistData

    private val _deleteWishlistV2Result =
        MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result

    private val _bulkDeleteWishlistV2Result = MutableLiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>()
    val bulkDeleteWishlistV2Result: LiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>
        get() = _bulkDeleteWishlistV2Result

    private val _atcResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcResult

    fun loadWishlistV2(params: WishlistV2Params, typeLayout: String?) {
        launch {
            try {
                val wishlistV2Response = wishlistV2UseCase.executeSuspend(params)
                _wishlistV2Filters.value = wishlistV2Response.wishlistV2.sortFilters
                _wishlistV2TotalData.value = wishlistV2Response.wishlistV2.totalData
                _wishlistV2HasNextPage.value = wishlistV2Response.wishlistV2.hasNextPage
                _wishlistV2Data.value = Success(organizeWishlistV2Data(wishlistV2Response.wishlistV2, params, typeLayout))
            } catch (e: Exception) {
                _wishlistV2Data.value = Fail(e)
            }
        }
    }

    private suspend fun organizeWishlistV2Data(wishlistV2Response: WishlistV2Response.Data.WishlistV2, params: WishlistV2Params, typeLayout: String?) : List<WishlistV2TypeLayoutData> {
        var listData = arrayListOf<WishlistV2TypeLayoutData>()
        if (wishlistV2Response.items.isEmpty() && params.page == 1) {
            if (wishlistV2Response.query.isEmpty()) {
                listData.add(WishlistV2TypeLayoutData("", WishlistV2Consts.TYPE_EMPTY_STATE))
            } else {
                listData.add(WishlistV2TypeLayoutData(wishlistV2Response.query, WishlistV2Consts.TYPE_EMPTY_NOT_FOUND))
            }
            val recommItems = getRecommendationWishlistV2(0, listOf(), EMPTY_WISHLIST_PAGE_NAME)
            listData.add(WishlistV2TypeLayoutData(recommItems.title, TYPE_RECOMMENDATION_TITLE))
            recommItems.recommendationData.forEach { item ->
                listData.add(WishlistV2TypeLayoutData(item, TYPE_RECOMMENDATION_LIST))
            }

        } else {
            if (params.page == 1) {
                when {
                    // if user has 0-3 products, recom widget is at the bottom of the page (vertical/infinite scroll)
                    wishlistV2Response.totalData < topAdsPositionInPage -> {
                        listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                        listData.add(WishlistV2TypeLayoutData(getRecommendationWishlistV2(0, listOf(), WISHLIST_PAGE_NAME), TYPE_RECOMMENDATION_CAROUSEL))

                        // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                    }
                    wishlistV2Response.totalData == topAdsPositionInPage -> {
                        listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                        listData.add(WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))

                        // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                    }
                    wishlistV2Response.totalData > topAdsPositionInPage -> {
                        listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                        listData.add(topAdsPositionInPage+1, WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))
                    }
                }
            } else {
                // next page
                if (wishlistV2Response.totalData >= topAdsPositionInPage && params.page % 2 == 0) {
                    listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                    listData.add(topAdsPositionInPage, WishlistV2TypeLayoutData(getRecommendationWishlistV2(params.page, listOf(), WISHLIST_PAGE_NAME), TYPE_RECOMMENDATION_CAROUSEL))

                } else {
                    listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                    listData.add(topAdsPositionInPage, WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))
                }
            }
        }
        return listData
    }

    private fun mapToProductCardList(items: List<WishlistV2Response.Data.WishlistV2.Item>, typeLayout: String?) : ArrayList<WishlistV2TypeLayoutData> {
        val listItem = arrayListOf<WishlistV2TypeLayoutData>()
        items.forEach { item ->
            val listGroupLabel = arrayListOf<ProductCardModel.LabelGroup>()

            item.labelGroup.forEach { labelGroupItem ->
                val labelGroup = ProductCardModel.LabelGroup(
                        position = labelGroupItem.position,
                        title = labelGroupItem.title,
                        type = labelGroupItem.type,
                        imageUrl = labelGroupItem.url)
                listGroupLabel.add(labelGroup)
            }

            val isButtonAtc = item.buttons.primaryButton.action == ATC_WISHLIST

            val productModel = ProductCardModel(
                    productImageUrl = item.imageUrl,
                    isWishlistVisible = true,
                    productName = item.name,
                    shopName = item.shop.name,
                    formattedPrice = item.priceFmt,
                    shopLocation = item.shop.location,
                    isShopRatingYellow = true,
                    hasSecondaryButton = true,
                    tambahKeranjangButton = isButtonAtc,
                    lihatBarangSerupaButton = !isButtonAtc,
                    labelGroupList = listGroupLabel)
            listItem.add(WishlistV2TypeLayoutData(productModel, typeLayout, item))
        }
        return listItem
    }

    fun loadExisting(params: WishlistV2Params) {
        launch(dispatcher.main) {
            try {
                val wishlistData = wishlistV2UseCase.executeSuspend(params).wishlistV2
                if (wishlistData.items.isEmpty() && params.page == 1) {
                    _wishlistV2Result.value =
                        Success(
                            listOf(
                                WishlistV2EmptyDataModel(query = wishlistData.query),
                                    getRecommendationData(0, listOf(), EMPTY_WISHLIST_PAGE_NAME, false)
                            )
                        )
                } else {
                    // TODO : harusnya ga perlu double variable data begini untuk observing..
                    _wishlistData.value = wishlistData
                    val visitableWishlist = wishlistData.items.mappingWishlistToVisitable()
                    _wishlistV2Result.value = Success(
                        organizeWishlistV2Data1(
                            visitableWishlist,
                            params.page,
                            wishlistData.items.map { it.id },
                            hasNextPage = wishlistData.hasNextPage,
                            totalData = wishlistData.totalData
                        )
                    )
                }
            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e)
            }
        }
    }

    fun deleteWishlistV2(productId: String, userId: String) {
        launch {
            _deleteWishlistV2Result.value =
                deleteWishlistV2UseCase.executeSuspend(productId, userId)
        }
    }

    fun bulkDeleteWishlistV2(listProductId: List<String>, userId: String) {
        launch {
            _bulkDeleteWishlistV2Result.value = bulkDeleteWishlistV2UseCase.executeSuspend(listProductId, userId)
        }
    }

    fun getRecommendation(page: Int, pageName: String, productIds: List<String>) {
        launch {
            try {
                _wishlistV2Result.value =
                    Success(listOf(getRecommendationData(page, productIds, pageName, false)))
            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e.fillInStackTrace())
            }
        }
    }

    fun getNextPageWishlistData(param: WishlistV2Params) {
        launch {
            try {
                val data = wishlistV2UseCase.executeSuspend(param).wishlistV2

                val previousData = if (_wishlistV2Result.value is Success) {
                    (_wishlistV2Result.value as Success<List<WishlistV2Data>>).data.toMutableList()
                } else {
                    mutableListOf()
                }

                // TODO : harusnya ga perlu combine, perlu cek logic nya lagi kenapa ga bisa pakai append...
                // mappingWishlistToVisitable = hanya ubah jadi toMutableList... hmm
                // val newPageVisitableData = previousData.combineVisitable(data.items.mappingWishlistToVisitable())
                val newPageVisitableData = data.items.mappingWishlistToVisitable()

                        _wishlistV2Result.value = Success(
                    organizeWishlistV2Data1(
                        newPageVisitableData,
                        param.page,
                        data.items.map { it.id },
                        hasNextPage = data.hasNextPage,
                        totalData = data.totalData)
                )

            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e.fillInStackTrace())
            }
        }
    }

    private suspend fun organizeWishlistV2Data1(
        wishlistVisitable: MutableList<WishlistV2Data>,
        page: Int,
        productIds: List<String>,
        hasNextPage: Boolean,
        totalData: Int
    ): List<WishlistV2Data> {
        return withContext(dispatcher.io) {
//            if user has 0-3 products, recom widget is at the bottom of the page (vertical/infinite scroll)
            if (wishlistVisitable.size < maxItemInPage) {
                val recommendationResult = getRecommendationData(
                    page = page,
                    productIds = productIds,
                    pageName = WISHLIST_PAGE_NAME,
                    isCarousel = true
                )
                wishlistVisitable.add(
                    recommendationResult
                )
            }
            else {
//                If user has >24 products → follow normal rules, banner ads is after 4th products, recom widget after 24th product
                // topAdsPositionInPage = 4
                if (wishlistVisitable.size >= topAdsPositionInPage) {
                    if (wishlistVisitable.size == topAdsPositionInPage) {
                        wishlistVisitable.add(getTopAdsData1(""))
                    } else {
                        if (wishlistVisitable[topAdsPositionInPage] !is WishlistV2TopAdsDataModel) {
                            wishlistVisitable.add(topAdsPositionInPage, getTopAdsData1(""))
                        }
                    }
                }
                if (wishlistVisitable.size >= recommendationPositionInPage) {
                    if (wishlistVisitable.size == recommendationPositionInPage) {
                        wishlistVisitable.add(
                            getRecommendationData(page, productIds, WISHLIST_PAGE_NAME, true)
                        )
                    } else {
                        if (wishlistVisitable[recommendationPositionInPage] !is WishlistV2RecommendationDataModel) {
                            wishlistVisitable.add(
                                recommendationPositionInPage,
                                getRecommendationData(page, productIds, WISHLIST_PAGE_NAME, true)
                            )
                        }

                    }
                }
            }
//            if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
            val lastRecommendationData = checkIfWishlistLessThanMinimalItem(
                hasNextPage = hasNextPage,
                page = page,
                productIds = productIds,
                totalData = totalData
            )
            if (lastRecommendationData != null) {
                wishlistVisitable.add(lastRecommendationData)
            }
            wishlistVisitable
        }
    }

    /*fun onCheckedBulkDeleteWishlist(productId: String, isChecked: Boolean) {
        val previousData = if (_wishlistV2Result.value is Success) {
            (_wishlistV2Result.value as Success<List<WishlistV2Data>>).data.toMutableList()
        } else {
            mutableListOf()
        }
        val selectedProductIndex = previousData.indexOfFirst { element -> element is WishlistV2DataModel && element.item.id == productId }
        if (selectedProductIndex != -1) {
            (previousData[selectedProductIndex] as WishlistV2DataModel).isChecked = isChecked
            _wishlistV2Result.value = Success(previousData)
        }
    }*/

    fun doAtc(atcParams: AddToCartRequestParams) {
        launch {
            try {
                atcUseCase.setParams(atcParams)
                val result = atcUseCase.executeOnBackground()
                _atcResult.value = Success(result)
            } catch (e: Exception) {
                Timber.d(e)
                _atcResult.value = Fail(e)
            }
        }
    }

    private suspend fun getRecommendationWishlistV2(page: Int, productIds: List<String>, pageName: String): WishlistV2RecommendationDataModel {
        val recommendation = singleRecommendationUseCase.getData(GetRecommendationRequestParam(
                        pageNumber = page,
                        productIds = productIds,
                        pageName = pageName))
        return WishlistV2RecommendationDataModel(recommendation.recommendationItemList, recommendation.title)
    }

    private suspend fun getRecommendationData(
        page: Int,
        productIds: List<String>, pageName: String, isCarousel: Boolean
    ): WishlistV2RecommendationDataModel = withContext(dispatcher.io) {
        val widget = singleRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                        pageNumber = page,
                        productIds = productIds,
                        pageName = pageName
                )
        )
        return@withContext WishlistV2RecommendationDataModel(widget.recommendationItemList, widget.title)
    }

    private suspend fun getTopAdsData(pageToken: String = ""): TopAdsImageViewModel  {
        return topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap("",
                WISHLIST_TOPADS_SOURCE, pageToken, WISHLIST_TOPADS_ADS_COUNT, WISHLIST_TOPADS_DIMENS, "")).first()
    }

    private suspend fun getTopAdsData1(
        pageToken: String = ""
    ): WishlistV2TopAdsDataModel = withContext(dispatcher.io) {
        return@withContext WishlistV2TopAdsDataModel(
            topAdsImageViewUseCase.getImageData(
                topAdsImageViewUseCase.getQueryMap(
                    "",
                    WISHLIST_TOPADS_SOURCE,
                    pageToken,
                    WISHLIST_TOPADS_ADS_COUNT,
                    WISHLIST_TOPADS_DIMENS,
                    ""
                )
            ).first()
        )
    }

    private suspend fun checkIfWishlistLessThanMinimalItem(
        totalData: Int,
        hasNextPage: Boolean,
        page: Int,
        productIds: List<String>
    ): WishlistV2RecommendationDataModel? = withContext(dispatcher.io) {
        if ((totalData in (maxItemInPage + 1) until newMinItemRegularRule) && !hasNextPage) {
            return@withContext getRecommendationData(page, productIds, WISHLIST_PAGE_NAME, true)
        } else return@withContext null
    }

    companion object {
        private const val recommendationPositionInPage = 25
        private const val topAdsPositionInPage = 4
        private const val maxItemInPage = 4
        private const val newMinItemRegularRule = 24
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3
        private const val EMPTY_WISHLIST_PAGE_NAME = "empty_wishlist"

        private const val RECOM_INDEX_STARTER = 4
        private const val RECOM_INDEX_STARTER_MINUS = 3
    }
}