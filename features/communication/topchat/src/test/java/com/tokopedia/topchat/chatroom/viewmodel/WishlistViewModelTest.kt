package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
import org.junit.Test

/** Need to redo this after wishlist usecase revamp
 *  Wishlist usecase currently on progress revamp
 *  This class is used for placeholder & add coverage temporary
 * */
class WishlistViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun verify_add_to_wishlistv2_returns_success() {
        val productId = "123"
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addToWishListV2(productId, testUserId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, testUserId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun verify_add_to_wishlistv2_returns_fail() {
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addToWishListV2(productId, testUserId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, testUserId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun verify_remove_wishlistV2_returns_success(){
        val productId = "123"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeFromWishListV2(productId, testUserId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, testUserId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun verify_remove_wishlistV2_returns_fail(){
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeFromWishListV2(productId, testUserId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, testUserId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }
}
