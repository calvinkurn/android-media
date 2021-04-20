package com.tokopedia.home_wishlist.viewModel

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.domain.GetWishlistParameter
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.mockk

fun createWishlistViewModel(
        userSessionInterface: UserSessionInterface = mockk(relaxed = true),
        getWishlistDataUseCase: GetWishlistDataUseCase = mockk(relaxed = true),
        getSingleRecommendationUseCase: GetSingleRecommendationUseCase = mockk(relaxed = true),
        getRecommendationUseCase: GetRecommendationUseCase = mockk(relaxed = true),
        removeWishlistUseCase: RemoveWishListUseCase = mockk(relaxed = true),
        addToCartUseCase: AddToCartUseCase = mockk(relaxed = true),
        bulkRemoveWishlistUseCase: BulkRemoveWishlistUseCase = mockk(relaxed = true),
        addWishListUseCase: AddWishListUseCase = mockk(relaxed = true),
        topAdsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true),
        updateCartCounterUseCase: UpdateCartCounterUseCase = mockk(relaxed = true)
): WishlistViewModel {

    return WishlistViewModel(
            userSessionInterface = userSessionInterface,
            getWishlistUseCase = getWishlistDataUseCase,
            getRecommendationUseCase = getRecommendationUseCase,
            getSingleRecommendationUseCase = getSingleRecommendationUseCase,
            wishlistCoroutineDispatcherProvider = CoroutineTestDispatchersProvider,
            removeWishListUseCase = removeWishlistUseCase,
            addToCartUseCase = addToCartUseCase,
            bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
            addWishListUseCase = addWishListUseCase,
            topAdsImageViewUseCase = topAdsImageViewUseCase,
            updateCartCounterUseCase = updateCartCounterUseCase
    )
}

fun GetSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(recommendationList: List<RecommendationItem>) {
    coEvery { getData(any()) } returns
            RecommendationWidget(
                    recommendationItemList = recommendationList
            )
}

fun GetRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(recommendationItems: List<RecommendationItem>) {
    coEvery {
        getData(any()) } returns
            listOf(
                    RecommendationWidget(
                            recommendationItemList = recommendationItems
                    )
            )
}

fun GetWishlistDataUseCase.givenGetWishlistDataReturnsThis(wishlistItems: List<WishlistItem> = listOf(),
                                                           keyword: String = "",
                                                           hasNextPage: Boolean = false,
                                                           page: Int = 1,
                                                           useDefaultWishlistItem: Boolean = false) {
    val defaultMaxPage = 20
    if (useDefaultWishlistItem) {
        val wishlistDefaultData = mutableListOf<WishlistItem>()
        var position = 1
        val id = ((page-1) * defaultMaxPage) + 1
        while (position <= defaultMaxPage) {
            wishlistDefaultData.add(WishlistItem(id=id.toString()))
            position++
        }
        coEvery { getData(GetWishlistParameter(keyword = keyword, page = page)) } returns WishlistEntityData(
                items = wishlistDefaultData,
                hasNextPage = hasNextPage)
    } else {
        coEvery { getData(GetWishlistParameter(keyword = keyword, page = page)) } returns WishlistEntityData(
                items = wishlistItems,
                hasNextPage = hasNextPage)
    }
}

fun GetWishlistDataUseCase.givenRepositoryGetWishlistDataReturnsThisOnBulkProgress(wishlistItems: List<WishlistItem>, boolean: Boolean) {
    coEvery { getData(GetWishlistParameter()) } returns WishlistEntityData(items = wishlistItems)
}

fun TopAdsImageViewUseCase.givenGetImageData(topadsImages: ArrayList<TopAdsImageViewModel>){
    coEvery { getImageData(any()) } returns topadsImages
}