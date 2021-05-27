package com.tokopedia.pms.paymentlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.CancelDetailWrapper
import com.tokopedia.pms.paymentlist.domain.data.Fail
import com.tokopedia.pms.paymentlist.domain.data.PaymentList
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
    private val nullDataErrorMessage = "NULL DATA"
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
    fun `Execute getPaymentList Empty`() {
        val emptyData = "EMPTY"
        val paymentData = mockk<PaymentList>(relaxed = true)
        coEvery {
            paymentListUseCase.getPaymentList(any(), any(), "")
        } coAnswers {
            firstArg<(PaymentList) -> Unit>().invoke(paymentData)
        }
        viewModel.getPaymentListCount()
        assert(viewModel.paymentListResultLiveData.value is Fail)
        coVerify(exactly = 0) { mapper.mapResponseToRenderPaymentList(any(), any(), any()) }
        Assertions.assertThat((viewModel.paymentListResultLiveData.value as Fail).throwable.message)
            .isEqualTo(emptyData)
    }

    @Test
    fun `Execute getPaymentList Success`() {
        val mockGatewayName = "Test Gateway"
        val paymentData = mockk<PaymentList>(relaxed = true) {
            every { lastCursor } returns ""
            every { isHasNextPage } returns true
            every { paymentList } returns arrayListOf(mockk {
                every { gatewayName } returns mockGatewayName
            })
        }
        val baseModel = mockk<BasePaymentModel>(relaxed = true) {
            every { gatewayName } returns mockGatewayName
        }
        coEvery {
            paymentListUseCase.getPaymentList(any(), any(), "")
        } coAnswers {
            firstArg<(PaymentList) -> Unit>().invoke(paymentData)
        }
        coEvery {
            mapper.mapResponseToRenderPaymentList(paymentData.paymentList, any(), any())
        } coAnswers {
            secondArg<(ArrayList<BasePaymentModel>) -> Unit>().invoke(arrayListOf(baseModel))
        }
        viewModel.getPaymentListCount()
        //assert(viewModel.paymentListResultLiveData.value is Success)
        //coVerify(exactly = 1) { mapper.mapResponseToRenderPaymentList(any(), any(), any()) }
        //Assertions.assertThat((viewModel.paymentListResultLiveData.value as Success).data[0].gatewayName)
        //    .isEqualTo(mockGatewayName)
    }

    @Test
    fun `getCancelPaymentDetail invoke Failed`() {
        coEvery {
            cancelPaymentDetailUseCase.gerCancelDetail(any(), any(), "", "", "")
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
        val mockData = mockk<CancelDetailWrapper>()
        coEvery {
            cancelPaymentDetailUseCase.gerCancelDetail(any(), any(), "", "", "")
        } coAnswers {
            firstArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPaymentListCount()
        assert(viewModel.cancelPaymentDetailLiveData.value is Fail)
        Assertions.assertThat((viewModel.cancelPaymentDetailLiveData.value as Fail).throwable.message)
            .isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun cancelPayment() {
    }
}