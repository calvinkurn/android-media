package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.pdpsimulation.PayLaterHelper
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PayLaterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val productDetailUseCase = mockk<ProductDetailUseCase>(relaxed = true)
    private val payLaterSimulationData = mockk<PayLaterSimulationV2UseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: PayLaterViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val nullDataErrorMessage = "NULL DATA"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = PayLaterViewModel(
            payLaterSimulationData,
            productDetailUseCase,
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
        val payLaterGetSimulation = mockk<PayLaterGetSimulation>(relaxed = true)
        coEvery {
            payLaterSimulationData.getPayLaterProductDetails(any(), any(), 0)
        } coAnswers {
            firstArg<(PayLaterGetSimulation) -> Unit>().invoke(payLaterGetSimulation)
        }
        viewModel.getPayLaterAvailableDetail(0)
        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Success).data,payLaterGetSimulation)
    }

    @Test
    fun failPayLaterOptions()
    {

        coEvery {
            payLaterSimulationData.getPayLaterProductDetails(any(), any(), 0)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPayLaterAvailableDetail(0)
        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Fail).throwable,mockThrowable)
    }



}