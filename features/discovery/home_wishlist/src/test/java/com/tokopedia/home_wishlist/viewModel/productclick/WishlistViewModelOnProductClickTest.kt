package com.tokopedia.home_wishlist.viewModel.productclick

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetImageData
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 25/07/20.
 */

class WishlistViewModelOnProductClickTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wishlistViewModel: WishlistViewModel
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)
    private val parentPosition = 4
    private val position = 2
    private val productId = 99

    @Test
    fun `On product click will trigger onProductClick liveData action with selected position`(){
        // Create wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(getWishlistDataUseCase = getWishlistDataUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

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

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        // Wishlist viewmodel onProductClick called
        wishlistViewModel.onProductClick(
                productId,
                parentPosition,
                position
        )


        // Expect onProductClickActionData value is filled by parameter
        Assert.assertEquals(parentPosition, wishlistViewModel.productClickActionData.value?.peekContent()?.parentPosition)
        Assert.assertEquals(position, wishlistViewModel.productClickActionData.value?.peekContent()?.position)
        Assert.assertEquals(productId, wishlistViewModel.productClickActionData.value?.peekContent()?.productId)

    }
}