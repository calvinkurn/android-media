package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.pdpsimulation.common.domain.model.BaseProductDetailClass
import com.tokopedia.pdpsimulation.common.domain.usecase.ProductDetailUseCase
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PayLaterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val productDetailUseCase = mockk<ProductDetailUseCase>(relaxed = true)
    private val payLaterSimulationData = mockk<PayLaterSimulationV3UseCase>(relaxed = true)
    private val payLaterUiMapperUseCase = mockk<PayLaterUiMapperUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: PayLaterViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = PayLaterViewModel(
            payLaterSimulationData,
            productDetailUseCase,
            payLaterUiMapperUseCase,
            dispatcher
        )
    }

    @Test
    fun productDetailSuccessTest()
    {
        val baseProductDetail = mockk<BaseProductDetailClass>(relaxed = true)
        coEvery {
            productDetailUseCase.getProductDetail(any(), any(), "")
        } coAnswers {
            firstArg<(BaseProductDetailClass) -> Unit>().invoke(baseProductDetail)
        }
        viewModel.getProductDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveData.value as Success).data,
            baseProductDetail.getProductV3
        )

    }

    @Test
    fun productDetailFail()
    {
        coEvery {
            productDetailUseCase.getProductDetail(any(), any(), "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getProductDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveData.value as Fail).throwable,
            mockThrowable
        )
    }


    @Test
    fun successPayLaterOptions()
    {
        val payLaterGetSimulation = PayLaterGetSimulation(listOf(PayLaterAllData(1,"", "", listOf())))
        val list = arrayListOf(SimulationUiModel(
            1,
            "",
            "",
            false,
            arrayListOf(SupervisorUiModel)))
        mockMapperResponse(list)

        coEvery {
            payLaterSimulationData.getPayLaterSimulationDetails(any(), any(), 10.0, "0")
        } coAnswers {
            firstArg<(PayLaterGetSimulation) -> Unit>().invoke(payLaterGetSimulation)
        }

        viewModel.getPayLaterAvailableDetail(10.0, "0")
        coVerify(exactly = 1) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Success).data, list)
    }

    private fun mockMapperResponse(list: ArrayList<SimulationUiModel>) {
        coEvery {
            payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any())
        } coAnswers {
            firstArg<(ArrayList<SimulationUiModel>) -> Unit>().invoke(list)
        }
    }

    @Test
    fun failPayLaterOptions()
    {
        coEvery {
            payLaterSimulationData.getPayLaterSimulationDetails(any(), any(), 0.0, "0")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPayLaterAvailableDetail(0.0, "0")
        coVerify(exactly = 0) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }

        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Fail).throwable,mockThrowable)
    }



}