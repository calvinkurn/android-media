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
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
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
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_PAGE_NAME
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment.Companion.ATC_WISHLIST
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class WishlistV2ViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val wishlistV2UseCase: WishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
    private val atcUseCase: AddToCartUseCase,
    private val irisSession: TopAdsIrisSession
) : BaseViewModel(dispatcher.main) {

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
                recommItems.recommendationProductCardModelData.forEach { item ->
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

    suspend fun organizeWishlistV2Data(wishlistV2Response: WishlistV2Response.Data.WishlistV2, typeLayout: String?) : List<WishlistV2TypeLayoutData> {
        var listData = arrayListOf<WishlistV2TypeLayoutData>()

        var isFilterActive = false
        for (itemFilter in wishlistV2Response.sortFilters) {
            if (itemFilter.isActive) isFilterActive = true
        }

        // empty wishlist
        if (wishlistV2Response.items.isEmpty() && wishlistV2Response.page == 1) {
            listData = mapToEmptyState(wishlistV2Response, listData, isFilterActive)

        } else {
            // only for wishlist which has 1 page response
            if (wishlistV2Response.page == 1 && !wishlistV2Response.hasNextPage) {
                listData = mapToProductCardList(wishlistV2Response.items, typeLayout)

                when {
                    // if user has 0-3 products, recom widget is at the bottom of the page (vertical/infinite scroll)
                    wishlistV2Response.totalData < recommPosition -> {
                        listData = mapToRecommendation(0, listData)
                    }

                    // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                    wishlistV2Response.totalData == recommPosition -> {
                        mapToTopads(0, listData)
                        mapToRecommendation(0, listData)
                    }

                    // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                    wishlistV2Response.totalData > recommPosition -> {
                        mapToTopads(recommPosition, listData)
                        mapToRecommendation(0, listData)
                    }
                }
            } else {
                listData = mapToProductCardList(wishlistV2Response.items, typeLayout)

                if (wishlistV2Response.page == 1) {
                    mapToTopads(recommPosition, listData)
                } else {
                    if (wishlistV2Response.items.size >= recommPosition) {
                        if (wishlistV2Response.page % 2 == 0) {
                            mapToRecommendation(recommPosition, listData)
                        } else {
                            mapToTopads(recommPosition, listData)
                        }
                    } else {
                        mapToRecommendation(0, listData)
                    }
                }
            }
        }
        return listData
    }

    suspend fun mapToEmptyState(wishlistV2Response: WishlistV2Response.Data.WishlistV2, listData: ArrayList<WishlistV2TypeLayoutData>, isFilterActive: Boolean): ArrayList<WishlistV2TypeLayoutData> {
        if (wishlistV2Response.query.isNotEmpty()) {
            listData.add(WishlistV2TypeLayoutData(wishlistV2Response.query, WishlistV2Consts.TYPE_EMPTY_NOT_FOUND))

        } else if (isFilterActive) {
            val wishlistV2Empty = WishlistV2EmptyStateData(R.string.empty_state_img, R.string.empty_state_desc, R.string.empty_state_title, R.string.empty_state_button)
            listData.add(WishlistV2TypeLayoutData(wishlistV2Empty, WishlistV2Consts.TYPE_EMPTY_STATE))

        } else if (!isFilterActive && wishlistV2Response.query.isEmpty()) {
            listData.add(WishlistV2TypeLayoutData("", WishlistV2Consts.TYPE_EMPTY_STATE_CAROUSEL))
        }
        val recomm = getRecommendationWishlistV2(1, listOf(), EMPTY_WISHLIST_PAGE_NAME)
        listData.add(WishlistV2TypeLayoutData(recomm.title, TYPE_RECOMMENDATION_TITLE))
        recomm.recommendationProductCardModelData.forEachIndexed { index, productCardModel ->
            if (recomm.listRecommendationItem.isNotEmpty()) {
                listData.add(WishlistV2TypeLayoutData(productCardModel, TYPE_RECOMMENDATION_LIST, recommItem = recomm.listRecommendationItem[index]))
            }
        }
        return listData
    }

    suspend fun mapToRecommendation(index:Int, listData: ArrayList<WishlistV2TypeLayoutData>): ArrayList<WishlistV2TypeLayoutData> {
        val recommItems = getRecommendationWishlistV2(1, listOf(), WISHLIST_PAGE_NAME)

        if (index > 0) {
            listData.add(index, WishlistV2TypeLayoutData(WishlistV2Consts.RECOMMENDED_FOR_YOU, TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
            listData.add(index+1, WishlistV2TypeLayoutData(recommItems, TYPE_RECOMMENDATION_CAROUSEL))
        } else {
            listData.add(WishlistV2TypeLayoutData(recommItems.title, TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
            listData.add(WishlistV2TypeLayoutData(recommItems, TYPE_RECOMMENDATION_CAROUSEL))
        }

        return listData
    }

    suspend fun mapToTopads(index: Int, listData: ArrayList<WishlistV2TypeLayoutData>): ArrayList<WishlistV2TypeLayoutData> {
        val topadsData = getTopAdsData()
        if (topadsData != null) {
            if (index > 0) {
                listData.add(index, WishlistV2TypeLayoutData(topadsData, TYPE_TOPADS))
            } else {
                listData.add(WishlistV2TypeLayoutData(topadsData, TYPE_TOPADS))
            }
        }
        return listData
    }

    fun mapToProductCardList(items: List<WishlistV2Response.Data.WishlistV2.Item>, typeLayout: String?) : ArrayList<WishlistV2TypeLayoutData> {
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

            val listBadge = arrayListOf<ProductCardModel.ShopBadge>()
            item.badges.forEach { badgesItem ->
                listBadge.add(ProductCardModel.ShopBadge(imageUrl = badgesItem.imageUrl))
            }

            val productModel = ProductCardModel(
                    productImageUrl = item.imageUrl,
                    productName = item.name,
                    shopName = item.shop.name,
                    formattedPrice = item.priceFmt,
                    shopLocation = item.shop.location,
                    isShopRatingYellow = true,
                    hasButtonThreeDotsWishlist = true,
                    hasAddToCartWishlist = isButtonAtc,
                    hasSimilarProductWishlist = !isButtonAtc,
                    labelGroupList = listGroupLabel,
                    shopBadgeList = listBadge,
                    discountPercentage = item.discountPercentageFmt,
                    countSoldRating = item.rating,
                    slashedPrice = item.originalPriceFmt,
                    freeOngkir = ProductCardModel.FreeOngkir(item.bebasOngkir.imageUrl.isNotEmpty(), item.bebasOngkir.imageUrl),
                    isOutOfStock = !item.available)
            listItem.add(WishlistV2TypeLayoutData(productModel, typeLayout, item))
        }
        return listItem
    }

    suspend fun getRecommendationWishlistV2(page: Int, productIds: List<String>, pageName: String): WishlistV2RecommendationDataModel {
        val recommendation = singleRecommendationUseCase.getData(GetRecommendationRequestParam(
                        pageNumber = page,
                        productIds = productIds,
                        pageName = pageName))
        return WishlistV2RecommendationDataModel(convertRecommendationIntoProductDataModel(recommendation.recommendationItemList),
                recommendation.recommendationItemList, recommendation.title)
    }

    fun convertRecommendationIntoProductDataModel(data: List<RecommendationItem>): List<ProductCardModel> {
        return data.map { element ->
            ProductCardModel(
                    slashedPrice = element.slashedPrice,
                    productName = element.name,
                    formattedPrice = element.price,
                    productImageUrl = element.imageUrl,
                    isTopAds = element.isTopAds,
                    discountPercentage = element.discountPercentage,
                    reviewCount = element.countReview,
                    ratingCount = element.rating,
                    shopLocation = element.location,
                    isWishlistVisible = true,
                    isWishlisted = element.isWishlist,
                    shopBadgeList = element.badgesUrl.map {
                        ProductCardModel.ShopBadge(imageUrl = it)
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = element.isFreeOngkirActive,
                            imageUrl = element.freeOngkirImageUrl
                    ),
                    labelGroupList = element.labelGroupList.map { recommendationLabel ->
                        ProductCardModel.LabelGroup(
                                position = recommendationLabel.position,
                                title = recommendationLabel.title,
                                type = recommendationLabel.type,
                                imageUrl = recommendationLabel.imageUrl
                        )
                    }
            )
        }
    }

    suspend fun getTopAdsData(): TopAdsImageViewModel?  {
        return topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap("",
                WISHLIST_TOPADS_SOURCE, "", WISHLIST_TOPADS_ADS_COUNT, WISHLIST_TOPADS_DIMENS, ""),
            irisSession.getSessionId()
        ).firstOrNull()
    }

    companion object {
        private const val recommPosition = 4
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3
        private const val EMPTY_WISHLIST_PAGE_NAME = "empty_wishlist"
    }
}