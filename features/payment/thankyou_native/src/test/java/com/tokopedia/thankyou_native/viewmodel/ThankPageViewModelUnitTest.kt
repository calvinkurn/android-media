package com.tokopedia.thankyou_native.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.TopAdsUIModel
import com.tokopedia.thankyou_native.domain.model.ValidateEngineResponse
import com.tokopedia.thankyou_native.domain.usecase.*
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.unifycomponents.ticker.TickerData
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
class ThankPageViewModelUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val fetchFailedErrorMessage = "Fetch Failed"
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: ThanksPageDataViewModel
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    private val thankPageUseCase = mockk<ThanksPageDataUseCase>(relaxed = true)
    private val thanksPageMapperUseCase = mockk<ThanksPageMapperUseCase>(relaxed = true)
    private val gyroEngineRequestUseCase = mockk<GyroEngineRequestUseCase>(relaxed = true)
    private val gyroEngineMapperUseCase = mockk<GyroEngineMapperUseCase>(relaxed = true)
    private val topTickerUseCase = mockk<TopTickerUseCase>(relaxed = true)
    private val defaultAddressUseCase = mockk<GetDefaultAddressUseCase>(relaxed = true)
    private val thankYouTopAdsViewModelUseCase =
        mockk<ThankYouTopAdsViewModelUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ThanksPageDataViewModel(
            thankPageUseCase,
            thanksPageMapperUseCase,
            gyroEngineRequestUseCase,
            gyroEngineMapperUseCase,
            topTickerUseCase,
            defaultAddressUseCase,
            thankYouTopAdsViewModelUseCase,
            dispatcher
        )
    }


    @Test
    fun successThanksPageResult() {

        val thankPageData = mockk<ThanksPageData>(relaxed = true)
        coEvery {
            thankPageUseCase.getThankPageData(any(), any(), "", "")
        } coAnswers {
            firstArg<(ThanksPageData) -> Unit>().invoke(thankPageData)
        }


        coEvery {
            thanksPageMapperUseCase.populateThanksPageDataFields(
                thankPageData,
                any(),
                any()
            )
        } coAnswers {
            secondArg<(ThanksPageData) -> Unit>().invoke(thankPageData)
        }

        viewModel.getThanksPageData("", "")
        Assert.assertEquals(
            (viewModel.thanksPageDataResultLiveData.value as Success).data,
            thankPageData
        )

    }

    @Test
    fun failThanksPageResult() {

        coEvery {
            thankPageUseCase.getThankPageData(any(), any(), "", "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getThanksPageData("", "")
        Assert.assertEquals(
            (viewModel.thanksPageDataResultLiveData.value as Fail).throwable,
            mockThrowable
        )

    }


    @Test
    fun successGyroRecommendationLiveData()
    {
        val gyroRecommendation = mockk<GyroRecommendation>(relaxed = true)
        val featureEngineData = mockk<FeatureEngineData>(relaxed = true)
        coEvery {
            gyroEngineMapperUseCase.getFeatureListData(featureEngineData,any(),any())
        }coAnswers {
            secondArg<(GyroRecommendation) -> Unit>().invoke(gyroRecommendation)
        }

        viewModel.postGyroRecommendation(featureEngineData)
        Assert.assertEquals(viewModel.gyroRecommendationLiveData.value,gyroRecommendation)
    }


    @Test
    fun successTopTickerLiveData()
    {
        val tickerData = mockk<List<TickerData>>(relaxed = true)
        coEvery {
            topTickerUseCase.getTopTickerData("",any(),any())
        }coAnswers {
            secondArg<(List<TickerData>) -> Unit>().invoke(tickerData)
        }
        viewModel.getThanksPageTicker("")
        Assert.assertEquals((viewModel.topTickerLiveData.value as Success).data,tickerData)
    }

    @Test
    fun failTopTickerLiveData()
    {
        coEvery {
            topTickerUseCase.getTopTickerData("",any(),any())
        }coAnswers {
           thirdArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getThanksPageTicker("")
        Assert.assertEquals((viewModel.topTickerLiveData.value as Fail).throwable,mockThrowable)
    }

    @Test
    fun successDefaultAddressLiveData()
    {
        val getDefaultChosenAddressResponse = mockk<GetDefaultChosenAddressResponse>(relaxed = true)
        coEvery {
            defaultAddressUseCase.getDefaultChosenAddress(any(),any())
        }coAnswers {
            firstArg<(GetDefaultChosenAddressResponse) -> Unit>().invoke(getDefaultChosenAddressResponse)
        }
        viewModel.resetAddressToDefault()
        Assert.assertEquals((viewModel.defaultAddressLiveData.value as Success).data,getDefaultChosenAddressResponse)
    }


    @Test
    fun failDefaultAddressLiveData()
    {
        coEvery {
        defaultAddressUseCase.getDefaultChosenAddress(any(),any())
    }coAnswers {
        secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
    }
        viewModel.resetAddressToDefault()
        Assert.assertEquals((viewModel.defaultAddressLiveData.value as Fail).throwable,mockThrowable)

    }


    @Test
    fun successTopAdsDataLiveData()
    {
        val topAdsRequestParams = mockk<TopAdsRequestParams>(relaxed = true)
        val thanksPageData = mockk<ThanksPageData>(relaxed = true)
         var listTopAdsUIList: MutableList<TopAdsUIModel> = ArrayList()
        val topAdsUIModel = mockk<TopAdsUIModel>(relaxed = true)
        listTopAdsUIList.add(topAdsUIModel)

        coEvery {
            thankYouTopAdsViewModelUseCase.getTopAdsData(topAdsRequestParams,thanksPageData,any(),any())
        }coAnswers {
            thirdArg<(List<TopAdsUIModel>) -> Unit>().invoke(listTopAdsUIList)
        }

    viewModel.loadTopAdsViewModelData(topAdsRequestParams,thanksPageData)
        Assert.assertEquals(viewModel.topAdsDataLiveData.value, topAdsRequestParams)
    }


    @Test
    fun successGyroResponseLiveData()
    {
        val thankPageData = mockk<ThanksPageData>(relaxed = true)
        val featureEngineData = mockk<FeatureEngineData>(relaxed = true)
        val validateEngineResponse = ValidateEngineResponse(true,"","",featureEngineData)
        coEvery {
            gyroEngineRequestUseCase.getFeatureEngineData(thankPageData,any())
        } coAnswers {
            secondArg<(ValidateEngineResponse) -> Unit>().invoke(validateEngineResponse)
        }
        viewModel.getFeatureEngine(thankPageData)
        Assert.assertEquals(viewModel.gyroResponseLiveData.value,featureEngineData)
    }
}