package com.tokopedia.pms.paymentlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.pms.paymentlist.domain.data.*
import com.tokopedia.pms.paymentlist.domain.usecase.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PaymentListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getPaymentListCountUseCase = mockk<GetPaymentListCountUseCase>(relaxed = true)
    private val paymentListUseCase = mockk<PaymentListUseCase>(relaxed = true)
    private val cancelPaymentDetailUseCase = mockk<PaymentCancelDetailUseCase>(relaxed = true)
    private val cancelPaymentUseCase = mockk<CancelPaymentUseCase>(relaxed = true)
    private val mapper = mockk<PaymentListMapperUseCase>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: PaymentListViewModel


    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = PaymentListViewModel(
            getPaymentListCountUseCase,
            paymentListUseCase,
            cancelPaymentDetailUseCase,
            cancelPaymentUseCase,
            mapper,
            dispatcher
        )
    }

    @Test
    fun `Execute getPaymentList Fail(Invoke Failed)`() {
        coEvery { getPaymentListCountUseCase.getPaymentCount(any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        coEvery {
            paymentListUseCase.getPaymentList(any(), any(), "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPaymentListCount()
        assert(viewModel.paymentListResultLiveData.value is Fail)
        coVerify(exactly = 0) { mapper.mapResponseToRenderPaymentList(any(), any(), any()) }
        Assertions.assertThat((viewModel.paymentListResultLiveData.value as Fail).throwable.message)
            .isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getPaymentList Null Response`() {
        val mockGatewayName = "Test Gateway"

        val baseModel = mockk<BasePaymentModel>(relaxed = true) {
            every { gatewayName } returns mockGatewayName
        }
        coEvery { getPaymentListCountUseCase.getPaymentCount(any(), any()) } coAnswers {
            firstArg<(Int) -> Unit>().invoke(0)
        }
        coEvery {
            paymentListUseCase.getPaymentList(any(), any(), "")
        } coAnswers {
            firstArg<(PaymentList?) -> Unit>().invoke(null)
        }
        viewModel.getPaymentListCount()
        assert(viewModel.paymentListResultLiveData.value is EmptyState)
        coVerify(exactly = 0) { mapper.mapResponseToRenderPaymentList(any(), any(), any()) }
    }


    @Test
    fun `Execute getPaymentList Empty`() {
        val mockGatewayName = "Test Gateway"
        val responseData = PaymentListInside().also { it.gatewayName = mockGatewayName }
        val paymentData = PaymentList().also {
            it.lastCursor = ""
            it.isHasNextPage = false
            it.paymentList = arrayListOf()
        }
        val baseModel = mockk<BasePaymentModel>(relaxed = true) {
            every { gatewayName } returns mockGatewayName
        }
        coEvery { getPaymentListCountUseCase.getPaymentCount(any(), any()) } coAnswers {
            firstArg<(Int) -> Unit>().invoke(0)
        }
        coEvery {
            paymentListUseCase.getPaymentList(any(), any(), "")
        } coAnswers {
            firstArg<(PaymentList?) -> Unit>().invoke(paymentData)
        }
        viewModel.getPaymentListCount()
        assert(viewModel.paymentListResultLiveData.value is EmptyState)
        coVerify(exactly = 0) { mapper.mapResponseToRenderPaymentList(any(), any(), any()) }
    }

    @Test
    fun `Execute getPaymentList Success`() {
        val mockGatewayName = "Test Gateway"
        val responseData = PaymentListInside().also { it.gatewayName = mockGatewayName }
        val paymentData = PaymentList().also {
            it.lastCursor = ""
            it.isHasNextPage = false
            it.paymentList = arrayListOf(responseData)
        }
        val baseModel = mockk<BasePaymentModel>(relaxed = true) {
            every { gatewayName } returns mockGatewayName
        }
        coEvery { getPaymentListCountUseCase.getPaymentCount(any(), any()) } coAnswers {
            firstArg<(Int) -> Unit>().invoke(1)
        }
        coEvery {
            paymentListUseCase.getPaymentList(any(), any(), "")
        } coAnswers {
            firstArg<(PaymentList?) -> Unit>().invoke(paymentData)
        }
        coEvery {
            mapper.mapResponseToRenderPaymentList(paymentData.paymentList, any(), any())
        } coAnswers {
            secondArg<(ArrayList<BasePaymentModel>) -> Unit>().invoke(arrayListOf(baseModel))
        }
        viewModel.getPaymentListCount()
        assert(viewModel.paymentListResultLiveData.value is Success)
        coVerify(exactly = 1) { mapper.mapResponseToRenderPaymentList(any(), any(), any()) }
        Assertions.assertThat((viewModel.paymentListResultLiveData.value as Success).data[0].gatewayName)
            .isEqualTo(mockGatewayName)
    }

    @Test
    fun `getCancelPaymentDetail invoke Failed`() {
        coEvery {
            cancelPaymentDetailUseCase.getCancelDetail(any(), any(), "", "", "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getCancelPaymentDetail("", "", "")
        assert(viewModel.cancelPaymentDetailLiveData.value is Fail)
        Assertions.assertThat((viewModel.cancelPaymentDetailLiveData.value as Fail).throwable.message)
            .isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `getCancelPaymentDetail Success`() {
        val mockData = CancelDetailWrapper("", "", "",
            CancelDetail().also
            { it.isSuccess = true })

        coEvery {
            cancelPaymentDetailUseCase.getCancelDetail(any(), any(), "", "", "")
        } coAnswers {
            firstArg<(CancelDetailWrapper) -> Unit>().invoke(mockData)
        }
        viewModel.getCancelPaymentDetail("", "", "")
        assert(viewModel.cancelPaymentDetailLiveData.value is Success)
        Assertions.assertThat((viewModel.cancelPaymentDetailLiveData.value as Success).data.cancelDetailData.isSuccess)
            .isEqualTo(true)
    }

    @Test
    fun `cancelPayment invoke Failed`() {
        coEvery {
            cancelPaymentUseCase.invokePaymentCancel(any(), any(), "t1234", "m1234")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.cancelPayment("t1234", "m1234")
        assert(viewModel.cancelPaymentLiveData.value is Fail)
        Assertions.assertThat((viewModel.cancelPaymentLiveData.value as Fail).throwable.message)
            .isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `cancelPayment successful`() {
        val data = CancelPayment()
        data.isSuccess = true
        data.message = "Cancel Success"
        coEvery {
            cancelPaymentUseCase.invokePaymentCancel(any(), any(), "t1234", "m1234")
        } coAnswers {
            firstArg<(CancelPayment) -> Unit>().invoke(data)
        }
        viewModel.cancelPayment("t1234", "m1234")
        assert(viewModel.cancelPaymentLiveData.value is Success)
        Assertions.assertThat((viewModel.cancelPaymentLiveData.value as Success).data.isSuccess)
            .isEqualTo(true)
    }
}