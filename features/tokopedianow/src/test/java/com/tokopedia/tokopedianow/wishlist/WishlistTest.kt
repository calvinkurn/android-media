package com.tokopedia.tokopedianow.wishlist

import com.tokopedia.tokopedianow.common.domain.model.AddToWishListResponse
import com.tokopedia.tokopedianow.common.domain.model.RemoveFromWishListResponse
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class WishlistTest: TokoNowWishlistViewModelTestFixture() {
    @Test
    fun `while adding product to wishlist, the request should be success`() {
        val productId = "122121"
        val response = AddToWishListResponse()

        onAddToWishlist_thenReturn(response)

        viewModel.addToWishlist(productId)

        viewModel.addToWishlistLiveData.verifySuccessEquals(Success(response))
    }

    @Test
    fun `while adding product to wishlist, the request should fail`() {
        val productId = "122121"

        onAddToWishlist_thenReturn(Throwable())

        viewModel.addToWishlist(productId)

        viewModel.addToWishlistLiveData.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while removing product from wishlist, the request should be success`() {
        val productId = "122121"
        val response = RemoveFromWishListResponse()

        onRemoveFromWishlist_thenReturn(response)

        viewModel.removeFromWishlist(productId)

        viewModel.removeFromWishlistLiveData.verifySuccessEquals(Success(response))
    }

    @Test
    fun `while removing product from wishlist, the request should fail`() {
        val productId = "122121"

        onRemoveFromWishlist_thenReturn(Throwable())

        viewModel.removeFromWishlist(productId)

        viewModel.removeFromWishlistLiveData.verifyErrorEquals(Fail(Throwable()))
    }
}
