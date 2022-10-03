package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataResponse
import com.tokopedia.buyerorderdetail.domain.models.GetResolutionTicketStatusResponse
import com.tokopedia.buyerorderdetail.domain.usecases.FinishOrderUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetP0DataUseCase
import com.tokopedia.buyerorderdetail.presentation.mapper.BuyerOrderDetailUiModelMapper
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BuyerOrderDetailViewModelTestFixture {

    @RelaxedMockK
    lateinit var buyerOrderDetailUiModelMapper: BuyerOrderDetailUiModelMapper

    @RelaxedMockK
    lateinit var gson: Gson

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getP0DataUseCase: GetP0DataUseCase

    @RelaxedMockK
    lateinit var finishOrderUseCase: FinishOrderUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartMultiUseCase

    lateinit var viewModel: BuyerOrderDetailViewModel

    @get:Rule
    val rule = CoroutineTestRule()

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
        productThumbnailUrl = "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/5/28/ab64b25e-a59f-4938-a08b-c49ec140eb43.jpg",
        quantity = 1,
        totalPrice = "500000",
        totalPriceText = "Rp500.000",
        isProcessing = false
    )

    val atcExpectedParams = arrayListOf(
        AddToCartMultiParam(
            productId = product.productId.toLong(),
            productName = product.productName,
            productPrice = product.price.toLong(),
            qty = product.quantity,
            notes = product.productNote,
            shopId = shopId.toInt(),
            custId = userId.toInt()
        )
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BuyerOrderDetailViewModel(
            atcMultiQuery = { "" },
            userSession = { userSession },
            getP0DataUseCase = { getP0DataUseCase },
            finishOrderUseCase = { finishOrderUseCase },
            atcUseCase = { atcUseCase },
            resourceProvider = { resourceProvider },
            gson = { gson },
            buyerOrderDetailUiModelMapper = { buyerOrderDetailUiModelMapper }
        )

        every { userSession.userId } returns userId
    }

    @After
    fun cleanup() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    fun createSuccessGetP0DataResult(
        getBuyerOrderDetailResult: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail = mockk(relaxed = true),
        getOrderResolutionResult: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData = mockk(relaxed = true),
    ) {
        coEvery {
            getP0DataUseCase.getP0Data(any())
        } returns flow {
            emit(GetP0DataRequestState.Requesting)
            emit(GetP0DataRequestState.Success(GetP0DataResponse(getBuyerOrderDetailResult, getOrderResolutionResult)))
        }
    }

    fun createFailedGetP0DataResult(throwable: Throwable = mockk(relaxed = true)) {
        coEvery {
            getP0DataUseCase.getP0Data(any())
        } returns flow {
            emit(GetP0DataRequestState.Requesting)
            emit(GetP0DataRequestState.Error(throwable))
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

    fun mockBuyerOrderDetailUiModelMapper(result: BuyerOrderDetailUiModel = mockk(relaxed = true)) {
        every {
            buyerOrderDetailUiModelMapper.mapDomainModelToUiModel(any(), any())
        } returns result
        every {
            buyerOrderDetailUiModelMapper.update(any(), any())
        } returns result
    }

    fun runCollectingUiState(block: (List<BuyerOrderDetailUiState>) -> Unit) {
        val uiStates = mutableListOf<BuyerOrderDetailUiState>()
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiStateCollectorJob = scope.launch {
            viewModel.buyerOrderDetailUiState.toList(uiStates)
        }
        block(uiStates)
        uiStateCollectorJob.cancel()
    }
}
