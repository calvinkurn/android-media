package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.*
import com.tokopedia.pdpsimulation.activateCheckout.domain.usecase.PaylaterActivationUseCase
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.ShowToasterException
import com.tokopedia.pdpsimulation.common.domain.model.BaseProductDetailClass
import com.tokopedia.pdpsimulation.common.domain.model.CampaignDetail
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.common.domain.model.Options
import com.tokopedia.pdpsimulation.common.domain.model.Products
import com.tokopedia.pdpsimulation.common.domain.model.Selections
import com.tokopedia.pdpsimulation.common.domain.model.ShopDetail
import com.tokopedia.pdpsimulation.common.domain.model.Variant
import com.tokopedia.pdpsimulation.common.domain.usecase.ProductDetailUseCase
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
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
    fun productDetailSuccessTest() {
        val baseProductDetail = BaseProductDetailClass(GetProductV3("A","a", ShopDetail("ef"),30.0,null,
            Variant(
                listOf(Selections(listOf(Options(value = "a"))),Selections(listOf(Options(value = "a")))),
            listOf(Products("a", listOf(1)))),9, CampaignDetail(40.0,0.0)
        ))


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
    fun productDetailSetVariantSuccessTest() {
        val baseProductDetail = BaseProductDetailClass(GetProductV3("A","a", ShopDetail("ef"),30.0,null,
            Variant(
                listOf(Selections(listOf(Options(value = "a"))),Selections(listOf(Options(value = "a")))),
                listOf(Products("", listOf(0)))),9, CampaignDetail(40.0,0.0)
        ))


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
    fun productDetailSuccessWithCampaignTest() {
        val baseProductDetail = BaseProductDetailClass(GetProductV3("A","a", ShopDetail("ef"),30.0,null,
            Variant(
                listOf(Selections(listOf(Options(value = "a"))),Selections(listOf(Options(value = "a")))),
                listOf(Products("a", listOf(1)))),9, CampaignDetail(40.0,10.0)
        ))


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
    fun productDetailSuccessTestFailedCondition() {
        val baseProductDetail = BaseProductDetailClass(GetProductV3(null,"a",null,30.0,null,
            Variant(
                listOf(Selections(listOf(Options(value = "a"))),Selections(listOf(Options(value = "a")))),
                listOf(Products("a", listOf(1)))),9, CampaignDetail(40.0,0.0)
        ))


        coEvery {
            productDetailUseCase.getProductDetail(any(), any(), "")
        } coAnswers {
            firstArg<(BaseProductDetailClass) -> Unit>().invoke(baseProductDetail)
        }
        viewModel.getProductDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveData.value as Fail).throwable.message,
            "Data invalid"
        )

    }


    @Test
    fun productDetailFail() {
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
    fun successPayLaterActivation() {
        val tenureDetail = TenureDetail(
            false,
            0,
            false,
            "",
            "",
            "",
            "",
            ActivationInstallmentDetails(
                "",
                listOf(DetailContent("", "", 0))
            ),
            ""
        )
        val checkoutData = CheckoutData("1",
            null,null,null,
            null,null,null,false,null,
            null, listOf(tenureDetail),"",null,null,false)
        val basePayLaterOptimizedModel = PaylaterGetOptimizedModel(listOf(checkoutData), "")
        coEvery {
            paylaterActivationUseCase.getPayLaterActivationDetail(
                any(),
                any(),
                0.0,
                "",
                "",
                "",
            )
        } coAnswers {
            firstArg<(PaylaterGetOptimizedModel) -> Unit>().invoke(basePayLaterOptimizedModel)
        }
        viewModel.getOptimizedCheckoutDetail("", 0.0, "", "")
        Assert.assertEquals(
                (viewModel.payLaterActivationDetailLiveData.value as Success).data,
                basePayLaterOptimizedModel
        )
    }


    @Test
    fun successPayLaterActivationConditionFail() {
        val basePayLaterOptimizedModel = PaylaterGetOptimizedModel(emptyList(), "")
        coEvery {
            paylaterActivationUseCase.getPayLaterActivationDetail(
                any(),
                any(),
                0.0,
                "",
                "",
                "",
            )
        } coAnswers {
            firstArg<(PaylaterGetOptimizedModel) -> Unit>().invoke(basePayLaterOptimizedModel)
        }
        viewModel.getOptimizedCheckoutDetail("", 0.0, "", "")
        Assert.assertEquals(
            (viewModel.payLaterActivationDetailLiveData.value as Fail).throwable.message,
          "Empty State"
        )
    }


    @Test
    fun failPayLaterActivation() {
        coEvery {
            paylaterActivationUseCase.getPayLaterActivationDetail(
                any(),
                any(),
                0.0,
                "",
                "",
                "",
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getOptimizedCheckoutDetail("", 0.0, "", "")
        Assert.assertEquals(
                (viewModel.payLaterActivationDetailLiveData.value as Fail).throwable,
                mockThrowable
        )
    }


    @Test
    fun successAddToCart() {
        val addToCartMultiDataModel = mockk<AddToCartOccMultiDataModel>(relaxed = true)

        coEvery {
            addToCartUseCase.execute(captureLambda(), any())
        } coAnswers {
            val onSuccess = lambda<(AddToCartOccMultiDataModel) -> Unit>()
            onSuccess.invoke(addToCartMultiDataModel)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart("", 0)
        Assert.assertEquals(
            (viewModel.addToCartLiveData.value as Success).data,
            addToCartMultiDataModel
        )
    }


    @Test
    fun successAddToCartStatusError() {
        val addToCartMultiDataModel = AddToCartOccMultiDataModel(emptyList(),"ERROR", mockk(relaxed = true))

        coEvery {
            addToCartUseCase.execute(captureLambda(), any())
        } coAnswers {
            val onSuccess = lambda<(AddToCartOccMultiDataModel) -> Unit>()
            onSuccess.invoke(addToCartMultiDataModel)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart("", 0)
        Assert.assertEquals(
            (viewModel.addToCartLiveData.value as Fail).throwable.message,
            ShowToasterException("").message
        )
    }

    @Test
    fun failAddToCart() {
        coEvery {
            addToCartUseCase.execute(any(), captureLambda())
        } coAnswers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(mockThrowable)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart("", 0)
        Assert.assertEquals(
            (viewModel.addToCartLiveData.value as Fail).throwable,
            mockThrowable
        )
    }


}
