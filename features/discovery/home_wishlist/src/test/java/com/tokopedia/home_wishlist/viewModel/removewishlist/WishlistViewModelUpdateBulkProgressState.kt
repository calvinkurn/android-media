package com.tokopedia.home_wishlist.viewModel.removewishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.BannerTopAdsDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
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
class WishlistViewModelUpdateBulkProgressState {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wishlistViewModel: WishlistViewModel
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)

    @Test
    fun `Enter bulk mode will update all wishlist data state into bulk mode and does not contains recommendation`() {
        // Create wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))
        
        // Get wishlist usecase returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3"),
                WishlistItem(id="4"),
                WishlistItem(id="5"),
                WishlistItem(id="6"),
                WishlistItem(id="7"),
                WishlistItem(id="8"),
                WishlistItem(id="9")
        ))

        // Get recommendation usecase returns 1 recommendation data
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

        // Wishlist ViewModel enter bulk mode
        wishlistViewModel.enterBulkMode()

        // Expect isInBulkMode value is true
        Assert.assertEquals(true, wishlistViewModel.isInBulkModeState.value)
        // Expect all visitable is set to bulk mode
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is RecommendationCarouselDataModel && !it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Item recommendation carousel not in bulk mode", true)
            }
            if (it is WishlistItemDataModel && !it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Item wishlist not in bulk mode", true)
            }
        }

        // Expect wishlist data not contains recommendation data model
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is RecommendationCarouselDataModel) {
                Assert.assertFalse("Wishlist contains recommendation data model in bulk load more", true)
            }
        }

    }

    @Test
    fun `Exit will update all wishlist data bulk mode state to false and has recommendation model`() {
        // Create wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))
        
        // Get wishlist usecase returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3"),
                WishlistItem(id="4"),
                WishlistItem(id="5"),
                WishlistItem(id="6"),
                WishlistItem(id="7"),
                WishlistItem(id="8"),
                WishlistItem(id="9")
        ))

        // Get recommendation usecase returns 1 recommendation data
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
        // Some of items is marked
        wishlistViewModel.setWishlistOnMarkDelete(0, true)
        wishlistViewModel.setWishlistOnMarkDelete(1, true)
        wishlistViewModel.setWishlistOnMarkDelete(2, true)
        wishlistViewModel.setWishlistOnMarkDelete(3, true)


        // Wishlist  ViewModel enter bulk mode
        wishlistViewModel.enterBulkMode()
        // Wishlist  ViewModel exit bulk mode
        wishlistViewModel.exitBulkMode()

        // Expect isInBulkMode value is false
        Assert.assertEquals(false, wishlistViewModel.isInBulkModeState.value)
        // Expect all visitable is set to bulk mode
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Item recommendation carousel not in bulk mode", true)
            }
            if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Item wishlist not in bulk mode", true)
            }
        }

        // Expect recommendation model is exist in position 4
        Assert.assertEquals(
                BannerTopAdsDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass
        )

    }
}