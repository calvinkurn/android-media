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
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.response.DeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COUNT_MANAGE_ROW
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_PAGE_NAME
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment.Companion.ATC_WISHLIST
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class WishlistV2ViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                            private val wishlistV2UseCase: WishlistV2UseCase,
                                            private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
                                            private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
                                            private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
                                            private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
                                            private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2 = MutableLiveData<Result<WishlistV2Response.Data.WishlistV2>>()
    val wishlistV2: LiveData<Result<WishlistV2Response.Data.WishlistV2>>
        get() = _wishlistV2

    private val _wishlistV2Data = MutableLiveData<Result<List<WishlistV2TypeLayoutData>>>()
    val wishlistV2Data: LiveData<Result<List<WishlistV2TypeLayoutData>>>
        get() = _wishlistV2Data

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

    private var sizeList = 0

    fun loadWishlistV2(params: WishlistV2Params, typeLayout: String?) {
        launch {
            try {
                val wishlistV2Response = wishlistV2UseCase.executeSuspend(params).wishlistV2
                _wishlistV2.value = Success(wishlistV2Response)
                _wishlistV2Data.value = Success(organizeWishlistV2Data(wishlistV2Response, typeLayout))
            } catch (e: Exception) {
                _wishlistV2.value = Fail(e)
                _wishlistV2Data.value = Fail(e)
            }
        }
    }

    fun loadRecommendation(page: Int) {
        val listData = arrayListOf<WishlistV2TypeLayoutData>()
        launch {
            try {
                val recommItems = getRecommendationWishlistV2(page, listOf(), EMPTY_WISHLIST_PAGE_NAME)
                recommItems.recommendationData.forEach { item ->
                    listData.add(WishlistV2TypeLayoutData(item, TYPE_RECOMMENDATION_LIST))
                }
                _wishlistV2Data.value = Success(listData)
            } catch (e: Exception) {
                _wishlistV2Data.value = Fail(e)
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

    private suspend fun organizeWishlistV2Data(wishlistV2Response: WishlistV2Response.Data.WishlistV2, typeLayout: String?) : List<WishlistV2TypeLayoutData> {
        val currSizeList = wishlistV2Response.items.size
        sizeList += currSizeList
        val diffArray = arrayListOf<Int>()
        diffArray.add(sizeList)

        var listData = arrayListOf<WishlistV2TypeLayoutData>()

        // empty wishlist
        if (wishlistV2Response.items.isEmpty() && wishlistV2Response.page == 1) {
            when {
                wishlistV2Response.sortFilters.isNotEmpty() -> {
                    val wishlistV2Empty = WishlistV2EmptyStateData(R.string.empty_state_img, R.string.empty_state_desc_1, R.string.empty_state_title, R.string.empty_state_button)
                    listData.add(WishlistV2TypeLayoutData(wishlistV2Empty, WishlistV2Consts.TYPE_EMPTY_STATE))
                }
                wishlistV2Response.query.isEmpty() -> {
                    listData.add(WishlistV2TypeLayoutData("", WishlistV2Consts.TYPE_EMPTY_STATE_CAROUSEL))
                }
                else -> {
                    listData.add(WishlistV2TypeLayoutData(wishlistV2Response.query, WishlistV2Consts.TYPE_EMPTY_NOT_FOUND))
                }
            }
            val recommItems = getRecommendationWishlistV2(1, listOf(), EMPTY_WISHLIST_PAGE_NAME)
            listData.add(WishlistV2TypeLayoutData(recommItems.title, TYPE_RECOMMENDATION_TITLE))
            recommItems.recommendationData.forEach { item ->
                listData.add(WishlistV2TypeLayoutData(item, TYPE_RECOMMENDATION_LIST))
            }

        } else {
            // only for wishlist size < 4
            if (wishlistV2Response.page == 1 && !wishlistV2Response.hasNextPage) {
                listData = mapToProductCardList(wishlistV2Response.items, typeLayout)

                val countManageRowData = WishlistV2CountManageRowData(wishlistV2Response.totalData)
                listData.add(0, WishlistV2TypeLayoutData(countManageRowData, TYPE_COUNT_MANAGE_ROW))
                when {
                    // if user has 0-3 products, recom widget is at the bottom of the page (vertical/infinite scroll)
                    wishlistV2Response.totalData < topAdsPositionInPage -> {
                        // listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                        val recommItems = getRecommendationWishlistV2(1, listOf(), WISHLIST_PAGE_NAME)
                        listData.add(WishlistV2TypeLayoutData(recommItems.title, TYPE_RECOMMENDATION_TITLE))
                        listData.add(WishlistV2TypeLayoutData(recommItems, TYPE_RECOMMENDATION_CAROUSEL))
                    }

                    // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                    wishlistV2Response.totalData == topAdsPositionInPage -> {
                        // listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                        listData.add(WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))
                        val recommItems = getRecommendationWishlistV2(1, listOf(), WISHLIST_PAGE_NAME)
                        listData.add(WishlistV2TypeLayoutData(recommItems.title, TYPE_RECOMMENDATION_TITLE))
                        listData.add(WishlistV2TypeLayoutData(recommItems, TYPE_RECOMMENDATION_CAROUSEL))
                    }

                    // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                    wishlistV2Response.totalData > topAdsPositionInPage -> {
                        // listData = mapToProductCardList(wishlistV2Response.items, typeLayout)
                        listData.add(topAdsPositionInPage, WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))
                        val recommItems = getRecommendationWishlistV2(1, listOf(), WISHLIST_PAGE_NAME)
                        listData.add(WishlistV2TypeLayoutData(recommItems.title, TYPE_RECOMMENDATION_TITLE))
                        listData.add(WishlistV2TypeLayoutData(recommItems, TYPE_RECOMMENDATION_CAROUSEL))
                    }
                }
            } else {
                listData = mapToProductCardList(wishlistV2Response.items, typeLayout)

                if (wishlistV2Response.page == 1) {
                    val countManageRowData = WishlistV2CountManageRowData(wishlistV2Response.totalData)
                    listData.add(0, WishlistV2TypeLayoutData(countManageRowData, TYPE_COUNT_MANAGE_ROW))
                }
                // has next page
                /*if (params.page == 1 ) {
                    listData.add(topAdsPositionInPage, WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))
                }*/

                if (wishlistV2Response.totalData >= topAdsPositionInPage && wishlistV2Response.page % 2 == 0) {
                    val recommItems = getRecommendationWishlistV2(1, listOf(), WISHLIST_PAGE_NAME)
                    listData.add(topAdsPositionInPage, WishlistV2TypeLayoutData(recommItems.title, TYPE_RECOMMENDATION_TITLE))
                    listData.add(topAdsPositionInPage+1, WishlistV2TypeLayoutData(recommItems, TYPE_RECOMMENDATION_CAROUSEL))
                } else {
                    listData.add(topAdsPositionInPage, WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))
                }

                // TBD
                /*for (i in 0 until sizeList) {
                    // has recommendation item
                    if (i>0 && i%termsRecom == 0) {
                        val pageRecom = i.floorDiv(termsRecom)
                        val recommItem = getRecommendationWishlistV2(pageRecom, listOf(), WISHLIST_PAGE_NAME)

                        println("++ recomm - diff = ${diffArray[diffArray.size-2]}, i = $i")
                        *//*listData.add(i, WishlistV2TypeLayoutData(recommItem.title, TYPE_RECOMMENDATION_TITLE))
                        listData.add(i+1, WishlistV2TypeLayoutData(recommItem, TYPE_RECOMMENDATION_CAROUSEL))*//*

                    // has topads item
                    } else if (params.page> 1 && i>0 && (i-5) % termsTopads == 0) {
                        println("++ topads - diff = ${diffArray[diffArray.size-2]}, i = $i")
                        // listData.add(i, WishlistV2TypeLayoutData(getTopAdsData(""), TYPE_TOPADS))
                    }
                }*/
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

    suspend fun getRecommendationWishlistV2(page: Int, productIds: List<String>, pageName: String): WishlistV2RecommendationDataModel {
        val recommendation = singleRecommendationUseCase.getData(GetRecommendationRequestParam(
                        pageNumber = page,
                        productIds = productIds,
                        pageName = pageName))
        return WishlistV2RecommendationDataModel(recommendation.recommendationItemList, recommendation.title)
    }

    suspend fun getTopAdsData(pageToken: String = ""): TopAdsImageViewModel  {
        return topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap("",
                WISHLIST_TOPADS_SOURCE, pageToken, WISHLIST_TOPADS_ADS_COUNT, WISHLIST_TOPADS_DIMENS, "")).first()
    }

    companion object {
        private const val topAdsPositionInPage = 4
        private const val termsRecom = 26
        private const val termsTopads = 21
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3
        private const val EMPTY_WISHLIST_PAGE_NAME = "empty_wishlist"
    }
}