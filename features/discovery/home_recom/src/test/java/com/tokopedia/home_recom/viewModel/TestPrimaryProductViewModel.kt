package com.tokopedia.home_recom.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.entity.Data
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.model.entity.ProductRecommendationProductDetail
import com.tokopedia.home_recom.util.RecommendationDispatcherTest
import com.tokopedia.home_recom.util.Status
import com.tokopedia.home_recom.viewmodel.PrimaryProductViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import rx.Observable
import java.util.concurrent.TimeoutException

class TestPrimaryProductViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val addWishListUseCase = mockk<AddWishListUseCase>(relaxed = true)
    private val removeWishListUseCase = mockk<RemoveWishListUseCase>(relaxed = true)
    private val getPrimaryProductUseCase = mockk<GetPrimaryProductUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)


    private val productId = "316960043"
    private val viewModel: PrimaryProductViewModel = PrimaryProductViewModel(
            addToCartUseCase = addToCartUseCase,
            addWishListUseCase = addWishListUseCase,
            dispatcher = RecommendationDispatcherTest(),
            getPrimaryProductUseCase = getPrimaryProductUseCase,
            removeWishlistUseCase = removeWishListUseCase,
            userSessionInterface = userSession
    )


    @Test
    fun `success get product info`(){
        val data = ProductRecommendationProductDetail(
                data = listOf(Data(
                        recommendation = listOf(ProductDetailData())
                ))
        )
        coEvery {
            getPrimaryProductUseCase.executeOnBackground()
        } returns PrimaryProductEntity(data)
        viewModel.getPrimaryProduct(productId, "")
        Assert.assertTrue(viewModel.productInfoDataModel.value?.status == Status.SUCCESS)
    }

    @Test
    fun `success get product info but empty`(){
        val data = ProductRecommendationProductDetail(
                data = listOf(Data(
                        recommendation = listOf()
                ))
        )
        coEvery {
            getPrimaryProductUseCase.executeOnBackground()
        } returns PrimaryProductEntity(data)

        viewModel.getPrimaryProduct(productId, "")
        Assert.assertTrue(viewModel.productInfoDataModel.value?.status == Status.EMPTY)
    }

    @Test
    fun `error get product info`(){
        coEvery {
            getPrimaryProductUseCase.executeOnBackground()
        } throws TimeoutException()

        viewModel.getPrimaryProduct(productId, "")
        Assert.assertTrue(viewModel.productInfoDataModel.value?.status == Status.ERROR)
    }

    @Test
    fun `success atc`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 1
                )
        ))
        viewModel.addToCart(AddToCartRequestParams())
        Assert.assertTrue(viewModel.addToCartLiveData.value?.status == Status.SUCCESS)
    }

    @Test
    fun `error atc`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_ERROR,
                data = DataModel(
                        success = 0
                )
        ))
        viewModel.addToCart(AddToCartRequestParams())
        Assert.assertTrue(viewModel.addToCartLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `throw error atc`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.error(TimeoutException())
        viewModel.addToCart(AddToCartRequestParams())
        Assert.assertTrue(viewModel.addToCartLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `success buy now`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 1
                )
        ))
        viewModel.buyNow(AddToCartRequestParams())
        Assert.assertTrue(viewModel.buyNowLiveData.value?.status == Status.SUCCESS)
    }

    @Test
    fun `error buy now`(){
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(
                        success = 0
                )
        ))
        viewModel.buyNow(AddToCartRequestParams())
        Assert.assertTrue(viewModel.buyNowLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `success add wishlist`(){
        val slot = slot<WishListActionListener>()
        every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onSuccessAddWishlist(productId)
        }
        viewModel.addWishList(productId)
        Assert.assertTrue(viewModel.addWishlistLiveData.value?.status == Status.SUCCESS)
    }

    @Test
    fun `error add wishlist`(){
        val slot = slot<WishListActionListener>()
        every { addWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onErrorAddWishList("", productId)
        }
        viewModel.addWishList(productId)
        Assert.assertTrue(viewModel.addWishlistLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `success remove wishlist`(){
        val slot = slot<WishListActionListener>()
        every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onSuccessRemoveWishlist(productId)
        }
        viewModel.removeWishList(productId)
        Assert.assertTrue(viewModel.removeWishlistLiveData.value?.status == Status.SUCCESS)
    }

    @Test
    fun `error remove wishlist`(){
        val slot = slot<WishListActionListener>()
        every { removeWishListUseCase.createObservable(any(), any(), capture(slot)) } answers {
            slot.captured.onErrorRemoveWishlist("", productId)
        }
        viewModel.removeWishList(productId)
        Assert.assertTrue(viewModel.removeWishlistLiveData.value?.status == Status.ERROR)
    }

    @Test
    fun `is login true`(){
        every { userSession.isLoggedIn } returns true
        Assert.assertTrue(viewModel.isLoggedIn())
    }

    @Test
    fun `is login false`(){
        every { userSession.isLoggedIn } returns false
        Assert.assertTrue(!viewModel.isLoggedIn())
    }
}