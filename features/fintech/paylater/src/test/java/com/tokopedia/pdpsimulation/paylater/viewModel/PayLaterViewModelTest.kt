package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.ShowToasterException
import com.tokopedia.pdpsimulation.common.domain.model.BaseProductDetailClass
import com.tokopedia.pdpsimulation.common.domain.model.CampaignDetail
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.common.domain.model.Pictures
import com.tokopedia.pdpsimulation.common.domain.model.ShopDetail
import com.tokopedia.pdpsimulation.common.domain.usecase.ProductDetailUseCase
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.Label
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterAllData
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulation
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SupervisorUiModel
import com.tokopedia.pdpsimulation.paylater.domain.usecase.PayLaterSimulationV3UseCase
import com.tokopedia.pdpsimulation.paylater.domain.usecase.PayLaterUiMapperUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.invoke
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
    private val addToCartUseCase = mockk<AddToCartOccMultiUseCase>(relaxed = true)
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
            addToCartUseCase,
                dispatcher
        )
    }

    @Test
    fun productDetailSuccessTest()
    {
        val shopDetail = mockk<ShopDetail>(relaxed = true)
        val campaignDetail = mockk<CampaignDetail>(relaxed = true)
        val baseProductDetail = BaseProductDetailClass(
            GetProductV3(
                "name", "url", shopDetail,
                1000.0, arrayListOf(
                    Pictures("url")
                ), null, 0, campaignDetail
            )
        )
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
    fun `productDetail fetch success but condition fail`()
    {
        val shopDetail = mockk<ShopDetail>(relaxed = true)
        val campaignDetail = mockk<CampaignDetail>(relaxed = true)
        val baseProductDetail = BaseProductDetailClass(
            GetProductV3(
                "", "url", shopDetail,
                1000.0, arrayListOf(
                    Pictures("url")
                ), null, 0, campaignDetail
            )
        )
        coEvery {
            productDetailUseCase.getProductDetail(any(), any(), "")
        } coAnswers {
            firstArg<(BaseProductDetailClass) -> Unit>().invoke(baseProductDetail)
        }
        viewModel.getProductDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveData.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun `productDetail fetch failed throws exception`()
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
    fun `productDetail data invalid `()
    {
        val baseProductDetail = mockk<BaseProductDetailClass>(relaxed = true)
        coEvery {
            productDetailUseCase.getProductDetail(any(), any(), "")
        } coAnswers {
            firstArg<(BaseProductDetailClass) -> Unit>().invoke(baseProductDetail)
        }
        viewModel.getProductDetail("")
        assert(viewModel.productDetailLiveData.value is Fail)
    }


    @Test
    fun successPayLaterOptions()
    {
        val finalPrice = 10.0
        val labelText = "PROMO"
        val labelTextColor = "white"
        val labelBgColor = "red"
        val label = Label(
            labelText,
            labelTextColor,
            labelBgColor,
        )
        val payLaterGetSimulation = PayLaterGetSimulation(
            listOf(
                PayLaterAllData(
                    1,
                    "",
                    "",
                    listOf(),
                    label,
                    "",
                )
            )
        )
        val list = arrayListOf(SimulationUiModel(
            1,
            "",
            "",
            false,
            arrayListOf(SupervisorUiModel),
            SimulationUiModel.LabelUiModel.create(label),
            "",
        ))
        mockMapperResponse(list)
        coVerify(exactly = 0) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
        coEvery {
            payLaterSimulationData.getPayLaterSimulationDetails(
                any(),
                any(),
                finalPrice,
                "0",
                "",
                "",
                ""
            )
        } coAnswers {
            firstArg<(PayLaterGetSimulation) -> Unit>().invoke(payLaterGetSimulation)
        }

        viewModel.getPayLaterAvailableDetail(finalPrice, "0", "", "", "")
        coVerify(exactly = 1) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Success).data, list)
        assertSimulationData(payLaterGetSimulation, list)
        assert(viewModel.finalProductPrice == finalPrice)
    }

    private fun assertSimulationData(
        simulationData: PayLaterGetSimulation,
        simulationUiModel: List<SimulationUiModel>,
    ) {
        simulationData.productList?.forEachIndexed { index, data ->
            assert(data.tenure?.equals(simulationUiModel[index].tenure) ?: false)
            assert(data.text?.equals(simulationUiModel[index].text) ?: false)
            assert(data.smallText?.equals(simulationUiModel[index].smallText) ?: false)
            assert(data.promoName?.equals(simulationUiModel[index].promoName) ?: false)
            assertTenureLabel(data.label, simulationUiModel[index].label)
        }
    }

    private fun assertTenureLabel(labelData: Label?, labelUiModel: SimulationUiModel.LabelUiModel?) {
        assert(labelData?.text?.equals(labelUiModel?.text) ?: false)
        assert(labelData?.textColor?.equals(labelUiModel?.textColor) ?: false)
        assert(labelData?.bgColor?.equals(labelUiModel?.bgColor) ?: false)
    }


    @Test
    fun successPayLaterOptionsDataEmpty()
    {
        val payLaterGetSimulation = PayLaterGetSimulation(
            listOf(
                PayLaterAllData(1,"", "", listOf(), Label(), "")
            )
        )
        val list = arrayListOf(SimulationUiModel(
            1,
            "",
            "",
            true,
            arrayListOf(SupervisorUiModel)))
        mockMapperResponse(list)
        coVerify(exactly = 0) {
            payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
        coEvery {
            payLaterSimulationData.getPayLaterSimulationDetails(
                any(),
                any(),
                10.0,
                "0",
                "",
                "",
                ""
            )
        } coAnswers {
            firstArg<(PayLaterGetSimulation) -> Unit>().invoke(payLaterGetSimulation)
        }

        viewModel.getPayLaterAvailableDetail(10.0, "0", "", "", "")
        coVerify(exactly = 1) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Success).data, list)
    }


    @Test
    fun `successPayLaterOptions With Default Selected`()
    {
        val payLaterGetSimulation = PayLaterGetSimulation(
            listOf(
                PayLaterAllData(1,"", "", listOf(), Label(), "")
            )
        )
        val list = arrayListOf(SimulationUiModel(
            1,
            "",
            "",
            true,
            arrayListOf(SupervisorUiModel)))
        mockMapperResponse(list)

        coEvery {
            payLaterSimulationData.getPayLaterSimulationDetails(
                any(),
                any(),
                10.0,
                "0",
                "",
                "",
                ""
            )
        } coAnswers {
            firstArg<(PayLaterGetSimulation) -> Unit>().invoke(payLaterGetSimulation)
        }

        viewModel.getPayLaterAvailableDetail(10.0, "0", "", "", "")
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
            payLaterSimulationData.getPayLaterSimulationDetails(
                any(),
                any(),
                0.0,
                "0",
                "",
                "",
                ""
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPayLaterAvailableDetail(0.0, "0", "", "", "")
        coVerify(exactly = 0) { payLaterUiMapperUseCase.mapResponseToUi(any(), any(), any()) }
        Assert.assertEquals((viewModel.payLaterOptionsDetailLiveData.value as Fail).throwable,mockThrowable)
    }


    @Test
    fun successAddToCart() {
        val addToCartMultiDataModel = mockk<AddToCartOccMultiDataModel>(relaxed = true)
        val detail = mockk<Detail>(relaxed = true)
        coEvery {
            addToCartUseCase.execute(captureLambda(), any())
        } coAnswers {
            val onSuccess = lambda<(AddToCartOccMultiDataModel) -> Unit>()
            onSuccess.invoke(addToCartMultiDataModel)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart(detail,"")
        Assert.assertEquals(
            (viewModel.addToCartLiveData.value as Success).data,
            addToCartMultiDataModel
        )
        assert(viewModel.cardDetailSelected == detail)
    }

    @Test
    fun successAddToCartWithErrorTrue() {
        val addToCartMultiDataModel = AddToCartOccMultiDataModel(emptyList(),"ERROR", mockk(relaxed = true))

        val detail = mockk<Detail>(relaxed = true)
        coEvery {
            addToCartUseCase.execute(captureLambda(), any())
        } coAnswers {
            val onSuccess = lambda<(AddToCartOccMultiDataModel) -> Unit>()
            onSuccess.invoke(addToCartMultiDataModel)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart(detail,"")
        Assert.assertEquals(
            (viewModel.addToCartLiveData.value as Fail).throwable.message,
           ShowToasterException("").message
        )
    }

    @Test
    fun failAddToCart() {
        val detail = mockk<Detail>(relaxed = true)
        coEvery {
            addToCartUseCase.execute(any(), captureLambda())
        } coAnswers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(mockThrowable)
        }
        viewModel.shopId = ""
        viewModel.addProductToCart(detail,"")
        Assert.assertEquals(
            (viewModel.addToCartLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

}
