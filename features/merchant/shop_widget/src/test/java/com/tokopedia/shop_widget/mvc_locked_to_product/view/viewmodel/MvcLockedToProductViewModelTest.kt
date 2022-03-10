package com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase.MvcLockedToProductUseCase
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductRequestUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MvcLockedToProductViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var viewModel: MvcLockedToProductViewModel

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var mvcLockedToProductUseCase: MvcLockedToProductUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MvcLockedToProductViewModel(
            userSession,
            mvcLockedToProductUseCase,
            testCoroutineDispatcherProvider
        )
    }

    @Test
    fun `check whether isUserLogin return mocked value`() {
        val mockIsUserLogin = true
        every { userSession.isLoggedIn } returns mockIsUserLogin
        Assert.assertTrue(viewModel.isUserLogin == mockIsUserLogin)
    }

    @Test
    fun `check whether get userId return mocked value`() {
        val mockUserId = "12356"
        every { userSession.userId } returns mockUserId
        Assert.assertTrue(viewModel.userId == mockUserId)
    }

    @Test
    fun `check whether isSellerView return true when match`() {
        val mockShopId = "1234"
        every { userSession.shopId } returns mockShopId
        Assert.assertTrue(viewModel.isSellerView(mockShopId))
    }

    @Test
    fun `check whether mvcLockToProductLiveData value is success`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        val mockResponse = getMockMvcLockedToProductResponse()
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } returns mockResponse
        viewModel.getMvcLockedToProductData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == mockResponse.shopPageMVCProductLock.nextPage)
        val mvcLockToProductValue = viewModel.mvcLockToProductLiveData.value
        assert(mvcLockToProductValue is Success)
        val voucherUiModelTitle = (mvcLockToProductValue as Success).data
            .mvcLockedToProductVoucherUiModel
            .title
        val voucherResponseTitle = mockResponse.shopPageMVCProductLock.voucher.title
        assert(voucherUiModelTitle == voucherResponseTitle)
    }

    @Test
    fun `check whether mvcLockToProductLiveData value is fail if exception thrown`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getMvcLockedToProductData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == null)
        val mvcLockToProductValue = viewModel.mvcLockToProductLiveData.value
        assert(mvcLockToProductValue is Fail)
    }

    @Test
    fun `check whether productListData value is success`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        val mockResponse = getMockMvcLockedToProductResponse()
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } returns mockResponse
        viewModel.getProductListData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == mockResponse.shopPageMVCProductLock.nextPage)
        val productListData = viewModel.productListDataProduct.value
        assert(productListData is Success)
        val totalProduct = (productListData as Success).data.size
        val mockResponseTotalProduct = mockResponse.shopPageMVCProductLock.productList.data.size
        assert(totalProduct == mockResponseTotalProduct)
    }

    @Test
    fun `check whether productListData value is fail if exception thrown`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getProductListData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == null)
        val mvcLockToProductValue = viewModel.productListDataProduct.value
        assert(mvcLockToProductValue is Fail)
    }

    private fun getMockMvcLockedToProductRequestUiModel(): MvcLockedToProductRequestUiModel {
        val mockPromoId = "6578"
        val shopId = "1234"
        return MvcLockedToProductRequestUiModel(
            shopId,
            mockPromoId,
            1,
            10,
            MvcLockedToProductSortUiModel(),
            LocalCacheModel()
        )
    }

    private fun getMockMvcLockedToProductResponse(): MvcLockedToProductResponse {
        val mockVoucherTitle = "Title here"
        val mockListProductData= listOf(
            MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data(),
            MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data(),
            MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data()
        )
        return MvcLockedToProductResponse(
            MvcLockedToProductResponse.ShopPageMVCProductLock(
                nextPage = 5,
                MvcLockedToProductResponse.ShopPageMVCProductLock.Voucher(
                    title = mockVoucherTitle
                ),
                MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList(
                    data = mockListProductData
                )
            )
        )
    }

}