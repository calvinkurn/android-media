package com.tokopedia.product.viewmodel

import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPdpViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

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

    @RelaxedMockK
    lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    private val viewModel by lazy {
        ImagePreviewPdpViewModel(userSessionInterface, addWishListUseCase, removeWishListUseCase, addToWishlistV2UseCase, deleteWishlistV2UseCase, CoroutineTestDispatchersProvider)
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

    @Test
    fun `user logged in and shopId match then isShopOwner should return true`(){
        val shopId = anyString()

        every { userSessionInterface.isLoggedIn } returns true
        every{userSessionInterface.shopId} returns shopId

        val isShopOwner = viewModel.isShopOwner(shopId)
        Assert.assertTrue(isShopOwner)
    }

    @Test
    fun `user not logged in and shopId match then isShopOwner should return false`(){
        val shopId = anyString()

        every { userSessionInterface.isLoggedIn } returns false
        every{userSessionInterface.shopId} returns shopId

        val isShopOwner = viewModel.isShopOwner(shopId)
        Assert.assertFalse(isShopOwner)
    }

    @Test
    fun `user not logged in and shopId not match then isShopOwner should return false`(){
        val shopId = anyString()

        every { userSessionInterface.isLoggedIn } returns false
        every{userSessionInterface.shopId} returns "shopId"

        val isShopOwner = viewModel.isShopOwner(shopId)
        Assert.assertFalse(isShopOwner)
    }

    @Test
    fun `user logged in and shopId not match then isShopOwner should return false`(){
        val shopId = anyString()

        every { userSessionInterface.isLoggedIn } returns true
        every{userSessionInterface.shopId} returns "shopId"

        val isShopOwner = viewModel.isShopOwner(shopId)
        Assert.assertFalse(isShopOwner)
    }

    @Test
    fun `verify add to wishlistv2 returns success` () {
        val productId = "123"
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify add to wishlistv2 returns fail` () {
        val productId = "123"
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns success`(){
        val productId = "123"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns fail`(){
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }
}