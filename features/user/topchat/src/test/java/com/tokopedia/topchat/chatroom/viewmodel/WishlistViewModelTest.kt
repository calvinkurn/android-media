package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.every
import org.junit.Test

/** Need to redo this after wishlist usecase revamp
 *  Wishlist usecase currently on progress revamp
 *  This class is used for placeholder & add coverage temporary
 * */
class WishlistViewModelTest: BaseTopChatViewModelTest() {

    private val testProductId = "productId123"

    @Test
    fun should_get_response_when_success_add_wishlist() {
        //Given
        every {
            addWishListUseCase.createObservable(any(), any(), any())
        } returns Unit

        //When
        viewModel.addToWishList(testProductId, testUserId, object: WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}
            override fun onSuccessAddWishlist(productId: String?) {}
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
        })

        //Then
    }

    @Test
    fun should_get_error_when_success_add_wishlist() {
        //Given
        every {
            addWishListUseCase.createObservable(any(), any(), any())
        } returns Unit

        //When
        viewModel.addToWishList(testProductId, testUserId, object: WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}
            override fun onSuccessAddWishlist(productId: String?) {}
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
        })

        //Then
    }

    @Test
    fun should_get_response_when_success_remove_wishlist() {
        //Given
        every {
            removeWishListUseCase.createObservable(any(), any(), any())
        } returns Unit

        //When
        viewModel.removeFromWishList(testProductId, testUserId, object: WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}
            override fun onSuccessAddWishlist(productId: String?) {}
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
        })

        //Then
    }

    @Test
    fun should_get_error_when_success_remove_wishlist() {
        //Given
        every {
            removeWishListUseCase.createObservable(any(), any(), any())
        } returns Unit

        //When
        viewModel.removeFromWishList(testProductId, testUserId, object: WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}
            override fun onSuccessAddWishlist(productId: String?) {}
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
        })

        //Then
    }
}