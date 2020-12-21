package com.tokopedia.product.viewmodel

import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPdpViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 16/11/20
 */
class ImagePreviewPdpViewModelTest : BaseProductViewModelTest() {

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var removeWishListUseCase: RemoveWishListUseCase

    private val viewModel by lazy {
        ImagePreviewPdpViewModel(userSessionInterface, addWishListUseCase, removeWishListUseCase, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `success add product to wishlist`() {
        val productId = "123"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessAddWishlist(productId)
        }

        viewModel.addWishList(productId, null, {
            Assert.assertEquals(it, productId)
        })
    }

    @Test
    fun `error add product to wishlist because product id empty`() {
        val productId = ""
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList("", productId)
        }

        viewModel.addWishList(productId, {
            Assert.assertEquals(it, "")
        }, null)
    }

    @Test
    fun `error add product to wishlist`() {
        val productId = ""
        val errorMessage = ""
        every {
            (addWishListUseCase.createObservable(any(), any(), any()))
        }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList(errorMessage, productId)
        }

        viewModel.addWishList(productId, null, {
            Assert.assertEquals(it, errorMessage)
        })
    }

    @Test
    fun `success remove product from wishlist`() {
        val productId = "123"
        every { (removeWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessRemoveWishlist(productId)
        }

        viewModel.removeWishList(productId, null, {
            Assert.assertEquals(it, productId)
        })
    }

    @Test
    fun `error remove product from wishlist because product id empty`() {
        val productId = ""
        every { (removeWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorRemoveWishlist("", productId)
        }

        viewModel.removeWishList(productId, {
            Assert.assertTrue(it == "")
        }, null)
    }

    @Test
    fun `error remove product from wishlist`() {
        val productId = ""
        val errorMessage = ""
        every {
            (removeWishListUseCase.createObservable(any(), any(), any()))
        }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorRemoveWishlist(errorMessage, productId)
        }

        viewModel.removeWishList(productId, null, {
            Assert.assertEquals(it, errorMessage)
        })
    }
}