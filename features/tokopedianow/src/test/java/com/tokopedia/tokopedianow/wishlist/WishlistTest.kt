package com.tokopedia.tokopedianow.wishlist

import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import org.junit.Test

class WishlistTest: TokoNowWishlistViewModelTestFixture() {
    @Test
    fun `while adding product to wishlist, the request should be success`() {
        val productId = "122121"
        val response = Success(AddToWishlistV2Response.Data.WishlistAddV2())

        onAddToWishlist_thenReturn(response)

        viewModel.addToWishlist(productId)

        viewModel.addToWishlistLiveData.verifySuccessEquals(Success(response.data))
    }

    @Test
    fun `while adding product to wishlist, the request should fail`() {
        val productId = "122121"

        onAddToWishlist_thenReturn(Fail(Throwable()))

        viewModel.addToWishlist(productId)

        viewModel.addToWishlistLiveData.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `while removing product from wishlist, the request should be success`() {
        val productId = "122121"
        val response = Success(DeleteWishlistV2Response.Data.WishlistRemoveV2())

        onRemoveFromWishlist_thenReturn(response)

        viewModel.removeFromWishlist(productId)

        viewModel.removeFromWishlistLiveData.verifySuccessEquals(Success(response.data))
    }

    @Test
    fun `while removing product from wishlist, the request should fail`() {
        val productId = "122121"

        onRemoveFromWishlist_thenReturn(Fail(Throwable()))

        viewModel.removeFromWishlist(productId)

        viewModel.removeFromWishlistLiveData.verifyErrorEquals(Fail(Throwable()))
    }
}
