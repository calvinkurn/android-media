package com.tokopedia.tkpd.flashsale.presentation.manageproductlist

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.campaign.utils.constant.TickerConstant
import com.tokopedia.campaign.utils.constant.TickerType
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.mapper.FlashSaleMonitorSubmitProductSseMapper
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.domain.entity.ProductDeleteResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.usecase.*
import com.tokopedia.tkpd.flashsale.presentation.common.constant.ValueConstant.SHARED_PREF_MANAGE_PRODUCT_LIST_COACH_MARK
import com.tokopedia.tkpd.flashsale.presentation.detail.DummyDataHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.adapter.item.FlashSaleManageProductListItem
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel.FlashSaleManageProductListUiEffect
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.uimodel.FlashSaleManageProductListUiEvent
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*

@ExperimentalCoroutinesApi
class FlashSaleManageProductListViewModelTest {

    @RelaxedMockK
    lateinit var getFlashSaleReservedProductListUseCase: GetFlashSaleReservedProductListUseCase

    @RelaxedMockK
    lateinit var getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase

    @RelaxedMockK
    lateinit var doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase

    @RelaxedMockK
    lateinit var doFlashSaleProductSubmissionUseCase: DoFlashSaleProductSubmissionUseCase

    @RelaxedMockK
    lateinit var flashSaleTkpdProductSubmissionMonitoringSse: FlashSaleTkpdProductSubmissionMonitoringSse

    @RelaxedMockK
    lateinit var sharedPreferences: SharedPreferences

    @RelaxedMockK
    lateinit var getFlashSaleProductSubmissionProgressUseCase: GetFlashSaleProductSubmissionProgressUseCase

    @RelaxedMockK
    lateinit var flashSaleMonitorSubmitProductSseMapper: FlashSaleMonitorSubmitProductSseMapper

    @RelaxedMockK
    lateinit var doFlashSaleProductSubmitAcknowledgeUseCase: DoFlashSaleProductSubmitAcknowledgeUseCase

    @RelaxedMockK
    lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    private lateinit var viewModel: FlashSaleManageProductListViewModel
    private val mockCampaignId = "123"
    private val mockPage = 1

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = FlashSaleManageProductListViewModel(
            testCoroutineRule.dispatchers,
            getFlashSaleReservedProductListUseCase,
            getFlashSaleDetailForSellerUseCase,
            doFlashSaleProductDeleteUseCase,
            doFlashSaleProductSubmissionUseCase,
            flashSaleTkpdProductSubmissionMonitoringSse,
            sharedPreferences,
            getTargetedTickerUseCase,
            getFlashSaleProductSubmissionProgressUseCase,
            flashSaleMonitorSubmitProductSseMapper,
            doFlashSaleProductSubmitAcknowledgeUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ui event does not matched anything, then ui effect should be null`() {
        testCoroutineRule.runTest {
            viewModel.processEvent(mockk())
            var emittedValue: FlashSaleManageProductListUiEffect? = null
            val job = launch {
                emittedValue = viewModel.uiEffect.firstOrNull()
            }
            assert(emittedValue == null)
            job.cancel()
        }
    }

    @Test
    fun `When get reserved product list success, then product list shouldn't empty`() {
        runTest {
            coEvery {
                getFlashSaleReservedProductListUseCase.execute(any())
            } returns getMockedReservedProductData()
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.GetReservedProductList(
                    mockCampaignId,
                    mockPage
                )
            )
            advanceUntilIdle()
            assert(viewModel.uiState.first().listDelegateItem.isNotEmpty())
        }
    }

    @Test
    fun `When get reserved product list error, then products should be empty and should show error`() {
        runTest {
            val mockError = Exception()
            coEvery {
                getFlashSaleReservedProductListUseCase.execute(any())
            } throws mockError
            val expectedUiEffect =
                FlashSaleManageProductListUiEffect.ShowErrorGetReservedProductList(
                    mockError
                )
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.GetReservedProductList(
                    mockCampaignId,
                    mockPage
                )
            )
            advanceUntilIdle()
            assert(viewModel.uiState.first().listDelegateItem.isEmpty())
            Assert.assertEquals(viewModel.uiEffect.first(), expectedUiEffect)
        }
    }

    @Test
    fun `When get campaign detail bottom sheet data success, then ui effect should emit AddIconCampaignDetailBottomSheet`() {
        testCoroutineRule.runTest {
            val expectedUiEffect =
                FlashSaleManageProductListUiEffect.AddIconCampaignDetailBottomSheet(
                    DummyDataHelper.generateDummyGeneralBottomSheetModel()
                )
            coEvery {
                getFlashSaleDetailForSellerUseCase.execute(mockCampaignId.toLongOrZero())
            } returns DummyDataHelper.generateDummyFlashSaleData()
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.GetCampaignDetailBottomSheet(
                    mockCampaignId
                )
            )
            Assert.assertEquals(viewModel.uiEffect.first(), expectedUiEffect)
        }
    }

    @Test
    fun `When get campaign detail bottom sheet data error, then ui effect should emit nothing`() {
        testCoroutineRule.runTest {
            coEvery {
                getFlashSaleDetailForSellerUseCase.execute(mockCampaignId.toLongOrZero())
            } throws Exception()
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.GetCampaignDetailBottomSheet(
                    mockCampaignId
                )
            )
            var emittedValue: FlashSaleManageProductListUiEffect? = null
            val job = launch {
                emittedValue = viewModel.uiEffect.firstOrNull()
            }
            assert(emittedValue == null)
            job.cancel()
        }
    }

    @Test
    fun `When delete product from reservation success with current product not empty and deleted product matched, then product list size should be updated`() {
        runBlockingTest {
            val mockCurrentReservedProductData = getMockedReservedProductData()
            getReservedProductList(mockCurrentReservedProductData)
            coEvery {
                doFlashSaleProductDeleteUseCase.execute(any())
            } returns ProductDeleteResult(true, "")
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.DeleteProductFromReserved(
                    ReservedProduct.Product(
                        productId = 1234
                    ),
                    "",
                    mockCampaignId
                )
            )
            assert(viewModel.uiState.first().listDelegateItem.size != mockCurrentReservedProductData.products.size)
        }
    }

    @Test
    fun `When delete product from reservation success with current product list is not empty and deleted product not matched, then product list should be the same`() {
        runBlockingTest {
            val mockCurrentReservedProductData = getMockedReservedProductData()
            getReservedProductList(mockCurrentReservedProductData)
            coEvery {
                doFlashSaleProductDeleteUseCase.execute(any())
            } returns ProductDeleteResult(true, "")
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.DeleteProductFromReserved(
                    ReservedProduct.Product(),
                    "",
                    mockCampaignId
                )
            )
            assert(viewModel.uiState.first().listDelegateItem.size == mockCurrentReservedProductData.products.size)
        }
    }

    @Test
    fun `When delete product from reservation success with current product list empty, then product list should be empty`() {
        testCoroutineRule.runTest {
            coEvery {
                doFlashSaleProductDeleteUseCase.execute(any())
            } returns ProductDeleteResult(true, "message")
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.DeleteProductFromReserved(
                    ReservedProduct.Product(),
                    "",
                    mockCampaignId
                )
            )
            assert(viewModel.uiState.first().listDelegateItem.isEmpty())
        }
    }

    @Test
    fun `When delete product from reservation not success, then should emit ShowToasterErrorDelete`() {
        testCoroutineRule.runTest {
            val mockErrorMessage = "errorMessage"
            coEvery {
                doFlashSaleProductDeleteUseCase.execute(any())
            } returns ProductDeleteResult(false, mockErrorMessage)
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.DeleteProductFromReserved(
                    ReservedProduct.Product(),
                    "",
                    mockCampaignId
                )
            )

            val emittedValue = viewModel.uiEffect.first()
            assert(emittedValue is FlashSaleManageProductListUiEffect.ShowToasterErrorDelete)
            assert((emittedValue as FlashSaleManageProductListUiEffect.ShowToasterErrorDelete).throwable.message == mockErrorMessage)
        }
    }

    @Test
    fun `When check enable button submit with existing product empty, then should emit ConfigSubmitButton with false value`() {
        testCoroutineRule.runTest {
            viewModel.processEvent(FlashSaleManageProductListUiEvent.CheckShouldEnableButtonSubmit)
            val emittedValue = viewModel.uiEffect.first()
            Assert.assertEquals(
                emittedValue,
                FlashSaleManageProductListUiEffect.ConfigSubmitButton(false)
            )
        }
    }

    @Test
    fun `When check enable button submit with existing product not empty and not all product discounted, then should emit ConfigSubmitButton with false value`() {
        runBlockingTest {
            val mockCurrentReservedProductData = getMockedReservedProductData()
            getReservedProductList(mockCurrentReservedProductData)
            viewModel.processEvent(FlashSaleManageProductListUiEvent.CheckShouldEnableButtonSubmit)
            val emittedValue = viewModel.uiEffect.first()
            Assert.assertEquals(
                emittedValue,
                FlashSaleManageProductListUiEffect.ConfigSubmitButton(false)
            )
        }
    }

    @Test
    fun `When check enable button submit with existing product not empty and all product discounted, then should emit ConfigSubmitButton with true value`() {
        runBlockingTest {
            val mockCurrentReservedProductData = getMockedAllDiscountedReservedProductData()
            getReservedProductList(mockCurrentReservedProductData)
            viewModel.processEvent(FlashSaleManageProductListUiEvent.CheckShouldEnableButtonSubmit)
            val emittedValue = viewModel.uiEffect.first()
            Assert.assertEquals(
                emittedValue,
                FlashSaleManageProductListUiEffect.ConfigSubmitButton(true)
            )
        }
    }

    @Test
    fun `When loadNextReservedProductList success, then product list should not empty`() {
        testCoroutineRule.runTest {
            val mockReservationId = "12"
            coEvery {
                getFlashSaleReservedProductListUseCase.execute(any())
            } returns getMockedReservedProductData()
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.LoadNextReservedProduct(
                    mockReservationId,
                    1
                )
            )
            val emittedValue = viewModel.uiState.first()
            assert(emittedValue.listDelegateItem.isNotEmpty())
        }
    }

    @Test
    fun `When loadNextReservedProductList error, then should emit ShowErrorLoadNextReservedProductList`() {
        testCoroutineRule.runTest {
            val mockReservationId = "12"
            val mockException = Exception("error")
            coEvery {
                getFlashSaleReservedProductListUseCase.execute(any())
            } throws mockException
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.LoadNextReservedProduct(
                    mockReservationId,
                    1
                )
            )
            val emittedValue = viewModel.uiEffect.first()
            Assert.assertEquals(
                emittedValue,
                FlashSaleManageProductListUiEffect.ShowErrorLoadNextReservedProductList(
                    mockException
                )
            )
        }
    }

    @Test
    fun `When submitDiscountedProduct non sse success, then should emit OnProductSubmitted`() {
        runTest {
            val mockCurrentReservedProductData = getMockedDiscountedReservedProductDataWithParentAndChild()
            getReservedProductList(mockCurrentReservedProductData)
            val mockProductSubmissionResult = ProductSubmissionResult(
                true,
                "",
                1,
                "",
                false
            )
            coEvery {
                doFlashSaleProductSubmissionUseCase.execute(any())
            } returns mockProductSubmissionResult
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.SubmitDiscountedProduct(
                    "",
                    ""
                )
            )
            advanceUntilIdle()
            val emittedValue = viewModel.uiEffect.first()
            Assert.assertEquals(
                emittedValue,
                FlashSaleManageProductListUiEffect.OnProductSubmitted(mockProductSubmissionResult)
            )
        }
    }

    @Test
    fun `When submitDiscountedProduct non sse error, then should emit ShowErrorSubmitDiscountedProduct`() {
        testCoroutineRule.runTest {
            val mockException = Exception("error")
            coEvery {
                doFlashSaleProductSubmissionUseCase.execute(any())
            } throws mockException
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.SubmitDiscountedProduct(
                    "",
                    ""
                )
            )
            val emittedValue = viewModel.uiEffect.first()
            Assert.assertEquals(
                emittedValue,
                FlashSaleManageProductListUiEffect.ShowErrorSubmitDiscountedProduct(mockException)
            )
        }
    }

    @Test
    fun `When submitDiscountedProduct sse success, then sse should call listen`() {
        testCoroutineRule.runTest {
            val mockProductSubmissionResult = ProductSubmissionResult(
                true,
                "",
                1,
                "",
                true
            )
            coEvery {
                doFlashSaleProductSubmissionUseCase.execute(any())
            } returns mockProductSubmissionResult
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.SubmitDiscountedProduct(
                    "",
                    ""
                )
            )
            verify { flashSaleTkpdProductSubmissionMonitoringSse.listen() }
        }
    }

    @Test
    fun `When UpdateProductData with empty current product list, then current product list should be empty`() {
        testCoroutineRule.runTest {
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.UpdateProductData(
                    ReservedProduct.Product()
                )
            )
            val emittedValue = viewModel.uiState.first()
            assert(emittedValue.listDelegateItem.isEmpty())
        }
    }

    @Test
    fun `When UpdateProductData and matched with current product list, then current product list should be updated`() {
        runBlockingTest {
            val mockUpdatedProductData = ReservedProduct.Product(
                productId = 12352,
                name = "test"
            )
            val mockCurrentReservedProductData = getMockedDiscountedReservedProductDataWithParentAndChild()
            getReservedProductList(mockCurrentReservedProductData)
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.UpdateProductData(
                    mockUpdatedProductData
                )
            )
            val emittedValue = viewModel.uiState.first()
            val updatedCurrentProduct = (emittedValue.listDelegateItem.first() as FlashSaleManageProductListItem).product
            assert(updatedCurrentProduct.name == mockUpdatedProductData.name)
        }
    }

    @Test
    fun `When rollence value list data is provided, then should get success ticker list result & show ticker`() {
        runBlockingTest {
            val tickerListData = listOf(
                RemoteTicker(
                    title = "some ticker title",
                    description = "some ticker description",
                    type = TickerType.INFO,
                    actionLabel = "some ticker action label",
                    actionType = "link",
                    actionAppUrl = "https://tokopedia.com"
                )
            )

            val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
            val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
                GetTargetedTickerUseCase.Param.Target(
                    type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                    values = rollenceValueList
                )
            )
            val tickerParams = GetTargetedTickerUseCase.Param(
                page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_MANAGE_PRODUCT,
                targets = targetParams
            )

            coEvery { getTargetedTickerUseCase.execute(tickerParams) } returns tickerListData

            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.GetTickerData(
                    rollenceValueList = rollenceValueList
                )
            )

            val emittedValue = viewModel.uiState.first()
            Assert.assertEquals(true, emittedValue.showTicker)
            assert(emittedValue.tickerList.isNotEmpty())
        }
    }

    @Test
    fun `When UpdateProductData and not matched with current product list, then current product list should not be updated`() {
        runBlockingTest {
            val mockUpdatedProductData = ReservedProduct.Product(
                productId = 5,
                name = "test"
            )
            val mockCurrentReservedProductData = getMockedDiscountedReservedProductDataWithParentAndChild()
            getReservedProductList(mockCurrentReservedProductData)
            viewModel.processEvent(
                FlashSaleManageProductListUiEvent.UpdateProductData(
                    mockUpdatedProductData
                )
            )
            val emittedValue = viewModel.uiState.first()
            val updatedCurrentProduct = (emittedValue.listDelegateItem.first() as FlashSaleManageProductListItem).product
            assert(updatedCurrentProduct.name != mockUpdatedProductData.name)
        }
    }

    @Test
    fun `When listenToExistingSse called and success, then sse should call listen`() {
        testCoroutineRule.runTest {
            viewModel.listenToExistingSse(mockCampaignId)
            verify { flashSaleTkpdProductSubmissionMonitoringSse.listen() }
        }
    }

    @Test
    fun `When listenToExistingSse called and error when connect, then sse should not call listen`() {
        testCoroutineRule.runTest {
            every { flashSaleTkpdProductSubmissionMonitoringSse.connect(any()) } throws Exception()
            viewModel.listenToExistingSse(mockCampaignId)
            verify(exactly = 0) { flashSaleTkpdProductSubmissionMonitoringSse.listen() }
        }
    }

    @Test
    fun `When setSharedPrefCoachMarkAlreadyShown called, then sharedPref should call putBoolean with expected parameter`() {
        testCoroutineRule.runTest {
            viewModel.setSharedPrefCoachMarkAlreadyShown()
            verify { sharedPreferences.edit().putBoolean(SHARED_PREF_MANAGE_PRODUCT_LIST_COACH_MARK, true).apply() }
        }
    }

    @Test
    fun `When acknowledgeProductSubmissionSse called and success, then should emit OnSuccessAcknowledgeProductSubmissionSse`() {
        testCoroutineRule.runTest {
            val mockTotalSubmittedProduct = 1
            coEvery {
                doFlashSaleProductSubmitAcknowledgeUseCase.execute(any())
            } returns true
            viewModel.acknowledgeProductSubmissionSse(mockCampaignId, mockTotalSubmittedProduct)
            val emittedValue = viewModel.uiEffect.first()
            assert((emittedValue as FlashSaleManageProductListUiEffect.OnSuccessAcknowledgeProductSubmissionSse).totalSubmittedProduct == mockTotalSubmittedProduct)
        }
    }

    @Test
    fun `When acknowledgeProductSubmissionSse called and success but return false, then should not emit OnSuccessAcknowledgeProductSubmissionSse`() {
        testCoroutineRule.runTest {
            val mockTotalSubmittedProduct = 1
            coEvery {
                doFlashSaleProductSubmitAcknowledgeUseCase.execute(any())
            } returns false
            viewModel.acknowledgeProductSubmissionSse(mockCampaignId, mockTotalSubmittedProduct)
            var emittedValue: FlashSaleManageProductListUiEffect? = null
            val job = launch {
                emittedValue = viewModel.uiEffect.firstOrNull()
            }
            assert(emittedValue == null)
            job.cancel()
        }
    }

    @Test
    fun `When acknowledgeProductSubmissionSse called and error, then should not emit OnSuccessAcknowledgeProductSubmissionSse`() {
        testCoroutineRule.runTest {
            coEvery {
                doFlashSaleProductSubmitAcknowledgeUseCase.execute(any())
            } throws Exception()
            viewModel.acknowledgeProductSubmissionSse(mockCampaignId, 1)
            var emittedValue: FlashSaleManageProductListUiEffect? = null
            val job = launch {
                emittedValue = viewModel.uiEffect.firstOrNull()
            }
            assert(emittedValue == null)
            job.cancel()
        }
    }

    @Test
    fun `When getFlashSaleSubmissionProgress called and success and sse is opened, then should emit OnSseOpen`() {
        testCoroutineRule.runTest {
            coEvery {
                getFlashSaleProductSubmissionProgressUseCase.execute(any())
            } returns FlashSaleProductSubmissionProgress(
                isOpenSse = true
            )
            viewModel.getFlashSaleSubmissionProgress(mockCampaignId)
            val emittedValue = viewModel.uiEffect.first()
            assert(emittedValue is FlashSaleManageProductListUiEffect.OnSseOpen)
        }
    }

    @Test
    fun `When getFlashSaleSubmissionProgress called and success and sse is not opened, then should not emit OnSseOpen`() {
        testCoroutineRule.runTest {
            coEvery {
                getFlashSaleProductSubmissionProgressUseCase.execute(any())
            } returns FlashSaleProductSubmissionProgress(
                isOpenSse = false
            )
            viewModel.getFlashSaleSubmissionProgress(mockCampaignId)
            var emittedValue: FlashSaleManageProductListUiEffect? = null
            val job = launch {
                emittedValue = viewModel.uiEffect.firstOrNull()
            }
            assert(emittedValue == null)
            job.cancel()
        }
    }

    @Test
    fun `When getFlashSaleSubmissionProgress called and error, then should not emit OnSseOpen`() {
        testCoroutineRule.runTest {
            coEvery {
                getFlashSaleProductSubmissionProgressUseCase.execute(any())
            } throws Exception()
            viewModel.getFlashSaleSubmissionProgress(mockCampaignId)
            var emittedValue: FlashSaleManageProductListUiEffect? = null
            val job = launch {
                emittedValue = viewModel.uiEffect.firstOrNull()
            }
            assert(emittedValue == null)
            job.cancel()
        }
    }

    @Test
    fun `When onCleared called, then should close SSE`() {
        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)
        verify { flashSaleTkpdProductSubmissionMonitoringSse.closeSSE() }
    }

    private fun getMockedReservedProductData(): ReservedProduct {
        return ReservedProduct(
            listOf(
                ReservedProduct.Product(
                    productId = 1234
                ),
                ReservedProduct.Product(
                    productId = 1235,
                    warehouses = listOf(
                        ReservedProduct.Product.Warehouse(
                            warehouseId = 1,
                            isDisabled = false,
                            discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                                stock = 22,
                                price = 12345,
                                discount = 10
                            ),
                            price = 5123,
                            stock = 12,
                            name = "",
                            disabledReason = "",
                            isDilayaniTokopedia = false,
                            isToggleOn = true
                        )
                    )

                )
            ),
            10
        )
    }

    private fun getMockedAllDiscountedReservedProductData(): ReservedProduct {
        return ReservedProduct(
            listOf(
                ReservedProduct.Product(
                    productId = 12352,
                    warehouses = listOf(
                        ReservedProduct.Product.Warehouse(
                            warehouseId = 1,
                            isDisabled = false,
                            discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                                stock = 22,
                                price = 12345,
                                discount = 10
                            ),
                            price = 5123,
                            stock = 12,
                            name = "",
                            disabledReason = "",
                            isDilayaniTokopedia = false,
                            isToggleOn = true
                        )
                    )
                ),
                ReservedProduct.Product(
                    productId = 1235,
                    warehouses = listOf(
                        ReservedProduct.Product.Warehouse(
                            warehouseId = 1,
                            isDisabled = false,
                            discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                                stock = 22,
                                price = 12345,
                                discount = 10
                            ),
                            price = 5123,
                            stock = 12,
                            name = "",
                            disabledReason = "",
                            isDilayaniTokopedia = false,
                            isToggleOn = true
                        )
                    )

                )
            ),
            10
        )
    }

    private fun getMockedDiscountedReservedProductDataWithParentAndChild(): ReservedProduct {
        return ReservedProduct(
            listOf(
                ReservedProduct.Product(
                    productId = 12352,
                    isParentProduct = true,
                    childProducts = listOf(
                        ReservedProduct.Product.ChildProduct(
                            disabledReason = "",
                            isDisabled = false,
                            isMultiwarehouse = false,
                            isToggleOn = true,
                            name = "",
                            picture = "",
                            price = ReservedProduct.Product.Price(),
                            productCriteria = ReservedProduct.Product.ProductCriteria(),
                            discountedPrice = 123,
                            discount = 521,
                            productId = 215213,
                            sku = "",
                            stock = 12,
                            url = "",
                            warehouses = listOf(
                                ReservedProduct.Product.Warehouse(
                                    warehouseId = 1,
                                    isDisabled = false,
                                    discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                                        stock = 22,
                                        price = 12345,
                                        discount = 10
                                    ),
                                    price = 5123,
                                    stock = 12,
                                    name = "",
                                    disabledReason = "",
                                    isDilayaniTokopedia = false,
                                    isToggleOn = true
                                )
                            )
                        )
                    )
                ),
                ReservedProduct.Product(
                    productId = 1235,
                    warehouses = listOf(
                        ReservedProduct.Product.Warehouse(
                            warehouseId = 1,
                            isDisabled = false,
                            discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                                stock = 22,
                                price = 12345,
                                discount = 10
                            ),
                            price = 5123,
                            stock = 12,
                            name = "",
                            disabledReason = "",
                            isDilayaniTokopedia = false,
                            isToggleOn = true
                        )
                    )

                )
            ),
            10
        )
    }

    private fun getReservedProductList(
        mockCurrentReservedProductData: ReservedProduct
    ) = runTest {
        coEvery {
            getFlashSaleReservedProductListUseCase.execute(any())
        } returns mockCurrentReservedProductData
        viewModel.processEvent(
            FlashSaleManageProductListUiEvent.GetReservedProductList(
                mockCampaignId,
                mockPage
            )
        )
        advanceUntilIdle()
    }
}
