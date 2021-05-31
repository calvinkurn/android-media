package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.FinishOrderUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetBuyerOrderDetailUseCase
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class BuyerOrderDetailViewModelTestFixture {

    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase

    @RelaxedMockK
    lateinit var finishOrderUseCase: FinishOrderUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartMultiUseCase

    lateinit var viewModel: BuyerOrderDetailViewModel

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val orderId = "166835036"
    val paymentId = "1234567890"
    val cart = "1234567890"
    val userId = "10001"
    val shopId = "10002"

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
            orderDetailId = "20531238",
            orderId = "166835036",
            price = "500000",
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

    val atcExpectedParams = arrayListOf(AddToCartMultiParam(
            productId = product.productId.toLong(),
            productName = product.productName,
            productPrice = product.price.toLong(),
            qty = product.quantity,
            notes = product.productNote,
            shopId = shopId.toInt(),
            custId = userId.toInt()
    ))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BuyerOrderDetailViewModel(
                coroutineDispatchers = coroutineDispatchers,
                atcMultiQuery = Lazy { "" },
                userSession = Lazy { userSession },
                getBuyerOrderDetailUseCase = Lazy { getBuyerOrderDetailUseCase },
                finishOrderUseCase = Lazy { finishOrderUseCase },
                atcUseCase = Lazy { atcUseCase }
        )

        every { userSession.userId } returns userId
    }

    fun createSuccessBuyerOrderDetailResult(result: BuyerOrderDetailUiModel) {
        coEvery {
            getBuyerOrderDetailUseCase.execute(any())
        } returns result

        viewModel.getBuyerOrderDetail(orderId, paymentId, cart)
    }

    fun createFailedBuyerOrderDetailResult(throwable: Throwable = Throwable()) {
        coEvery {
            getBuyerOrderDetailUseCase.execute(any())
        } throws throwable

        viewModel.getBuyerOrderDetail(orderId, paymentId, cart)
    }
}