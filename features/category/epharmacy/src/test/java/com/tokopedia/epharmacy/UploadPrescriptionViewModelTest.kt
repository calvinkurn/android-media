package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.network.response.EPharmacyProduct
import com.tokopedia.epharmacy.usecase.GetEPharmacyCheckoutDetailUseCase
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.usecase.PostPrescriptionIdUseCase
import com.tokopedia.epharmacy.usecase.UploadPrescriptionUseCase
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UploadPrescriptionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getEPharmacyOrderDetailUseCase = mockk<GetEPharmacyOrderDetailUseCase>(relaxed = true)
    private val getEPharmacyCheckoutDetailUseCase = mockk<GetEPharmacyCheckoutDetailUseCase>(relaxed = true)
    private val uploadPrescriptionUseCase = mockk<UploadPrescriptionUseCase>(relaxed = true)
    private val postPrescriptionIdUseCase = mockk<PostPrescriptionIdUseCase>(relaxed = true)

    private val dispatcherMain = TestCoroutineDispatcher()
    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: UploadPrescriptionViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = UploadPrescriptionViewModel(
            getEPharmacyOrderDetailUseCase,
            getEPharmacyCheckoutDetailUseCase,
            uploadPrescriptionUseCase,
            postPrescriptionIdUseCase,
            dispatcherBackground,
            dispatcherMain
        )
    }

    @Test
    fun orderDetailSuccessTest()
    {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(product),0L,"","","",
            false,"",""
        )

        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyOrderDetail("")
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun `orderDetail fetch success but condition fail`()
    {
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(),0L,"","","",
            false,"",""
        )
        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyOrderDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun checkoutDetailSuccessTest()
    {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(product),0L,"","","",
            false,"",""
        )

        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("")
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun `checkout fetch success but condition fail`()
    {
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(),0L,"","","",
            false,"",""
        )
        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

//    @Test
//    fun `productDetail fetch failed throws exception`()
//    {
//        coEvery {
//            productDetailUseCase.getProductDetail(any(), any(), "")
//        } coAnswers {
//            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
//        }
//        viewModel.getProductDetail("")
//        Assert.assertEquals(
//                (viewModel.productDetailLiveData.value as Fail).throwable,
//                mockThrowable
//        )
//    }
//
//    @Test
//    fun `productDetail data invalid `()
//    {
//        val baseProductDetail = mockk<BaseProductDetailClass>(relaxed = true)
//        coEvery {
//            productDetailUseCase.getProductDetail(any(), any(), "")
//        } coAnswers {
//            firstArg<(BaseProductDetailClass) -> Unit>().invoke(baseProductDetail)
//        }
//        viewModel.getProductDetail("")
//        assert(viewModel.productDetailLiveData.value is Fail)
//    }
//
//
//    @Test
//    fun successPayLaterOptions()
//    {
//        val payLaterGetSimulation = PayLaterGetSimulation(listOf(PayLaterAllData(1,"", "", listOf())))
//        val list = arrayListOf(SimulationUiModel(
//                1,
//                "",
//                "",
//                false,
//                arrayListOf(SupervisorUiModel)))
//        mockMapperResponse(list)
//        coVerify(exactly = 0) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
//        coEvery {
//            payLaterSimulationData.getPayLaterSimulationDetails(any(), any(), 10.0, "0")
//        } coAnswers {
//            firstArg<(PayLaterGetSimulation) -> Unit>().invoke(payLaterGetSimulation)
//        }
//
//        viewModel.getPayLaterAvailableDetail(10.0, "0")
//        coVerify(exactly = 1) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
//        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Success).data, list)
//    }
//
//    @Test
//    fun `successPayLaterOptions With Default Selected`()
//    {
//        val payLaterGetSimulation = PayLaterGetSimulation(listOf(PayLaterAllData(1,"", "", listOf())))
//        val list = arrayListOf(SimulationUiModel(
//            1,
//            "",
//            "",
//            true,
//            arrayListOf(SupervisorUiModel)))
//        mockMapperResponse(list)
//
//        coEvery {
//            payLaterSimulationData.getPayLaterSimulationDetails(any(), any(), 10.0, "0")
//        } coAnswers {
//            firstArg<(PayLaterGetSimulation) -> Unit>().invoke(payLaterGetSimulation)
//        }
//
//        viewModel.getPayLaterAvailableDetail(10.0, "0")
//        coVerify(exactly = 1) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
//        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Success).data, list)
//    }
//
//
//    private fun mockMapperResponse(list: ArrayList<SimulationUiModel>) {
//        coEvery {
//            payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any())
//        } coAnswers {
//            firstArg<(ArrayList<SimulationUiModel>) -> Unit>().invoke(list)
//        }
//    }
//
//    @Test
//    fun failPayLaterOptions()
//    {
//        coEvery {
//            payLaterSimulationData.getPayLaterSimulationDetails(any(), any(), 0.0, "0")
//        } coAnswers {
//            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
//        }
//        viewModel.getPayLaterAvailableDetail(0.0, "0")
//        coVerify(exactly = 0) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
//        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Fail).throwable,mockThrowable)
//    }
//
//
//    @Test
//    fun successAddToCart() {
//        val addToCartMultiDataModel = mockk<AddToCartOccMultiDataModel>(relaxed = true)
//        val detail = mockk<Detail>(relaxed = true)
//        coEvery {
//            addToCartUseCase.execute(captureLambda(), any())
//        } coAnswers {
//            val onSuccess = lambda<(AddToCartOccMultiDataModel) -> Unit>()
//            onSuccess.invoke(addToCartMultiDataModel)
//        }
//        viewModel.shopId = ""
//        viewModel.addProductToCart(detail,"")
//        verify {
//            addToCartUseCase.execute(any(), any())
//        }
//    }
//
//    @Test
//    fun failAddToCart() {
//        val detail = mockk<Detail>(relaxed = true)
//        coEvery {
//            addToCartUseCase.execute(any(), captureLambda())
//        } coAnswers {
//            val onError = lambda<(Throwable) -> Unit>()
//            onError.invoke(mockThrowable)
//        }
//        viewModel.shopId = ""
//        viewModel.addProductToCart(detail,"")
//        verify {
//            addToCartUseCase.execute(any(), any())
//        }
//    }

}