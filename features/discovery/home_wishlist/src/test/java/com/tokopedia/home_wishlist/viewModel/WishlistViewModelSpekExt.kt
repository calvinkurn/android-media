package com.tokopedia.home_wishlist.viewModel

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.TestDispatcherProvider
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.domain.GetWishlistParameter
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

fun TestBody.createWishlistViewModel(): WishlistViewModel {
    val userSessionInterface by memoized<UserSessionInterface>()
    val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
    val getSingleRecommendationUseCase by memoized<GetSingleRecommendationUseCase>()
    val getRecommendationUseCase by memoized<GetRecommendationUseCase>()
    val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
    val addToCartUseCase by memoized<AddToCartUseCase>()
    val bulkRemoveWishlistUseCase by memoized<BulkRemoveWishlistUseCase>()
    val addWishListUseCase by memoized<AddWishListUseCase>()

    return WishlistViewModel(
            userSessionInterface = userSessionInterface,
            getWishlistUseCase = getWishlistDataUseCase,
            getRecommendationUseCase = getRecommendationUseCase,
            getSingleRecommendationUseCase = getSingleRecommendationUseCase,
            wishlistCoroutineDispatcherProvider = TestDispatcherProvider(),
            removeWishListUseCase = removeWishlistUseCase,
            addToCartUseCase = addToCartUseCase,
            bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
            addWishListUseCase = addWishListUseCase
    )
}

@Suppress("UNUSED_VARIABLE")
fun FeatureBody.createWishlistTestInstance() {
    val userSessionInterface by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }

    val wishlistRepository by memoized {
        mockk<WishlistRepository>(relaxed = true)
    }

    val removeWishlistUseCase by memoized {
        mockk<RemoveWishListUseCase>(relaxed = true)
    }

    val addToCartUseCase by memoized {
        mockk<AddToCartUseCase>(relaxed = true)
    }

    val bulkRemoveWishlistUseCase by memoized {
        mockk<BulkRemoveWishlistUseCase>(relaxed = true)
    }

    val addWishListUseCase by memoized {
        mockk<AddWishListUseCase>(relaxed = true)
    }

    val getWishlistDataUseCase by memoized {
        mockk<GetWishlistDataUseCase>(relaxed = true)
    }

    val getSingleRecommendationUseCase by memoized {
        mockk<GetSingleRecommendationUseCase>(relaxed = true)
    }

    val getRecommendationUseCase by memoized {
        mockk<GetRecommendationUseCase>(relaxed = true)
    }
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
        var id = ((page-1) * defaultMaxPage) + 1
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