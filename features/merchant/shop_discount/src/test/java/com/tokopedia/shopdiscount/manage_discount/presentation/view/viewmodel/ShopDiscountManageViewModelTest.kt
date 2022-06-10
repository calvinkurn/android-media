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
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.ALL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.NO_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.PARTIAL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.R2_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.START_DATE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.VALUE_ERROR
import com.tokopedia.shopdiscount.manage_discount.domain.GetSlashPriceSetupProductListUseCase
import com.tokopedia.shopdiscount.manage_discount.domain.MutationSlashPriceProductSubmissionUseCase
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountConstant.GET_SETUP_PRODUCT_LIST_DELAY
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.utils.constant.DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.toString
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ShopDiscountManageViewModelTest {

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
    private val mockSlashPriceStatus = DiscountStatus.DEFAULT
    private val mockSlashPriceStatusOngoing = DiscountStatus.ONGOING
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
        ShopDiscountManageViewModel(
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

    @Test
    fun `When success get setup product list data on update mode, then should return success result and enable variant`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponse()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.listProductVariant.any { productVariant ->
                    productVariant.variantStatus.isVariantEnabled == true
                }
            })
        }
    }

    @Test
    fun `When success get setup product list data on create mode with abusive product variants, then should return success result with disabled variant`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseWithAbusiveVariant()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeCreate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.listProductVariant.any { productVariant ->
                    productVariant.variantStatus.isVariantEnabled == false
                }
            })
        }
    }

    @Test
    fun `When success get setup product list data on create mode with default start date and end date, then should return success result with null start date and end date`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseWithDefaultStartDate()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeCreate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.mappedResultData.minStartDateUnix == null && it.mappedResultData.minEndDateUnix == null
            })
        }
    }

    @Test
    fun `When success get setup product list data on update mode with value error, then should return success result with product value error type`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseValueError()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == VALUE_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with variant data on update mode with value error, then should return success result with product value error type`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListWithVariantMockResponseValueError()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == VALUE_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with all variant abusive data, then should return all abusive error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListWithVariantAbusiveMockResponseValueError()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == ALL_ABUSIVE_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with value error and product not discounted, then should return no error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseValueErrorAndNoDiscount()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == NO_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with start date error, product discounted and start date error type, then should return no error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseStartDateErrorNoDiscountAndErrorTypeStartDate()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == NO_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with correct start date, product discounted and start date error type, then should return no error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseCorrectStartDateNoDiscountAndErrorTypeStartDate()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == NO_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list variant with start date error, product discounted and start date error type, then should return no error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListVariantMockResponseStartDateErrorNoDiscountAndErrorTypeStartDate()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == NO_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list variant with correct start date, product discounted and start date error type, then should return no error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListVariantMockResponseCorrectStartDateNoDiscountAndErrorTypeStartDate()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == NO_ERROR
            })
        }
    }


    @Test
    fun `When success get setup product list with r2 abusive error and product discounted, then should return r2 error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseR2ErrorWithDiscount()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == R2_ABUSIVE_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with no r2 abusive error and product discounted, then should return no error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseNoR2WithDiscount()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == NO_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with r2 abusive error and no product discount, then should return no error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseR2AbusiveWithNoDiscount()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == NO_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list variant with r2 abusive error and product discounted, then should return r2 error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListVariantMockResponseR2ErrorWithDiscount()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == R2_ABUSIVE_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list variant with partial abusive error, then should return abusive partial error`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListVariantMockResponsePartialAbusiveError()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.errorType == PARTIAL_ABUSIVE_ERROR
            })
        }
    }

    @Test
    fun `When success get setup product list with multiple warehouse, then multi loc should be true`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListMockResponseMultiWarehouse()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.productStatus.isMultiLoc
            })
        }
    }

    @Test
    fun `When success get setup product list variant with multiple warehouse, then multi loc should be true`() {
        testCoroutineRule.runBlockingTest {
            coEvery {
                getSlashPriceSetupProductListUseCase.executeOnBackground()
            } returns getSlashPriceSetupProductListVariantMockResponseMultiWarehouse()
            viewModel.getSetupProductListData(
                mockRequestId,
                mockSelectedProductVariantId,
                mockModeUpdate,
                mockSlashPriceStatus
            )
            advanceTimeBy(GET_SETUP_PRODUCT_LIST_DELAY)
            val liveDataValue = viewModel.setupProductListLiveData.value
            assert(liveDataValue is Success)
            assert((liveDataValue as Success).data.listSetupProductData.any {
                it.listProductVariant.any { productVariant ->
                    productVariant.variantStatus.isMultiLoc
                }
            })
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

    private fun getSlashPriceSetupProductListMockResponseWithAbusiveVariant(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = listOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        abusiveRule = true
                                    )
                                )
                            )
                        ),
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseWithDefaultStartDate(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            startDate = "2022-06-03 18:00:00 +0700 WIB",
                            endDate = "2022-06-03 18:00:00 +0700 WIB"
                        )
                    ),
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            startDate = "0001-01-01 00:00:00 +0000 UTC",
                            endDate = "0001-01-01 00:00:00 +0000 UTC"
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseValueError(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        warehouses = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                discountedPercentage = 0
                            )
                        ),
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            startDate = "2022-06-03 18:00:00 +0700 WIB",
                            endDate = "2022-06-03 18:00:00 +0700 WIB"
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListWithVariantMockResponseValueError(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = listOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 100
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    startDate = "2022-06-03 18:00:00 +0700 WIB",
                                    endDate = "2022-06-03 18:00:00 +0700 WIB"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListWithVariantAbusiveMockResponseValueError(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = listOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 100,
                                        abusiveRule = true
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    startDate = "2022-06-03 18:00:00 +0700 WIB",
                                    endDate = "2022-06-03 18:00:00 +0700 WIB"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseValueErrorAndNoDiscount(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = listOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 0
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    startDate = "0001-01-01 00:00:00 +0000 UTC",
                                    endDate = "0001-01-01 00:00:00 +0000 UTC"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseStartDateErrorNoDiscountAndErrorTypeStartDate(): GetSlashPriceSetupProductListResponse {
        val mockStartDate = Calendar.getInstance().apply {
            add(Calendar.MINUTE, -3)
        }.time
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        warehouses = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                discountedPercentage = 50
                            )
                        ),
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            startDate = mockStartDate.toString(
                                DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE
                            ),
                            endDate = "2022-06-03 18:00:00 +0700 WIB"
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListVariantMockResponseStartDateErrorNoDiscountAndErrorTypeStartDate(): GetSlashPriceSetupProductListResponse {
        val mockStartDate = Calendar.getInstance().apply {
            add(Calendar.MINUTE, -3)
        }.time
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 50
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    startDate = mockStartDate.toString(
                                        DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE
                                    ),
                                    endDate = "2022-06-03 18:00:00 +0700 WIB"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseCorrectStartDateNoDiscountAndErrorTypeStartDate(): GetSlashPriceSetupProductListResponse {
        val mockStartDate = Calendar.getInstance().apply {
            add(Calendar.MINUTE, -10)
        }.time
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        warehouses = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                discountedPercentage = 50
                            )
                        ),
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            startDate = mockStartDate.toString(
                                DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE
                            ),
                            endDate = "2022-06-03 18:00:00 +0700 WIB"
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListVariantMockResponseCorrectStartDateNoDiscountAndErrorTypeStartDate(): GetSlashPriceSetupProductListResponse {
        val mockStartDate = Calendar.getInstance().apply {
            add(Calendar.MINUTE, -10)
        }.time
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 50
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    startDate = mockStartDate.toString(
                                        DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE
                                    ),
                                    endDate = "2022-06-03 18:00:00 +0700 WIB"
                                )
                            )
                        )
                    )
                )
            )
        )
    }


    private fun getSlashPriceSetupProductListMockResponseR2ErrorWithDiscount(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        warehouses = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                discountedPercentage = 50,
                                avgSoldPrice = 4000.0,
                                discountedPrice = 5000.0,
                                originalPrice = 10000.0
                            )
                        ),
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            discountedPrice = 6000.0,
                            discountPercentage = 50
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseNoR2WithDiscount(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        warehouses = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                discountedPercentage = 50,
                                avgSoldPrice = 6000.0,
                                discountedPrice = 5000.0,
                                originalPrice = 10000.0
                            )
                        ),
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            discountedPrice = 6000.0,
                            discountPercentage = 50
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseR2AbusiveWithNoDiscount(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        warehouses = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                discountedPercentage = 50,
                                avgSoldPrice = 4000.0,
                                discountedPrice = 5000.0,
                                originalPrice = 10000.0
                            )
                        ),
                        slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                            discountedPrice = 5000.0,
                            discountPercentage = 50,
                            startDate = "0001-01-01 00:00:00 +0000 UTC",
                            endDate = "0001-01-01 00:00:00 +0000 UTC"
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListVariantMockResponseR2ErrorWithDiscount(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 50,
                                        avgSoldPrice = 4000.0,
                                        discountedPrice = 5000.0,
                                        originalPrice = 10000.0
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    discountedPrice = 6000.0,
                                    discountPercentage = 50
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListVariantMockResponsePartialAbusiveError(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 50,
                                        discountedPrice = 5000.0,
                                        originalPrice = 10000.0,
                                        abusiveRule = true
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    discountedPrice = 6000.0,
                                    discountPercentage = 50
                                )
                            ),
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(
                                        discountedPercentage = 50,
                                        discountedPrice = 5000.0,
                                        originalPrice = 10000.0
                                    )
                                ),
                                slashPriceInfo = GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.SlashPriceInfo(
                                    discountedPrice = 6000.0,
                                    discountPercentage = 50
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListMockResponseMultiWarehouse(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        warehouses = arrayListOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(),
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses()
                        )
                    )
                )
            )
        )
    }

    private fun getSlashPriceSetupProductListVariantMockResponseMultiWarehouse(): GetSlashPriceSetupProductListResponse {
        return GetSlashPriceSetupProductListResponse(
            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList(
                responseHeader = ResponseHeader(
                    success = true
                ),
                productList = listOf(
                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList(
                        variants = listOf(
                            GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Variant(
                                warehouses = arrayListOf(
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses(),
                                    GetSlashPriceSetupProductListResponse.GetSlashPriceSetupProductList.ProductList.Warehouses()
                                )
                            )
                        )
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

    @Test
    fun `When apply bulk update on discount type percentage and all product abusive, then product price should remain the same`() {
        val mockDiscountSettings = getMockDiscountSettings(
            mockStartDateValue,
            mockEndDateValue,
            mockBulkApplyPercentageDiscountAmount,
            DiscountType.PERCENTAGE
        )
        val mockProductData = getMockListSetupProductVariantDataWithAllAbusive()
        viewModel.applyBulkUpdate(
            mockProductData,
            mockDiscountSettings,
            mockModeUpdate,
            mockSlashPriceStatusOngoing
        )
        val liveDataValue = viewModel.updatedProductListData.value
        liveDataValue?.forEachIndexed { indexProductData, productData ->
            productData.listProductVariant.forEachIndexed { indexProductVariant, productVariantData ->
                assert(productVariantData.slashPriceInfo.discountedPrice == mockProductData[indexProductData].listProductVariant[indexProductVariant].slashPriceInfo.discountedPrice)
            }
        }
    }

    @Test
    fun `When apply bulk update on discount type percentage and product slash price status is ongoing, then product start date should be matched with mocked product data`() {
        val mockDiscountSettings = getMockDiscountSettings(
            mockStartDateValue,
            mockEndDateValue,
            mockBulkApplyPercentageDiscountAmount,
            DiscountType.PERCENTAGE
        )
        val mockProductData = getMockListSetupProductVariantDataWithSlashPriceStatusOngoing()
        viewModel.applyBulkUpdate(
            mockProductData,
            mockDiscountSettings,
            mockModeUpdate,
            mockSlashPriceStatusOngoing
        )
        val liveDataValue = viewModel.updatedProductListData.value
        liveDataValue?.forEachIndexed { indexProductData, productData ->
            productData.listProductVariant.forEachIndexed { indexProductVariant, productVariantData ->
                assert(productVariantData.slashPriceInfo.startDate == mockProductData[indexProductData].listProductVariant[indexProductVariant].slashPriceInfo.startDate)
            }
        }
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
                listProductWarehouse = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                        discountedPercentage = 10,
                        discountedPrice = 1000,
                        originalPrice = 100000
                    )
                ),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true
                )
            ),
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(
                            ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                                discountedPercentage = 10,
                                discountedPrice = 1000,
                                originalPrice = 100000
                            )
                        )
                    )
                ),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true,
                    isVariant = true
                )
            )
        )
    }

    private fun getMockListSetupProductDataWithStartDateError(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    errorType = START_DATE_ERROR
                )
            )
        )
    }

    private fun getMockListSetupProductDataWithR2Abusive(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    errorType = R2_ABUSIVE_ERROR
                )
            )
        )
    }

    private fun getMockListSetupProductVariantDataWithAllAbusive(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(
                            ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                                discountedPercentage = 10,
                                discountedPrice = 1000,
                                originalPrice = 100000,
                                abusiveRule = true
                            )
                        )
                    )
                ),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true,
                    isVariant = true,
                    errorType = ALL_ABUSIVE_ERROR
                )
            )
        )
    }

    private fun getMockListSetupProductVariantDataWithSlashPriceStatusOngoing(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(
                            ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                                discountedPercentage = 10,
                                discountedPrice = 1000,
                                originalPrice = 100000,
                            )
                        ),
                        slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                            slashPriceStatusId = DiscountStatus.ONGOING.toString(),
                            startDate = mockStartDateValue
                        )
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
                listProductWarehouse = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                        originalPrice = 100000
                    )
                )
            ),
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(
                            ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                                originalPrice = 100000
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getMockListSetupProductDataWithDiscountButError(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId1,
                listProductWarehouse = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                        originalPrice = 100000
                    )
                ),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true,
                    errorType = VALUE_ERROR
                )
            ),
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        listProductWarehouse = listOf(
                            ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                                originalPrice = 100000
                            )
                        )
                    )
                ),
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isProductDiscounted = true,
                    errorType = VALUE_ERROR
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
    fun `When call checkShouldEnableButtonSubmit with all abusive error, then should not enable button submit`() {
        viewModel.checkShouldEnableButtonSubmit(
            getMockListSetupProductVariantDataWithAllAbusive()
        )
        assert(viewModel.enableButtonSubmitLiveData.value == false)
    }

    @Test
    fun `When call checkShouldEnableButtonSubmit with start date error, then should not enable button submit`() {
        viewModel.checkShouldEnableButtonSubmit(
            getMockListSetupProductDataWithStartDateError()
        )
        assert(viewModel.enableButtonSubmitLiveData.value == false)
    }

    @Test
    fun `When call checkShouldEnableButtonSubmit with r2 abusive error, then should not enable button submit`() {
        viewModel.checkShouldEnableButtonSubmit(
            getMockListSetupProductDataWithR2Abusive()
        )
        getMockListSetupProductDataWithStartDateError()
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
        } returns DoSlashPriceProductSubmissionResponse()
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
        } returns DoSlashPriceProductSubmissionResponse()
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
        } throws Exception()
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
        } returns DoSlashPriceProductReservationResponse()
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
        } returns DoSlashPriceProductReservationResponse()
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
        } throws Exception()
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

    @Test
    fun `When checkStartDateError with start date error, then product error should be start date error`() {
        viewModel.checkStartDateError(
            getMockListSetupProductDataWithStartDateLessThanFiveMinutesFromNow(),
            mockModeCreate,
            DiscountStatus.ONGOING
        )
        val liveData = viewModel.updatedProductListData.value
        assert(liveData?.all {
            it.productStatus.errorType == START_DATE_ERROR
        } == true)
    }

    @Test
    fun `When checkStartDateError with correct start date, then product error should be no error`() {
        viewModel.checkStartDateError(
            getMockListSetupProductDataWithStartDateLessThanFiveMinutesFromNow2(),
            mockModeCreate,
            DiscountStatus.ONGOING
        )
        val liveData = viewModel.updatedProductListData.value
        assert(liveData?.all {
            it.productStatus.errorType == NO_ERROR
        } == true)
    }

    private fun getMockListSetupProductDataWithStartDateLessThanFiveMinutesFromNow(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        val mockStartDate = Calendar.getInstance().apply {
            add(Calendar.MINUTE, -3)
        }.time
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isVariant = true
                ),
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        variantStatus = ShopDiscountSetupProductUiModel.SetupProductData.VariantStatus(
                            isVariantEnabled = true
                        ),
                        slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                            startDate = mockStartDate
                        )
                    )
                )
            )
        )
    }

    private fun getMockListSetupProductDataWithStartDateLessThanFiveMinutesFromNow2(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        val mockStartDate = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 10)
        }.time
        return listOf(
            ShopDiscountSetupProductUiModel.SetupProductData(
                productId = mockProductId2,
                productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                    isVariant = true
                ),
                listProductVariant = listOf(
                    ShopDiscountSetupProductUiModel.SetupProductData(
                        variantStatus = ShopDiscountSetupProductUiModel.SetupProductData.VariantStatus(
                            isVariantEnabled = true
                        ),
                        slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                            startDate = mockStartDate
                        )
                    )
                )
            )
        )
    }

}