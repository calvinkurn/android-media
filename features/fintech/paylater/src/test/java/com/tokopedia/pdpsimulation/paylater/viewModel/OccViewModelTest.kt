package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.BasePayLaterOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.usecase.PaylaterActivationUseCase
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.common.domain.model.BaseProductDetailClass
import com.tokopedia.pdpsimulation.common.domain.usecase.ProductDetailUseCase
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OccViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val productDetailUseCase = mockk<ProductDetailUseCase>(relaxed = true)
    private val paylaterActivationUseCase = mockk<PaylaterActivationUseCase>(relaxed = true)
    private val addToCartUseCase = mockk<AddToCartOccMultiUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: PayLaterActivationViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = PayLaterActivationViewModel(
            paylaterActivationUseCase,
            productDetailUseCase,
            addToCartUseCase,
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
    fun successPayLaterActivation()
    {
        val basePayLaterOptimized = mockk<PaylaterGetOptimizedModel>(relaxed = true)
        coEvery {
            paylaterActivationUseCase.getPayLaterActivationDetail(any(), any(), 0.0,"","")
        } coAnswers {
            firstArg<(PaylaterGetOptimizedModel) -> Unit>().invoke(basePayLaterOptimized)
        }
        viewModel.getOptimizedCheckoutDetail("",0.0,"")
        Assert.assertEquals(
            (viewModel.payLaterActivationDetailLiveData.value as Success).data,
            basePayLaterOptimized
        )
    }



    @Test
    fun failPayLaterActivation()
    {
        coEvery {
            paylaterActivationUseCase.getPayLaterActivationDetail(any(), any(), 0.0,"","")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getOptimizedCheckoutDetail("",0.0,"")
        Assert.assertEquals(
            (viewModel.payLaterActivationDetailLiveData.value as Fail).throwable,
            mockThrowable
        )
    }


    @Test
    fun successAddToCart()
    {
        val addToCartMultiDataModel = mockk<AddToCartOccMultiDataModel>(relaxed = true)

        coEvery {
            addToCartUseCase.execute(captureLambda(), any())
        } coAnswers {
            val onSuccess = lambda<(AddToCartOccMultiDataModel) -> Unit>()
            onSuccess.invoke(addToCartMultiDataModel)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart("",0)
        verify {
            addToCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun failAddToCart(){
        coEvery {
            addToCartUseCase.execute( any(),captureLambda())
        } coAnswers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(mockThrowable)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart("",0)
        verify {
            addToCartUseCase.execute(any(), any())
        }
    }





}