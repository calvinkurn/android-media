package com.tokopedia.shopdiscount.product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.manage.data.response.DeleteDiscountResponse
import com.tokopedia.shopdiscount.manage.domain.usecase.DeleteDiscountUseCase
import com.tokopedia.shopdiscount.product_detail.data.response.GetSlashPriceProductDetailResponse
import com.tokopedia.shopdiscount.product_detail.domain.GetSlashPriceProductDetailUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopDiscountProductDetailBottomSheetViewModelTest {

    @RelaxedMockK
    lateinit var getSlashPriceProductDetailUseCase: GetSlashPriceProductDetailUseCase

    @RelaxedMockK
    lateinit var reserveProductUseCase: MutationDoSlashPriceProductReservationUseCase

    @RelaxedMockK
    lateinit var deleteDiscountUseCase: DeleteDiscountUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    private val viewModel by lazy {
        ShopDiscountProductDetailBottomSheetViewModel(
            testCoroutineRule.dispatchers,
            getSlashPriceProductDetailUseCase,
            reserveProductUseCase,
            deleteDiscountUseCase,
            userSession
        )
    }

    private val mockChildProductId = "4456"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `When success getProductDetailListData, should return success result and matched with mock data`() {
        val mockParentProductId = "123"
        val mockStatus = 2
        coEvery {
            getSlashPriceProductDetailUseCase.executeOnBackground()
        } returns getMockSlashPriceProductDetailResponse()
        viewModel.getProductDetailListData(mockParentProductId, mockStatus)
        val liveDataValue = viewModel.productDetailListLiveData.value
        assert(liveDataValue is Success)
        val liveDataSuccessValue = liveDataValue as Success
        assert(liveDataSuccessValue.data.listProductDetailData.firstOrNull()?.productId == mockChildProductId)
    }

    private fun getMockSlashPriceProductDetailResponse(): GetSlashPriceProductDetailResponse {
        return GetSlashPriceProductDetailResponse(
            GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail(
                productList = listOf(
                    GetSlashPriceProductDetailResponse.GetSlashPriceProductDetail.ProductList(
                        productId = mockChildProductId
                    )
                )
            )
        )
    }

    @Test
    fun `When error getProductDetailListData, should return fail result`() {
        val mockParentProductId = "123"
        val mockStatus = 2
        coEvery {
            getSlashPriceProductDetailUseCase.executeOnBackground()
        } throws Exception()
        viewModel.getProductDetailListData(mockParentProductId, mockStatus)
        val liveDataValue = viewModel.productDetailListLiveData.value
        assert(liveDataValue is Fail)
    }

    @Test
    fun `When success reserveProduct, should return success result and matched with mock data`() {
        val mockParentProductId = "123"
        val mockParentProductPosition = 1
        val mockListSelectedVariantId = "456"
        coEvery {
            reserveProductUseCase.executeOnBackground()
        } returns getMockDoSlashPriceProductReservationResponse()
        viewModel.reserveProduct(mockParentProductId, mockParentProductPosition, mockListSelectedVariantId)
        val liveDataValue = viewModel.reserveProduct.value
        assert(liveDataValue is Success)
        val liveDataSuccessValue = liveDataValue as Success
        assert(liveDataSuccessValue.data.selectedProductVariantId == mockListSelectedVariantId)
        assert(liveDataSuccessValue.data.responseHeader.success)
    }

    private fun getMockDoSlashPriceProductReservationResponse(): DoSlashPriceProductReservationResponse {
        return DoSlashPriceProductReservationResponse(
            DoSlashPriceProductReservationResponse.DoSlashPriceProductReservation(
                responseHeader = ResponseHeader(
                    success = true
                )
            )
        )
    }

    @Test
    fun `When error reserveProduct, should return fail result`() {
        val mockParentProductId = "123"
        val mockParentProductPosition = 1
        val mockListSelectedVariantId = "456"
        coEvery {
            reserveProductUseCase.executeOnBackground()
        } throws Exception()
        viewModel.reserveProduct(mockParentProductId, mockParentProductPosition, mockListSelectedVariantId)
        val liveDataValue = viewModel.reserveProduct.value
        assert(liveDataValue is Fail)
    }

    @Test
    fun `When success deleteSelectedProductDiscount, should return success result and matched with mock data`() {
        val mockParentProductId = "123"
        val mockStatus = 2
        coEvery {
            deleteDiscountUseCase.executeOnBackground()
        } returns getMockDeleteDiscountResponse()
        viewModel.deleteSelectedProductDiscount(mockParentProductId, mockStatus)
        val liveDataValue = viewModel.deleteProductDiscount.value
        assert(liveDataValue is Success)
        val liveDataSuccessValue = liveDataValue as Success
        assert(liveDataSuccessValue.data.responseHeader.success)
    }

    private fun getMockDeleteDiscountResponse(): DeleteDiscountResponse {
        return DeleteDiscountResponse(
            DeleteDiscountResponse.DoSlashPriceStop(
                responseHeader = ResponseHeader(
                    success = true
                )
            )
        )
    }

    @Test
    fun `When error deleteSelectedProductDiscount, should return fail result`() {
        val mockParentProductId = "123"
        val mockStatus = 2
        coEvery {
            deleteDiscountUseCase.executeOnBackground()
        } throws Exception()
        viewModel.deleteSelectedProductDiscount(mockParentProductId, mockStatus)
        val liveDataValue = viewModel.deleteProductDiscount.value
        assert(liveDataValue is Fail)
    }
}