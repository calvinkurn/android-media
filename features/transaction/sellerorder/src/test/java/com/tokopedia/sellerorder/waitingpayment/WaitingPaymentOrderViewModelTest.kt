package com.tokopedia.sellerorder.waitingpayment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.util.TestHelper
import com.tokopedia.sellerorder.waitingpaymentorder.domain.GetWaitingPaymentOrderUseCase
import com.tokopedia.sellerorder.waitingpaymentorder.domain.mapper.WaitingPaymentOrderResultMapper
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderResponse
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.Paging
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.viewmodel.WaitingPaymentOrderViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WaitingPaymentOrderViewModelTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/som_get_waiting_payment_order_success_response.json"

        private val SUCCESS_PAGING_RESULT = Paging(nextPaymentDeadline = 123456)

        private val SUCCESS_WAITING_PAYMENT_ORDER_LIST_RESULT = listOf(
                WaitingPaymentOrderUiModel(
                        orderId = "166427360",
                        paymentDeadline = "11 Sep, 17:44",
                        buyerNameAndPlace = "N***********g (Kota Administrasi Jakarta Selatan)",
                        productUiModels = listOf(
                                WaitingPaymentOrderUiModel.ProductUiModel(
                                        id = "15293732",
                                        name = "Kaos Polos Aja",
                                        picture = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2019/10/29/5479999/5479999_fd571b4d-6f16-4805-84a0-3d02375f4f22_800_800",
                                        quantity = 1,
                                        price = "Rp 100"
                                ),
                                WaitingPaymentOrderUiModel.ProductUiModel(
                                        id = "15332067",
                                        name = "Kaos Polos Aja gimana coy",
                                        picture = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2019/10/29/5479999/5479999_fd571b4d-6f16-4805-84a0-3d02375f4f22_800_800",
                                        quantity = 1,
                                        price = "Rp 9.000"
                                ),
                                WaitingPaymentOrderUiModel.ProductUiModel(
                                        id = "15359272",
                                        name = "product 1",
                                        picture = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/4/8/5479999/5479999_0d445339-9e18-4994-9744-2a18deffd0f6_315_315",
                                        quantity = 1,
                                        price = "Rp 1.500"
                                ),
                                WaitingPaymentOrderUiModel.ProductUiModel(
                                        id = "15379993",
                                        name = "product 10",
                                        picture = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/6/8/5479999/5479999_3c9286e7-e942-4be6-bd23-c34999ae350b_315_315",
                                        quantity = 1,
                                        price = "Rp 10.000"
                                ),
                                WaitingPaymentOrderUiModel.ProductUiModel(
                                        id = "15405840",
                                        name = "abc",
                                        picture = "https://ecs7.tokopedia.net/img/cache/100-square/hDjmkQ/2020/7/7/5b8cdb76-baaa-4541-bfa2-80e89fd31999.jpg",
                                        quantity = 1,
                                        price = "Rp 100"
                                )
                        ),
                        isExpanded = false
                ),
                WaitingPaymentOrderUiModel(
                        orderId = "166426695",
                        paymentDeadline = "11 Sep, 11:25",
                        buyerNameAndPlace = "N***********g (Kota Administrasi Jakarta Selatan)",
                        productUiModels = listOf(
                                WaitingPaymentOrderUiModel.ProductUiModel(
                                        id = "15359272",
                                        name = "product 1",
                                        picture = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/4/8/5479999/5479999_0d445339-9e18-4994-9744-2a18deffd0f6_315_315",
                                        quantity = 1,
                                        price = "Rp 1.500"
                                )
                        ),
                        isExpanded = false
                )
        )
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider

    @RelaxedMockK
    lateinit var getWaitingPaymentOrderGqlUseCase: GraphqlUseCase<WaitingPaymentOrderResponse.Data>

    private val getWaitingPaymentOrderMapper = WaitingPaymentOrderResultMapper()

    private val getWaitingPaymentOrderUseCase: GetWaitingPaymentOrderUseCase by lazy {
        GetWaitingPaymentOrderUseCase(getWaitingPaymentOrderGqlUseCase, getWaitingPaymentOrderMapper)
    }

    private val viewModel by lazy {
        WaitingPaymentOrderViewModel(dispatcher, getWaitingPaymentOrderUseCase)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun loadWaitingPaymentOrder_shouldReturnSuccess() {
        coEvery {
            getWaitingPaymentOrderGqlUseCase.executeOnBackground()
        } returns TestHelper.createSuccessResponse(SUCCESS_RESPONSE)

        viewModel.loadWaitingPaymentOrder(WaitingPaymentOrderRequestParam())

        coVerify {
            getWaitingPaymentOrderGqlUseCase.executeOnBackground()
        }

        assert(viewModel.waitingPaymentOrderUiModelResult.value is Success)
        assert((viewModel.waitingPaymentOrderUiModelResult.value as Success).data == SUCCESS_WAITING_PAYMENT_ORDER_LIST_RESULT)
        assert(viewModel.paging == SUCCESS_PAGING_RESULT)
    }

    @Test
    fun loadWaitingPaymentOrder_shouldReturnFail() {
        coEvery {
            getWaitingPaymentOrderGqlUseCase.executeOnBackground()
        } throws Exception()

        viewModel.loadWaitingPaymentOrder(WaitingPaymentOrderRequestParam())

        coVerify {
            getWaitingPaymentOrderGqlUseCase.executeOnBackground()
        }

        assert(viewModel.waitingPaymentOrderUiModelResult.value is Fail)
        assert(viewModel.paging == Paging())
    }

    @Test
    fun resetPagingTest() {
        viewModel.resetPaging()

        assert(viewModel.paging == Paging())
    }
}