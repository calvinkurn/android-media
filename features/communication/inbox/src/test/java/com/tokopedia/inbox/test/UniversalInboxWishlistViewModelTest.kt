package com.tokopedia.inbox.test

import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.coEvery
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UniversalInboxWishlistViewModelTest : UniversalInboxViewModelTestFixture() {

    private val dummyResultAddWishlist = Success(AddToWishlistV2Response.Data.WishlistAddV2())
    private val dummyResultRemoveWishlist = Success(DeleteWishlistV2Response.Data.WishlistRemoveV2())
    private val dummyFailResult = Fail(dummyThrowable)
    private val dummyItem = RecommendationItem()
    private val dummyListener = spyk(object : WishlistV2ActionListener {
        override fun onErrorAddWishList(throwable: Throwable, productId: String) {}
        override fun onSuccessAddWishlist(
            result: AddToWishlistV2Response.Data.WishlistAddV2,
            productId: String
        ) {}
        override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
        override fun onSuccessRemoveWishlist(
            result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
            productId: String
        ) {}
    })

    @Test
    fun `should call onSuccessAddWishlist when success add product to wishlist`() {
        runBlocking {
            // Given
            coEvery {
                addWishListV2UseCase.executeOnBackground()
            } returns dummyResultAddWishlist

            // When
            viewModel.addWishlistV2(dummyItem, dummyListener)

            // Then
            verify {
                dummyListener.onSuccessAddWishlist(any(), any())
            }
        }
    }

    @Test
    fun `should call onErrorAddWishList when add product to wishlist response is fail`() {
        runBlocking {
            // Given
            coEvery {
                addWishListV2UseCase.executeOnBackground()
            } returns dummyFailResult

            // When
            viewModel.addWishlistV2(dummyItem, dummyListener)

            // Then
            verify {
                dummyListener.onErrorAddWishList(any(), any())
            }
        }
    }

    @Test
    fun `should call onErrorAddWishList when error add product to wishlist`() {
        runBlocking {
            // Given
            coEvery {
                addWishListV2UseCase.executeOnBackground()
            } throws dummyThrowable

            // When
            viewModel.addWishlistV2(dummyItem, dummyListener)

            // Then
            verify {
                dummyListener.onErrorAddWishList(any(), any())
            }
        }
    }

    @Test
    fun `should call onSuccessRemoveWishlist when success remove product from wishlist`() {
        runBlocking {
            // Given
            coEvery {
                deleteWishlistV2UseCase.executeOnBackground()
            } returns dummyResultRemoveWishlist

            // When
            viewModel.removeWishlistV2(dummyItem, dummyListener)

            // Then
            verify {
                dummyListener.onSuccessRemoveWishlist(any(), any())
            }
        }
    }

    @Test
    fun `should call onErrorRemoveWishlist when remove product from wishlist response is fail`() {
        runBlocking {
            // Given
            coEvery {
                deleteWishlistV2UseCase.executeOnBackground()
            } returns dummyFailResult

            // When
            viewModel.removeWishlistV2(dummyItem, dummyListener)

            // Then
            verify {
                dummyListener.onErrorRemoveWishlist(any(), any())
            }
        }
    }

    @Test
    fun `should call onErrorRemoveWishlist when error remove product from wishlist`() {
        runBlocking {
            // Given
            coEvery {
                deleteWishlistV2UseCase.executeOnBackground()
            } throws dummyThrowable

            // When
            viewModel.removeWishlistV2(dummyItem, dummyListener)

            // Then
            verify {
                dummyListener.onErrorRemoveWishlist(any(), any())
            }
        }
    }
}
