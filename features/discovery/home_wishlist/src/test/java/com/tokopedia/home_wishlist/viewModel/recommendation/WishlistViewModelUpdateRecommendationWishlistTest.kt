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

    @Test
    fun `Update wishlist will change its wishlist data in wishlistLiveData`() {
        val wishlistCurrentStateFor33 = true

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

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(RecommendationItem(productId = 33)))

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        // Update wishlist data in selected parent and child
        val index = (wishlistViewModel.wishlistLiveData.value?.size ?: -1) -1
        wishlistViewModel.updateRecommendationItemWishlist(33, index, 0, wishlistCurrentStateFor33)

        // Expect product in last position and child position = 0 updated with new wishlist status in wishlist data
        val recommendationCarouselDataModel =
                wishlistViewModel.wishlistLiveData.value!![index] as RecommendationCarouselDataModel
        val recommendationCarouselItemDataModel =
                recommendationCarouselDataModel.list[0]

        Assert.assertEquals(wishlistCurrentStateFor33, recommendationCarouselItemDataModel.recommendationItem.isWishlist)
    }
}