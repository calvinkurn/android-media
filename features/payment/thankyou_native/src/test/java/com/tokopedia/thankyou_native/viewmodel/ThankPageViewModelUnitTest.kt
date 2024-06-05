package com.tokopedia.thankyou_native.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetDefaultChosenAddressUseCase
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.FeatureEngineItem
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.TopAdsUIModel
import com.tokopedia.thankyou_native.domain.model.ValidateEngineResponse
import com.tokopedia.thankyou_native.domain.model.WalletBalance
import com.tokopedia.thankyou_native.domain.repository.DynamicChannelRepository
import com.tokopedia.thankyou_native.domain.usecase.FetchWalletBalanceUseCase
import com.tokopedia.thankyou_native.domain.usecase.GyroEngineMapperUseCase
import com.tokopedia.thankyou_native.domain.usecase.GyroEngineRequestUseCase
import com.tokopedia.thankyou_native.domain.usecase.ThankYouTopAdsViewModelUseCase
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageDataUseCase
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageDataV2UseCase
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageMapperUseCase
import com.tokopedia.thankyou_native.domain.usecase.TopTickerUseCase
import com.tokopedia.thankyou_native.presentation.adapter.model.BannerWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.HeadlineAdsWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.TokoMemberRequestParam
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.thankyou_native.presentation.adapter.model.WaitingHeaderUiModel
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.tokomember.model.MembershipRegister
import com.tokopedia.tokomember.usecase.MembershipRegisterUseCase
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
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
    private val thankPageUseCaseV2 = mockk<ThanksPageDataV2UseCase>(relaxed = true)
    private val thanksPageMapperUseCase = mockk<ThanksPageMapperUseCase>(relaxed = true)
    private val gyroEngineRequestUseCase = mockk<GyroEngineRequestUseCase>(relaxed = true)
    private val gyroEngineMapperUseCase = mockk<GyroEngineMapperUseCase>(relaxed = true)
    private val topTickerUseCase = mockk<TopTickerUseCase>(relaxed = true)
    private val defaultAddressUseCase = mockk<GetDefaultChosenAddressUseCase>(relaxed = true)
    private val walletBalanceUseCase = mockk<FetchWalletBalanceUseCase>(relaxed = true)
    private val thankYouTopAdsViewModelUseCase =
        mockk<ThankYouTopAdsViewModelUseCase>(relaxed = true)
    private val membershipRegisterUseCase = mockk<MembershipRegisterUseCase>(relaxed = true)
    private val dynamicChannelRepository = mockk<DynamicChannelRepository>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ThanksPageDataViewModel(
            thankPageUseCase,
            thankPageUseCaseV2,
            thanksPageMapperUseCase,
            gyroEngineRequestUseCase,
            walletBalanceUseCase,
            gyroEngineMapperUseCase,
            topTickerUseCase,
            defaultAddressUseCase,
            thankYouTopAdsViewModelUseCase,
            membershipRegisterUseCase,
            dynamicChannelRepository,
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

        viewModel.getThanksPageData("", "", false)
        Assert.assertEquals(
            (viewModel.thanksPageDataResultLiveData.value as Success).data,
            thankPageData
        )
    }

    @Test
    fun successThanksPageResultConditionFail() {
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
            thirdArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getThanksPageData("", "", false)
        Assert.assertEquals(
            (viewModel.thanksPageDataResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun failThanksPageResult() {
        coEvery {
            thankPageUseCase.getThankPageData(any(), any(), "", "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getThanksPageData("", "", false)
        Assert.assertEquals(
            (viewModel.thanksPageDataResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun successGyroRecommendationLiveData() = runTest {
        val gyroRecommendation = mockk<GyroRecommendation>(relaxed = true)
        val featureEngineData = mockk<FeatureEngineData>(relaxed = true)
        val tokoMemberRequestParam = mockk<TokoMemberRequestParam>(relaxed = true)

        coEvery {
            gyroEngineMapperUseCase.getFeatureListData(
                featureEngineData,
                tokoMemberRequestParam,
                any(),
                any()
            )
        } coAnswers {
            thirdArg<(GyroRecommendation) -> Unit>().invoke(gyroRecommendation)
        }

        viewModel.postGyroRecommendation(featureEngineData, tokoMemberRequestParam)
        Assert.assertEquals(viewModel.gyroRecommendationLiveData.value, gyroRecommendation)
    }

    @Test
    fun successMembershipRegisterLiveData() {
        val membershipRegisterData = mockk<MembershipRegister>(relaxed = true)
        coEvery {
            membershipRegisterUseCase.registerMembership("", any(), any())
        } coAnswers {
            secondArg<(MembershipRegister) -> Unit>().invoke(membershipRegisterData)
        }
        viewModel.registerTokomember("")
        Assert.assertEquals(
            (viewModel.membershipRegisterData.value as Success).data,
            membershipRegisterData
        )
    }

    @Test
    fun failMembershipRegisterLiveData() {
        coEvery {
            membershipRegisterUseCase.registerMembership("", any(), any())
        } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.registerTokomember("")
        Assert.assertEquals(
            (viewModel.membershipRegisterData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun successTopTickerLiveData() {
        val tickerData = mockk<List<TickerData>>(relaxed = true)
        coEvery {
            topTickerUseCase.getTopTickerData("", any(), any())
        } coAnswers {
            secondArg<(List<TickerData>) -> Unit>().invoke(tickerData)
        }
        viewModel.getThanksPageTicker("")
        Assert.assertEquals((viewModel.topTickerLiveData.value as Success).data, tickerData)
    }

    @Test
    fun failTopTickerLiveData() {
        coEvery {
            topTickerUseCase.getTopTickerData("", any(), any())
        } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getThanksPageTicker("")
        Assert.assertEquals((viewModel.topTickerLiveData.value as Fail).throwable, mockThrowable)
    }

    @Test
    fun successDefaultAddressLiveData() {
        val getDefaultChosenAddressResponse = mockk<GetDefaultChosenAddressResponse>(relaxed = true)
        coEvery {
            defaultAddressUseCase(any())
        } returns GetDefaultChosenAddressGqlResponse(getDefaultChosenAddressResponse)

        viewModel.resetAddressToDefault()
        Assert.assertEquals(
            (viewModel.defaultAddressLiveData.value as Success).data,
            getDefaultChosenAddressResponse
        )
    }

    @Test
    fun failDefaultAddressLiveData() {
        val error = Exception()
        coEvery {
            defaultAddressUseCase(any())
        } throws error
        viewModel.resetAddressToDefault()
        Assert.assertEquals(
            (viewModel.defaultAddressLiveData.value as Fail).throwable,
            error
        )
    }

    @Test
    fun successTopAdsDataLiveData() {
        val topAdsRequestParams = mockk<TopAdsRequestParams>(relaxed = true)
        val thanksPageData = mockk<ThanksPageData>(relaxed = true)
        var listTopAdsUIList: MutableList<TopAdsUIModel> = ArrayList()
        val topAdsUIModel = mockk<TopAdsUIModel>(relaxed = true)
        listTopAdsUIList.add(topAdsUIModel)

        coEvery {
            thankYouTopAdsViewModelUseCase.getTopAdsData(
                topAdsRequestParams,
                thanksPageData,
                any(),
                any()
            )
        } coAnswers {
            thirdArg<(List<TopAdsUIModel>) -> Unit>().invoke(listTopAdsUIList)
        }

        viewModel.loadTopAdsViewModelData(topAdsRequestParams, thanksPageData)
        Assert.assertEquals(viewModel.topAdsDataLiveData.value, topAdsRequestParams)
    }

    @Test
    fun `check for wallet activation`() {
        val walletBalanceData = WalletBalance("SUCCESS", arrayListOf())
        coEvery {
            walletBalanceUseCase.getGoPayBalance(any())
        } coAnswers {
            firstArg<(WalletBalance?) -> Unit>().invoke(walletBalanceData)
        }
    }

    @Test
    fun successGyroResponseLiveData() {
        val thankPageData = mockk<ThanksPageData>(relaxed = true)
        val featureEngineData = mockk<FeatureEngineData>(relaxed = true)
        val validateEngineResponse = ValidateEngineResponse(true, "", "", featureEngineData)

        `check for wallet activation`()
        coEvery {
            gyroEngineRequestUseCase.getFeatureEngineData(thankPageData, any(), any())
        } coAnswers {
            thirdArg<(ValidateEngineResponse) -> Unit>().invoke(validateEngineResponse)
        }
        runBlocking {
            launch {
                viewModel.checkForGoPayActivation(thankPageData, "")
            }
            delay(500)
            Assert.assertEquals(viewModel.gyroResponseLiveData.value, featureEngineData)
        }
    }

    @Test
    fun `Feature engine has banner data`() {
        val expectedTitle = "Biar belanjamu makin #PraktisAbis"
        val expectedBannerItemSize = 2
        val thankPageData = mockk<ThanksPageData>(relaxed = true)
        val validateEngineResponse = ValidateEngineResponse(
            true,
            "",
            "",
            FeatureEngineData(
                "",
                "",
                arrayListOf(
                    FeatureEngineItem(
                        id = 67,
                        detail = "{\"type\":\"banner\",\"section_title\":\"Biar belanjamu makin #PraktisAbis\",\"banner_data\":\"[{\\\"asset_url\\\": \\\"https://images.tokopedia.net/img/cache/900/QBrNqa/2023/3/3/4f9ffabb-e2cc-4aea-b374-76d534f0f519.png\\\",\\\"url\\\": \\\"https://www.tokopedia.com/tokopedia-cobrand\\\",\\\"applink\\\": \\\"tokopedia://webview?url\u003dhttps://www.tokopedia.com/tokopedia-cobrand\\\"},{\\\"asset_url\\\": \\\"https://images.tokopedia.net/img/cache/1208/NsjrJu/2023/3/17/b3d19a1c-678d-4ec1-8807-0213ea11f76b.jpg\\\",\\\"url\\\": \\\"https://www.tokopedia.com/discovery/serbu-official-store?source\u003dhomepage.slider_banner.0.42009\\\",\\\"applink\\\": \\\"tokopedia://buyer/payment\\\"}]\"}"
                    )
                )
            )
        )

        // given
        `check for wallet activation`()
        coEvery {
            gyroEngineRequestUseCase.getFeatureEngineData(thankPageData, any(), any())
        } coAnswers {
            thirdArg<(ValidateEngineResponse) -> Unit>().invoke(validateEngineResponse)
        }

        runBlocking {
            launch {
                // when
                viewModel.checkForGoPayActivation(thankPageData, "")

                delay(500)

                // then
                Assert.assertEquals(viewModel.bannerLiveData.value?.title, expectedTitle)
                Assert.assertEquals(viewModel.bannerLiveData.value?.items?.size, expectedBannerItemSize)
            }
        }
    }

    @Test
    fun `Feature engine has widget order data`() {
        val thankPageData = mockk<ThanksPageData>(relaxed = true)
        val gyroVisitable = GyroRecommendationWidgetModel(
            mockk(relaxed = true),
            thankPageData,
            mockk(relaxed = true)
        )
        val headlineAdsVisitable = HeadlineAdsWidgetModel(
            mockk(relaxed = true)
        )
        val bannerWidgetModel = BannerWidgetModel()
        val validateEngineResponse = ValidateEngineResponse(
            true,
            "",
            "",
            FeatureEngineData(
                "",
                "",
                arrayListOf(
                    FeatureEngineItem(
                        id = 67,
                        detail = "{\"type\":\"config\",\"widget_order\":\"banner, dg, pg, shopads, feature\"}"
                    )
                )
            )
        )

        // given
        `check for wallet activation`()
        coEvery {
            gyroEngineRequestUseCase.getFeatureEngineData(thankPageData, any(), any())
        } coAnswers {
            thirdArg<(ValidateEngineResponse) -> Unit>().invoke(validateEngineResponse)
        }

        runBlocking {
            launch {
                viewModel.addBottomContentWidget(bannerWidgetModel)
                viewModel.checkForGoPayActivation(thankPageData, "")
                viewModel.addBottomContentWidget(gyroVisitable)
                viewModel.addBottomContentWidget(headlineAdsVisitable)
            }

            delay(1000)

            // assert
            Assert.assertEquals(viewModel.widgetOrder, arrayListOf("instant_header","waiting_header","processing_header","divider","banner", "dg", "pg", "shopads", "feature"))
            Assert.assertEquals(viewModel.bottomContentVisitableList.value?.size, 3)
            Assert.assertEquals(viewModel.bottomContentVisitableList.value?.first(), bannerWidgetModel)
            Assert.assertEquals(viewModel.bottomContentVisitableList.value?.get(1), headlineAdsVisitable)
            Assert.assertEquals(viewModel.bottomContentVisitableList.value?.last(), gyroVisitable)
        }
    }

    @Test
    fun `Show Tokomember Widget`() {
        val thankPageData = mockk<ThanksPageData>(relaxed = true)
        val validateEngineResponse = ValidateEngineResponse(
            true,
            "",
            "",
            FeatureEngineData(
                "",
                "",
                arrayListOf(
                    FeatureEngineItem(
                        id = 67,
                        detail = "{\"type\":\"tokomember\"}"
                    )
                )
            )
        )
        val queryParamTokomember = TokoMemberRequestParam(shopID = 0, amount = 0.0F, pageType = null, paymentID = "", source = 1, orderData = listOf(), sectionTitle = "", sectionSubtitle = "", isFirstElement = false)

        // given
        `check for wallet activation`()
        coEvery {
            gyroEngineRequestUseCase.getFeatureEngineData(thankPageData, any(), any())
        } coAnswers {
            thirdArg<(ValidateEngineResponse) -> Unit>().invoke(validateEngineResponse)
        }

        runBlocking {
            launch {
                viewModel.checkForGoPayActivation(thankPageData, "")
            }
            delay(500)
            verify {
                gyroEngineMapperUseCase.getFeatureListData(
                    any(),
                    queryParamTokomember,
                    any(),
                    any()
                )
            }
        }

        // when
    }

    @Test
    fun `Load ThanksPageData V2`() {
        // given
        val isV2 = true

        // when
        viewModel.getThanksPageData("", "", isV2)

        // verify
        verify {
            thankPageUseCaseV2.getThankPageData(any(), any(), any(), any())
        }
    }

    @Test
    fun `Thanks page data has ticker`() {
        val waitingHeaderUiModel = WaitingHeaderUiModel(
            "",
            "",
            0L,
            "",
            "",
            "",
            "",
            0L,
            arrayListOf(),
            false,
            "",
            "",
            "",
            false,
            "",
            "",
            "",
            false,
            ""
        )
        val tickerData = arrayListOf(TickerData("", 0))

        // given
        val isV2 = true

        // when
        viewModel.getThanksPageData("", "", isV2)
        viewModel.addBottomContentWidget(waitingHeaderUiModel)
        viewModel.setTicker(tickerData)

        // then
        Assert.assertEquals((viewModel.bottomContentVisitableList.value?.first() as WaitingHeaderUiModel).tickerData, tickerData)
    }
}
