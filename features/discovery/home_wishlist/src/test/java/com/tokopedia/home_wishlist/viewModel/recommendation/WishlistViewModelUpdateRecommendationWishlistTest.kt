package com.tokopedia.home_wishlist.viewModel.recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetImageData
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 25/07/20.
 */

class WishlistViewModelUpdateRecommendationWishlistTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var wishlistViewModel: WishlistViewModel
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)
    private val parentPosition1 = 25
    private val childPosition1 = 2

    private val parentPosition2 = 25
    private val childPosition2 = 4

    @Test
    fun `Update wishlist will change its wishlist data in wishlistLiveData`() {
        val wishlistCurrentStateFor33 = true
        val wishlistCurrentStateFor55 = false

        // Create wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                getRecommendationUseCase = getRecommendationUseCase,
                getWishlistDataUseCase = getWishlistDataUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase)

        // topads set image
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ))
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        ), page = 2)

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()
        wishlistViewModel.getNextPageWishlistData()

        // Update wishlist data in selected parent and child
        wishlistViewModel.updateRecommendationItemWishlist(33, parentPosition1, childPosition1, wishlistCurrentStateFor33)
        wishlistViewModel.updateRecommendationItemWishlist(55, parentPosition2, childPosition2, wishlistCurrentStateFor55)
        wishlistViewModel.updateRecommendationItemWishlist(66, 4, childPosition2, wishlistCurrentStateFor55)
        wishlistViewModel.updateRecommendationItemWishlist(66, -1, childPosition2, wishlistCurrentStateFor55)
        wishlistViewModel.updateRecommendationItemWishlist(1, parentPosition1, childPosition2, wishlistCurrentStateFor55)


        // Expect product in parent position = 4 and child position = 2 updated with new wishlist status in wishlist data
        val recommendationCarouselDataModel =
                wishlistViewModel.wishlistLiveData.value!![parentPosition1] as RecommendationCarouselDataModel
        val recommendationCarouselItemDataModel =
                recommendationCarouselDataModel.list[childPosition1]

        Assert.assertEquals(wishlistCurrentStateFor33, recommendationCarouselItemDataModel.recommendationItem.isWishlist)

        val recommendationCarouselDataModel2 =
                wishlistViewModel.wishlistLiveData.value!![parentPosition2] as RecommendationCarouselDataModel
        val recommendationCarouselItemDataModel2 =
                recommendationCarouselDataModel2.list[childPosition2]

        Assert.assertEquals(wishlistCurrentStateFor55, recommendationCarouselItemDataModel2.recommendationItem.isWishlist)

    }
}