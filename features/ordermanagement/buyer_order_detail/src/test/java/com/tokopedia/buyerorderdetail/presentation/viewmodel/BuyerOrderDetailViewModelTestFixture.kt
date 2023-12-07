package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.AddToCartSingleRequestState
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.PlusComponent
import com.tokopedia.buyerorderdetail.domain.models.PlusDetail
import com.tokopedia.buyerorderdetail.domain.models.PlusFooter
import com.tokopedia.buyerorderdetail.domain.models.PlusSavings
import com.tokopedia.buyerorderdetail.domain.models.PlusTicker
import com.tokopedia.buyerorderdetail.domain.models.PlusTotal
import com.tokopedia.buyerorderdetail.domain.usecases.FinishOrderUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetBuyerOrderDetailDataUseCase
import com.tokopedia.buyerorderdetail.presentation.mapper.ActionButtonsUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.EpharmacyInfoUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.OrderStatusUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.ProductListUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.SavingsWidgetUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.model.EpharmacyInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokochat.config.domain.TokoChatCounterUseCase
import com.tokopedia.tokochat.config.domain.TokoChatGroupBookingUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BuyerOrderDetailViewModelTestFixture {

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getBuyerOrderDetailDataUseCase: GetBuyerOrderDetailDataUseCase

    @RelaxedMockK
    lateinit var finishOrderUseCase: FinishOrderUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartMultiUseCase

    @RelaxedMockK
    lateinit var tokoChatGroupBookingUseCase: TokoChatGroupBookingUseCase

    @RelaxedMockK
    lateinit var tokoChatCounterUseCase: TokoChatCounterUseCase

    lateinit var viewModel: BuyerOrderDetailViewModel

    @get:Rule
    val rule = UnconfinedTestRule()

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val orderId = "166835036"
    val orderStatusId = "220"
    val paymentId = "1234567890"
    val cart = "1234567890"
    val userId = "10001"
    val shopId = "10002"
    val shopName = "Test Toko"
    val shopType = 10

    val product = ProductListUiModel.ProductUiModel(
        button = ActionButtonsUiModel.ActionButton(
            key = "test_buy_again_button_key",
            label = "Beli Lagi",
            popUp = ActionButtonsUiModel.ActionButton.PopUp(
                actionButton = emptyList(),
                body = "",
                title = ""
            ),
            variant = "ghost",
            type = "main",
            url = ""
        ),
        category = "Pakaian Atas",
        categoryId = "10",
        orderDetailId = "20531238",
        orderStatusId = "220",
        orderId = "166835036",
        price = 500000.0,
        priceText = "Rp500.000",
        productId = "2147819914",
        productName = "Hengpong jadul",
        productNote = "Test product note",
        productThumbnailUrl = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2021/5/28/ab64b25e-a59f-4938-a08b-c49ec140eb43.jpg",
        quantity = 1,
        totalPrice = "500000",
        totalPriceText = "Rp500.000",
        isProcessing = false,
        productUrl = ""
    )

    val atcExpectedParams = arrayListOf(
        AddToCartMultiParam(
            productId = product.productId,
            productName = product.productName,
            productPrice = product.price,
            qty = product.quantity,
            notes = product.productNote,
            shopId = shopId,
            custId = userId,
            warehouseId = "0"
        )
    )

    val additionalEpharmacyData =
        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BomAdditionalData(
            GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BomAdditionalData.EpharmacyData(
                consultationName = "halodoc",
                consultationDoctorName = "yusuf hendrawan"
            ),
            PlusSavings(
                    plusTicker = PlusTicker(
                        leftText = "asd",
                        rightText = "asd",
                        imageUrl = "asd"
                    ),
            plusComponents = PlusComponent(
                plusDetailComponents = listOf(
                    PlusDetail(
                        label = "asd",
                        value = "asd",
                        imageUrl = "asd"
                    )
                ),
                plusFooter = PlusFooter(
                    PlusTotal(
                    footerLabel = "asd",
                    footerValue = "asd"
                )
                )
            )
        )
        )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BuyerOrderDetailViewModel(
            atcMultiQuery = { "" },
            userSession = { userSession },
            getBuyerOrderDetailDataUseCase = { getBuyerOrderDetailDataUseCase },
            finishOrderUseCase = { finishOrderUseCase },
            atcUseCase = { atcUseCase },
            tokoChatGroupBookingUseCase = { tokoChatGroupBookingUseCase },
            tokoChatCounterUseCase = { tokoChatCounterUseCase },
            resourceProvider = { resourceProvider }
        )

        every { userSession.userId } returns userId
        mockkObject(EpharmacyInfoUiStateMapper)
        mockkStatic(TrackApp::class)
        justRun { TrackApp.getInstance().gtm.sendGeneralEvent(any()) }
        mockkObject(SavingsWidgetUiStateMapper)
    }

    @After
    fun cleanup() {
        unmockkAll()
        viewModel.viewModelScope.coroutineContext.cancelChildren()
        unmockkAll()
    }

    fun createSuccessGetBuyerOrderDetailDataResult(
        getBuyerOrderDetailResult: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail = mockk(relaxed = true) {
            every { getPodInfo() } returns null
            every { additionalData } returns additionalEpharmacyData
        },
        getOrderResolutionResult: GetOrderResolutionResponse.ResolutionGetTicketStatus.ResolutionData = mockk(relaxed = true),
        getInsuranceDetailResult: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data = mockk(relaxed = true),
        actionBeforeComplete: () -> Unit = {}
    ) {
        coEvery {
            getBuyerOrderDetailDataUseCase(any())
        } answers {
            flow {
                emit(
                    GetBuyerOrderDetailDataRequestState.Requesting(
                        GetP0DataRequestState.Requesting(GetBuyerOrderDetailRequestState.Requesting),
                        GetP1DataRequestState.Requesting(
                            GetOrderResolutionRequestState.Requesting,
                            GetInsuranceDetailRequestState.Requesting
                        )
                    )
                )
                delay(1)
                actionBeforeComplete()
                emit(
                    GetBuyerOrderDetailDataRequestState.Complete(
                        GetP0DataRequestState.Complete(
                            GetBuyerOrderDetailRequestState.Complete.Success(getBuyerOrderDetailResult)
                        ),
                        GetP1DataRequestState.Complete(
                            GetOrderResolutionRequestState.Complete.Success(getOrderResolutionResult),
                            GetInsuranceDetailRequestState.Complete.Success(getInsuranceDetailResult)
                        )
                    )
                )
            }
        }
    }

    fun createFailedGetBuyerOrderDetailDataResult(throwable: Throwable = mockk(relaxed = true)) {
        coEvery {
            getBuyerOrderDetailDataUseCase(any())
        } returns flow {
            emit(
                GetBuyerOrderDetailDataRequestState.Requesting(
                    GetP0DataRequestState.Requesting(
                        GetBuyerOrderDetailRequestState.Requesting
                    ),
                    GetP1DataRequestState.Requesting(
                        GetOrderResolutionRequestState.Requesting,
                        GetInsuranceDetailRequestState.Requesting
                    )
                )
            )
            emit(
                GetBuyerOrderDetailDataRequestState.Complete(
                    GetP0DataRequestState.Complete(
                        GetBuyerOrderDetailRequestState.Complete.Error(throwable)
                    ),
                    GetP1DataRequestState.Complete(
                        GetOrderResolutionRequestState.Complete.Error(throwable),
                        GetInsuranceDetailRequestState.Complete.Error(throwable)
                    )
                )
            )
        }
    }

    fun createSuccessFinishOrderResult(result: FinishOrderResponse.Data.FinishOrderBuyer = mockk(relaxed = true)) {
        coEvery {
            finishOrderUseCase.execute(any())
        } returns result
    }

    fun createFailedFinishOrderResult(throwable: Throwable = Throwable()) {
        coEvery {
            finishOrderUseCase.execute(any())
        } throws throwable
    }

    fun createSuccessATCResult(result: com.tokopedia.usecase.coroutines.Result<AtcMultiData> = Success(mockk(relaxed = true))) {
        coEvery {
            atcUseCase.execute(any(), any(), any())
        } returns result
    }

    fun createFailedATCResult(throwable: Throwable = Throwable()) {
        coEvery {
            atcUseCase.execute(any(), any(), any())
        } throws throwable
    }

    fun mockOrderStatusUiStateMapper(
        loadingState: OrderStatusUiState.Loading = mockk(relaxed = true),
        reloadingState: OrderStatusUiState.HasData.Reloading = mockk(relaxed = true),
        showingState: OrderStatusUiState.HasData.Showing = mockk(relaxed = true),
        errorState: OrderStatusUiState.Error = mockk(relaxed = true),
        block: () -> Unit
    ) {
        mockkObject(OrderStatusUiStateMapper, recordPrivateCalls = true) {
            every {
                OrderStatusUiStateMapper["mapOnLoading"]()
            } returns loadingState
            every {
                OrderStatusUiStateMapper["mapOnReloading"](
                    any<OrderStatusUiState.HasData>()
                )
            } returns reloadingState
            every {
                OrderStatusUiStateMapper["mapOnDataReady"](
                    any<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail>()
                )
            } returns showingState
            every {
                OrderStatusUiStateMapper["mapOnError"](any<Throwable>())
            } returns errorState
            block()
        }
    }

    fun mockProductListUiStateMapper(
        loadingState: ProductListUiState.Loading = mockk(relaxed = true),
        reloadingState: ProductListUiState.HasData.Reloading = mockk(relaxed = true),
        showingState: ProductListUiState.HasData.Showing = mockk(relaxed = true),
        errorState: ProductListUiState.Error = mockk(relaxed = true),
        block: () -> Unit
    ) {
        mockkObject(ProductListUiStateMapper, recordPrivateCalls = true) {
            every {
                ProductListUiStateMapper["mapOnLoading"]()
            } returns loadingState
            every {
                ProductListUiStateMapper["mapOnReloading"](
                    any<ProductListUiState.HasData>()
                )
            } returns reloadingState
            every {
                ProductListUiStateMapper["mapOnDataReady"](
                    any<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail>(),
                    any<GetInsuranceDetailRequestState>(),
                    any<Map<String, AddToCartSingleRequestState>>(),
                    any<Boolean>(),
                    any<Boolean>()
                )
            } returns showingState
            every {
                ProductListUiStateMapper["mapOnError"](any<Throwable>())
            } returns errorState
            block()
        }
    }

    fun mockActionButtonsUiStateMapper(
        loadingState: ActionButtonsUiState.Loading = mockk(relaxed = true),
        reloadingState: ActionButtonsUiState.HasData.Reloading = mockk(relaxed = true),
        showingState: ActionButtonsUiState.HasData.Showing = mockk(relaxed = true),
        errorState: ActionButtonsUiState.Error = mockk(relaxed = true),
        block: () -> Unit
    ) {
        mockkObject(ActionButtonsUiStateMapper, recordPrivateCalls = true) {
            every {
                ActionButtonsUiStateMapper["mapOnLoading"]()
            } returns loadingState
            every {
                ActionButtonsUiStateMapper["mapOnReloading"](
                    any<ActionButtonsUiState.HasData>()
                )
            } returns reloadingState
            every {
                ActionButtonsUiStateMapper["mapOnDataReady"](
                    any<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail>()
                )
            } returns showingState
            every {
                ActionButtonsUiStateMapper["mapOnError"](any<Throwable>())
            } returns errorState
            block()
        }
    }

    fun runCollectingUiState(block: suspend TestScope.(List<BuyerOrderDetailUiState>) -> Unit) = runTest {
        val uiStates = mutableListOf<BuyerOrderDetailUiState>()
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val uiStateCollectorJob = backgroundScope.launch(dispatcher) {
            viewModel.buyerOrderDetailUiState.toList(uiStates)
        }
        block(uiStates)
        uiStateCollectorJob.cancel()
    }

    fun EpharmacyInfoUiModel.isEmptyData(): Boolean {
        return this.consultationDate.isEmpty() &&
            this.consultationDoctorName.isEmpty() &&
            this.consultationExpiryDate.isEmpty() &&
            this.consultationPatientName.isEmpty() &&
            this.consultationName.isEmpty() &&
            this.consultationPrescriptionNumber.isEmpty()
    }

    fun isProductListCollapsed(): Boolean {
        return BuyerOrderDetailViewModel::class.java.getDeclaredField("productListCollapsed").run {
            isAccessible = true
            (get(viewModel) as MutableStateFlow<*>).value as Boolean
        }
    }

    fun isProductListExpanded(): Boolean {
        return BuyerOrderDetailViewModel::class.java.getDeclaredField("productListCollapsed").run {
            isAccessible = true
            !((get(viewModel) as MutableStateFlow<*>).value as Boolean)
        }
    }

    fun TestScope.getBuyerOrderDetailData(
        orderId: String = this@BuyerOrderDetailViewModelTestFixture.orderId,
        paymentId: String = this@BuyerOrderDetailViewModelTestFixture.paymentId,
        cart: String = this@BuyerOrderDetailViewModelTestFixture.cart,
        shouldCheckCache: Boolean = false
    ) {
        viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, shouldCheckCache)
        // skip debounce on viewModel#productListUiState
        advanceTimeBy(1050L)
    }
}
