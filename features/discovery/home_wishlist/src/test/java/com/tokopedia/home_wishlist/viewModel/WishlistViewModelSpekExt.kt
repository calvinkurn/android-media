package com.tokopedia.home_wishlist.viewModel

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.TestDispatcherProvider
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
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
    val wishlistRepository by memoized<WishlistRepository>()
    val removeWishlistUseCase by memoized<RemoveWishListUseCase>()
    val addToCartUseCase by memoized<AddToCartUseCase>()
    val bulkRemoveWishlistUseCase by memoized<BulkRemoveWishlistUseCase>()
    val addWishListUseCase by memoized<AddWishListUseCase>()

    return WishlistViewModel(
            userSessionInterface = userSessionInterface,
            wishlistRepository = wishlistRepository,
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
}


fun WishlistRepository.givenRepositoryGetSingleRecommendationReturnsThis(recommendationList: List<RecommendationItem>) {
    coEvery { getSingleRecommendationData(0) } returns
            RecommendationWidget(
                    recommendationItemList = recommendationList
            )
}

fun WishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(recommendationItems: List<RecommendationItem>) {
    coEvery {
        getRecommendationData(any(), any()) } returns
            listOf(
                    RecommendationWidget(
                            recommendationItemList = recommendationItems
                    )
            )
}

fun WishlistRepository.givenRepositoryGetWishlistDataReturnsThis(wishlistItems: List<WishlistItem> = listOf(),
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
        coEvery { getData(keyword, page) } returns WishlistEntityData(
                items = wishlistDefaultData,
                hasNextPage = hasNextPage)
    } else {
        coEvery { getData(keyword, page) } returns WishlistEntityData(
                items = wishlistItems,
                hasNextPage = hasNextPage)
    }
}

fun WishlistRepository.givenRepositoryGetWishlistDataReturnsThisOnBulkProgress(wishlistItems: List<WishlistItem>, boolean: Boolean) {
    coEvery { getData(any(), any()) } returns WishlistEntityData(items = wishlistItems)
}