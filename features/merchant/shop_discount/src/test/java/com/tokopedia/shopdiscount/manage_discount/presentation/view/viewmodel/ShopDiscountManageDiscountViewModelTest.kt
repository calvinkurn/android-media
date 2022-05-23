package com.tokopedia.shopdiscount.manage_discount.presentation.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.manage_discount.data.response.DoSlashPriceProductSubmissionResponse
import com.tokopedia.shopdiscount.manage_discount.data.response.GetSlashPriceSetupProductListResponse
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.domain.GetSlashPriceSetupProductListUseCase
import com.tokopedia.shopdiscount.manage_discount.domain.MutationSlashPriceProductSubmissionUseCase
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountConstant.GET_SETUP_PRODUCT_LIST_DELAY
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.utils.constant.SlashPriceStatusId
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ShopDiscountManageDiscountViewModelTest {

    @RelaxedMockK
    lateinit var getSlashPriceSetupProductListUseCase: GetSlashPriceSetupProductListUseCase

    @RelaxedMockK
    lateinit var mutationSlashPriceProductSubmissionUseCase: MutationSlashPriceProductSubmissionUseCase

    @RelaxedMockK
    lateinit var mutationDoSlashPriceProductReservationUseCase: MutationDoSlashPriceProductReservationUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    private val mockRequestId = "12345"
    private val mockSelectedProductVariantId = ""
    private val mockModeCreate = ShopDiscountManageDiscountMode.CREATE
    private val mockModeUpdate = ShopDiscountManageDiscountMode.UPDATE
    private val mockSlashPriceStatus = SlashPriceStatusId.CREATE
    private val mockSlashPriceStatusOngoing = SlashPriceStatusId.ONGOING
    private val mockStartDateValue = Date(1652415771000)
    private val mockEndDateValue = Date(1670880168000)
    private val mockBulkApplyRupiahDiscountAmount = 1000
    private val mockBulkApplyPercentageDiscountAmount = 10
    private val mockDeletedProductId = "12345"
    private val mockDeletedProductPosition = "1"
    private val mockProductId1 = "1"
    private val mockProductId2 = "2"
    private val mockProductId3 = "3"

    private val viewModel by lazy {
        ShopDiscountManageDiscountViewModel(
            testCoroutineRule.dispatchers,
            getSlashPriceSetupProductListUseCase,
            mutationSlashPriceProductSubmissionUseCase,
            mutationDoSlashPriceProductReservationUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `When success get setup product list data on create mode, should return success result and matched with mock data`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponse()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeCreate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
        }
    }

    private fun getSlashPriceSetupProductListMockResponse(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(),
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = listOf(GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant())
                    )
                )
            )
        )
    }

    @Test
    fun `When error get setup product list data on create mode, should return fail result`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } throws Exception()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeCreate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Fail)
        }
    }

    @Test
    fun `When apply bulk update on discount type rupiah and slash price status create, then the updated product should match with mocked data`() {
        val mockDiscountSettings = getMockDiscountSettings(
            mockStartDateValue,
            mockEndDateValue,
            mockBulkApplyRupiahDiscountAmount,
            DiscountType.RUPIAH
        )
        viewModel.applyBulkUpdate(
            getMockListSetupProductDataWithDiscountAndNoError(),
            mockDiscountSettings,
            mockModeCreate,
            mockSlashPriceStatus
        )
        val liveDataValue = viewModel.updatedProductListData.value
        assert(liveDataValue?.size == 2)
        assert(liveDataValue?.any {
            it.productStatus.isVariant
        } == true)
        assert(liveDataValue?.firstOrNull()?.slashPriceInfo?.discountedPrice == mockBulkApplyRupiahDiscountAmount)
    }

    @Test
    fun `When apply bulk update on discount type rupiah and slash price status create with no date, then the updated product should match with mocked data, while start date and end date match with mocked default date`() {
        val mockDiscountSettings = getMockDiscountSettings(
            discountAmount = mockBulkApplyRupiahDiscountAmount,
            discountType = DiscountType.RUPIAH
        )
        viewModel.applyBulkUpdate(
            getMockListSetupProductDataWithDiscountAndNoError(),
            mockDiscountSettings,
            mockModeCreate,
            mockSlashPriceStatus,
        )
        val liveDataValue = viewModel.updatedProductListData.value
        assert(liveDataValue?.size == 2)
        assert(liveDataValue?.any {
            it.productStatus.isVariant
        } == true)
        assert(liveDataValue?.firstOrNull()?.slashPriceInfo?.startDate?.time != 0L)
        assert(liveDataValue?.firstOrNull()?.slashPriceInfo?.endDate?.time != 0L)
    }

    @Test
    fun `When apply bulk update on discount type percentage, then the updated product should match with mocked data`() {
        val mockDiscountSettings = getMockDiscountSettings(
            mockStartDateValue,
            mockEndDateValue,
            mockBulkApplyPercentageDiscountAmount,
            DiscountType.PERCENTAGE
        )
        viewModel.applyBulkUpdate(
            getMockListSetupProductDataWithDiscountAndNoError(),
            mockDiscountSettings,
            mockModeUpdate,
            mockSlashPriceStatusOngoing
        )
        val liveDataValue = viewModel.updatedProductListData.value
        assert(liveDataValue?.size == 2)
        assert(liveDataValue?.any {
            it.productStatus.isVariant
        } == true)
        assert(liveDataValue?.firstOrNull()?.slashPriceInfo?.discountPercentage == mockBulkApplyPercentageDiscountAmount)
    }

    private fun getMockDiscountSettings(
        startDate: Date? = null,
        endDate: Date? = null,
        discountAmount: Int,
        discountType: DiscountType
    ): DiscountSettings {
        return DiscountSettings(
            startDate = startDate,
            endDate = endDate,
            discountAmount = discountAmount,
            discountType = discountType,
            maxPurchaseQuantity = 1,
            isUsingCustomPeriod = false
        )
    }

    private fun getMockListSetupProductDataWithDiscountAndNoError(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId1,
                listProductWarehouse = listOf(ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                    discountedPercentage = 10,
                    discountedPrice = 1000,
                    originalPrice = 100000
                )),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true
                )
            ),
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                            discountedPercentage = 10,
                            discountedPrice = 1000,
                            originalPrice = 100000
                        ))
                    )
                ),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true,
                    isVariant = true
                )
            )
        )
    }

    private fun getMockListSetupProductDataWithNoDiscount(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId1,
                listProductWarehouse = listOf(ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                    originalPrice = 100000
                ))
            ),
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                            originalPrice = 100000
                        ))
                    )
                )
            )
        )
    }

    private fun getMockListSetupProductDataWithDiscountButError(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId1,
                listProductWarehouse = listOf(ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                    originalPrice = 100000
                )),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true,
                    errorType = ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.VALUE_ERROR
                )
            ),
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                            originalPrice = 100000
                        ))
                    )
                ),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true,
                    errorType = ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.VALUE_ERROR
                )
            )
        )
    }

    @Test
    fun `When call checkShouldEnableButtonSubmit with discounted product and no error product list, then should enable button submit`() {
        viewModel.checkShouldEnableButtonSubmit(
            getMockListSetupProductDataWithDiscountAndNoError()
        )
        assert(viewModel.enableButtonSubmitLiveData.value == true)
    }

    @Test
    fun `When call checkShouldEnableButtonSubmit with no discount, then should not enable button submit`() {
        viewModel.checkShouldEnableButtonSubmit(
            getMockListSetupProductDataWithNoDiscount()
        )
        assert(viewModel.enableButtonSubmitLiveData.value == false)
    }

    @Test
    fun `When call checkShouldEnableButtonSubmit with discount but has error product, then should not enable button submit`() {
        viewModel.checkShouldEnableButtonSubmit(
            getMockListSetupProductDataWithDiscountButError()
        )
        assert(viewModel.enableButtonSubmitLiveData.value == false)
    }

    @Test
    fun `When call submitProductDiscount with mode create is success, then should return success result`() {
        coEvery {
            mutationSlashPriceProductSubmissionUseCase.executeOnBackground()
        }returns DoSlashPriceProductSubmissionResponse()
        viewModel.submitProductDiscount(
            getMockListSetupProductDataWithDiscountAndNoError(),
            mockModeCreate
        )
        val liveData = viewModel.resultSubmitProductSlashPriceLiveData.value
        assert(liveData is Success)
    }

    @Test
    fun `When call submitProductDiscount with mode update is success, then should return success result`() {
        coEvery {
            mutationSlashPriceProductSubmissionUseCase.executeOnBackground()
        }returns DoSlashPriceProductSubmissionResponse()
        viewModel.submitProductDiscount(
            getMockListSetupProductDataWithDiscountAndNoError(),
            mockModeUpdate
        )
        val liveData = viewModel.resultSubmitProductSlashPriceLiveData.value
        assert(liveData is Success)
    }

    @Test
    fun `When call submitProductDiscount is error, then should return fail result`() {
        coEvery {
            mutationSlashPriceProductSubmissionUseCase.executeOnBackground()
        }throws Exception()
        viewModel.submitProductDiscount(
            getMockListSetupProductDataWithDiscountAndNoError(),
            mockModeUpdate
        )
        val liveData = viewModel.resultSubmitProductSlashPriceLiveData.value
        assert(liveData is Fail)
    }

    @Test
    fun `When call deleteSlashPriceProduct with mode create is success, then should return success result`() {
        coEvery {
            mutationDoSlashPriceProductReservationUseCase.executeOnBackground()
        }returns DoSlashPriceProductReservationResponse()
        viewModel.deleteSlashPriceProduct(
            mockDeletedProductId,
            mockDeletedProductPosition,
            mockRequestId,
            mockModeCreate
        )
        val liveData = viewModel.resultDeleteSlashPriceProductLiveData.value
        assert(liveData is Success)
    }

    @Test
    fun `When call deleteSlashPriceProduct with mode update is success, then should return success result`() {
        coEvery {
            mutationDoSlashPriceProductReservationUseCase.executeOnBackground()
        }returns DoSlashPriceProductReservationResponse()
        viewModel.deleteSlashPriceProduct(
            mockDeletedProductId,
            mockDeletedProductPosition,
            mockRequestId,
            mockModeUpdate
        )
        val liveData = viewModel.resultDeleteSlashPriceProductLiveData.value
        assert(liveData is Success)
    }

    @Test
    fun `When call deleteSlashPriceProduct is error, then should return fail result`() {
        coEvery {
            mutationDoSlashPriceProductReservationUseCase.executeOnBackground()
        }throws Exception()
        viewModel.deleteSlashPriceProduct(
            mockDeletedProductId,
            mockDeletedProductPosition,
            mockRequestId,
            mockModeUpdate
        )
        val liveData = viewModel.resultDeleteSlashPriceProductLiveData.value
        assert(liveData is Fail)
    }

    @Test
    fun `When updateSingleProductData and data is matched, then updatedProductListData should return updated data`() {
        viewModel.updateSingleProductData(
            getMockListSetupProductDataWithDiscountAndNoError(),
            getMockUpdatedProductData(mockProductId1),
            mockModeUpdate,
            mockSlashPriceStatus
        )
        val liveData = viewModel.updatedProductListData.value
        assert(liveData?.firstOrNull { it.productId == mockProductId1 } != null)
    }

    @Test
    fun `When updateSingleProductData data is not matched, then updatedProductListData value should be null`() {
        viewModel.updateSingleProductData(
            getMockListSetupProductDataWithDiscountAndNoError(),
            getMockUpdatedProductData(mockProductId3),
            mockModeUpdate,
            mockSlashPriceStatusOngoing
        )
        val liveData = viewModel.updatedProductListData.value
        assert(liveData == null)
    }

    private fun getMockUpdatedProductData(productId: String): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            productId = productId
        )
    }

}