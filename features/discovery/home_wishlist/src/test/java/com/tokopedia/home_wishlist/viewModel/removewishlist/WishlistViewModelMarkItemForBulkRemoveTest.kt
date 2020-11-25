package com.tokopedia.home_wishlist.viewModel.removewishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
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
class WishlistViewModelMarkItemForBulkRemoveTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var wishlistViewmodel: WishlistViewModel
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)
    private val markPosition = 2

    @Test
    fun `Mark item true will update its onChecked status in wishlist data`() {
        // Create wishlist viewmodel
        wishlistViewmodel = createWishlistViewModel(getRecommendationUseCase = getRecommendationUseCase, getWishlistDataUseCase = getWishlistDataUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)
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

        // Get recommendation usecase returns recommendation widget
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
        wishlistViewmodel.getWishlistData()

        // Mark true product in position 2
        wishlistViewmodel.setWishlistOnMarkDelete(markPosition, true)

        // Expect product in position 2 is checked = true in wishlist data
        val actualProductStatus =
                (wishlistViewmodel.wishlistLiveData.value!![markPosition] as WishlistItemDataModel).isOnChecked
        Assert.assertEquals(true, actualProductStatus)

    }

    @Test
    fun `Mark item false will update its onChecked status in wishlist data`() {
        // Create wishlist viewmodel
        wishlistViewmodel = createWishlistViewModel(getRecommendationUseCase = getRecommendationUseCase, getWishlistDataUseCase = getWishlistDataUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)
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
        wishlistViewmodel.getWishlistData()

        // Mark false product in position 2
        wishlistViewmodel.setWishlistOnMarkDelete(markPosition, false)

        // Expect product in position 2 is checked = false in wishlist data
        val actualProductStatus =
                (wishlistViewmodel.wishlistLiveData.value!![markPosition] as WishlistItemDataModel).isOnChecked
        Assert.assertEquals(false, actualProductStatus)

    }
}