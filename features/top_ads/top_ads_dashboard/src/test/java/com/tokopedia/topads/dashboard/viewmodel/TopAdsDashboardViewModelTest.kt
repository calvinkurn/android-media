package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.topads.common.data.model.WhiteListUserResponse
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.TopadsDashboardDeposits
import com.tokopedia.topads.common.domain.usecase.GetWhiteListedUserUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsTickerUseCase
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.IS_TOP_UP_CREDIT_NEW_UI
import com.tokopedia.topads.dashboard.data.model.GetPersonalisedCopyResponse
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.raw.topAdsHomepageLatestReadingJson
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopadsRecommendationStatisticsUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.domain.usecase.TopAdsGetSelectedTopUpTypeUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsDashboardViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var summaryStatisticsUseCase: TopAdsWidgetSummaryStatisticsUseCase
    private lateinit var recommendationStatisticsUseCase: TopadsRecommendationStatisticsUseCase
    private lateinit var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase
    private lateinit var topadsTickerUseCase: TopAdsTickerUseCase
    private lateinit var autoTopUpUSeCase: TopAdsAutoTopUpUSeCase
    private lateinit var topAdsGetSelectedTopUpTypeUseCase: TopAdsGetSelectedTopUpTypeUseCase
    private lateinit var whiteListedUserUseCase: GetWhiteListedUserUseCase
    private lateinit var topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase
    private lateinit var topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase
    private lateinit var viewModel: TopAdsDashboardViewModel

    @Before
    fun setUp() {
        summaryStatisticsUseCase = mockk(relaxed = true)
        recommendationStatisticsUseCase = mockk(relaxed = true)
        topAdsGetShopDepositUseCase = mockk(relaxed = true)
        topadsTickerUseCase = mockk(relaxed = true)
        autoTopUpUSeCase = mockk(relaxed = true)
        topAdsGetSelectedTopUpTypeUseCase = mockk(relaxed = true)
        whiteListedUserUseCase = mockk(relaxed = true)
        topAdsListAllInsightCountsUseCase = mockk(relaxed = true)
        topAdsGetTotalAdGroupsWithInsightUseCase = mockk(relaxed = true)

        viewModel = TopAdsDashboardViewModel(
            testRule.dispatchers,
            summaryStatisticsUseCase,
            recommendationStatisticsUseCase,
            topAdsGetShopDepositUseCase,
            topadsTickerUseCase,
            autoTopUpUSeCase,
            topAdsGetSelectedTopUpTypeUseCase,
            whiteListedUserUseCase,
            topAdsListAllInsightCountsUseCase,
            topAdsGetTotalAdGroupsWithInsightUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getShopDeposit() success check`() {
        val expected = 10

        every { topAdsGetShopDepositUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(Deposit) -> Unit>().invoke(
                Deposit(TopadsDashboardDeposits(DepositAmount(expected)))
            )
        }

        viewModel.fetchShopDeposit()
        assertEquals(expected, (viewModel.shopDepositLiveData.value as Success).data.amount)
    }

    @Test
    fun `getShopDeposit() failure check`() {
        val mockThrowable = Throwable()

        every { topAdsGetShopDepositUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.fetchShopDeposit()
        assertEquals(mockThrowable, (viewModel.shopDepositLiveData.value as Fail).throwable)
    }

    @Test
    fun `fetchRecommendationStatistics success check`() {
        val expected =
            RecommendationStatistics(
                RecommendationStatistics.Statistics(
                    mockk(relaxed = true),
                    mockk(),
                    mockk()
                )
            )

        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns expected

        viewModel.fetchRecommendationStatistics()
        assertEquals(
            (viewModel.recommendationStatsLiveData.value as Success).data,
            expected.statistics.data
        )
    }

    @Test
    fun `fetchRecommendationStatistics failure check `() {
        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns null

        viewModel.fetchRecommendationStatistics()
        assert(viewModel.recommendationStatsLiveData.value is Fail)
    }

    @Test
    fun `fetchRecommendationStatistics onError block test`() {
        coEvery { recommendationStatisticsUseCase.fetchRecommendationStatistics() } returns mockk()

        viewModel.fetchRecommendationStatistics()
        assert(viewModel.recommendationStatsLiveData.value is Fail)
    }

    @Test
    fun `fetchSummaryStatistics success check`() {
        val expectedData =
            TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics(
                listOf(),
                mockk()
            )
        val fakeResponse: TopadsWidgetSummaryStatisticsModel = mockk()

        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns fakeResponse
        every { fakeResponse.topadsWidgetSummaryStatistics.widgetSummaryStatistics } returns expectedData

        viewModel.fetchSummaryStatistics("", "", "")
        assertEquals((viewModel.summaryStatisticsLiveData.value as Success).data, expectedData)
    }

    @Test
    fun `fetchSummaryStatistics failure check`() {
        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns null

        viewModel.fetchSummaryStatistics("", "", "")
        assert(viewModel.summaryStatisticsLiveData.value is Fail)
    }

    @Test
    fun `fetchSummaryStatistics onError block test`() {
        coEvery {
            summaryStatisticsUseCase.getSummaryStatistics(any(), any(), any())
        } returns mockk()

        viewModel.fetchSummaryStatistics("", "", "")
        assert(viewModel.summaryStatisticsLiveData.value is Fail)
    }

    @Test
    fun `getLatestReadings success test`() {
        val expected: TopAdsLatestReading = TopAdsLatestReading().apply {
            add(TopAdsLatestReading.TopAdsLatestReadingItem(listOf(), "", "", ""))
        }

        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(
                topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java
            )
        } returns expected

        viewModel.getLatestReadings()
        assertEquals(expected[0].articles, (viewModel.latestReadingLiveData.value as Success).data)
    }

    @Test
    fun `getLatestReadings failure test`() {
        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(
                topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java
            )
        } returns TopAdsLatestReading()

        viewModel.getLatestReadings()
        assert(viewModel.latestReadingLiveData.value is Fail)
    }

    @Test
    fun `getLatestReadings onError block test`() {
        mockkConstructor(Gson::class)
        every {
            anyConstructed<Gson>().fromJson(
                topAdsHomepageLatestReadingJson,
                TopAdsLatestReading::class.java
            )
        } returns mockk()

        viewModel.getLatestReadings()
        assert(viewModel.latestReadingLiveData.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus success - response not null and error is empty test, livedata should contain data`() {
        val mockObject = AutoTopUpData.Response(AutoTopUpData(AutoTopUpStatus()))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatus()
        assertEquals(
            (viewModel.autoTopUpStatusLiveData.value as Success).data,
            mockObject.response?.data
        )
    }

    @Test
    fun `getAutoTopUpStatus response not null and error not empty test, livedata should be fail`() {
        val actual = AutoTopUpData.Response(AutoTopUpData(errors = listOf(Error())))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatus()
        assertTrue(viewModel.autoTopUpStatusLiveData.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus on exception occured test`() {
        val actual = Exception("it")

        every { autoTopUpUSeCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatus()
        assertEquals((viewModel.autoTopUpStatusLiveData.value as Fail).throwable, actual)
    }

    @Test
    fun `test success in getSelectedTopUpType when credit performance is topUpFrequently`() {
        val actual = GetPersonalisedCopyResponse(
            GetPersonalisedCopyResponse.GetPersonalisedCopy(
                GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData(creditPerformance = TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_UP_FREQUENTLY)
            )
        )
        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(GetPersonalisedCopyResponse) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()

        assertEquals((viewModel.getAutoTopUpDefaultSate.value as Success).data.isAutoTopUpSelected, true)
    }

    @Test
    fun `test success in getSelectedTopUpType when credit performance is INSUFFICIENT_CREDIT`() {
        val actual = GetPersonalisedCopyResponse(
            GetPersonalisedCopyResponse.GetPersonalisedCopy(
                GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData(creditPerformance = TopAdsDashboardConstant.TopAdsCreditTopUpConstant.INSUFFICIENT_CREDIT)
            )
        )
        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(GetPersonalisedCopyResponse) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()

        assertEquals((viewModel.getAutoTopUpDefaultSate.value as Success).data.isAutoTopUpSelected, true)
    }

    @Test
    fun `test success in getSelectedTopUpType when credit performance is not INSUFFICIENT_CREDIT and not TOP_UP_FREQUENTLY`() {
        val actual = GetPersonalisedCopyResponse(
            GetPersonalisedCopyResponse.GetPersonalisedCopy(
                GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData()
            )
        )
        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(GetPersonalisedCopyResponse) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()

        assertEquals((viewModel.getAutoTopUpDefaultSate.value as Success).data.isAutoTopUpSelected, false)
    }

    @Test
    fun `getSelectedTopUpType on exception ,livedata value should be Fail`() {
        val actual = Throwable("my exception")

        every { topAdsGetSelectedTopUpTypeUseCase.execute(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }
        viewModel.getSelectedTopUpType()
        assertEquals(
            (viewModel.getAutoTopUpDefaultSate.value as Fail).throwable.message,
            actual.message
        )
    }

    @Test
    fun `test success in getWhiteListedUser when credit performance is IS_TOP_UP_CREDIT_NEW_UI`() {
        val actual = WhiteListUserResponse.TopAdsGetShopWhitelistedFeature(
            listOf(
                WhiteListUserResponse.TopAdsGetShopWhitelistedFeature.Data(featureName = IS_TOP_UP_CREDIT_NEW_UI)
            )
        )
        every { whiteListedUserUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(WhiteListUserResponse.TopAdsGetShopWhitelistedFeature) -> Unit>().invoke(
                actual
            )
        }
        viewModel.getWhiteListedUser()

        assertTrue((viewModel.isUserWhitelisted.value as Success).data)
    }

    @Test
    fun `test Fail in getWhiteListedUser `() {
        val actual = Throwable("my exception")
        every { whiteListedUserUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(
                actual
            )
        }
        viewModel.getWhiteListedUser()

        assertEquals((viewModel.isUserWhitelisted.value as Fail).throwable.message, actual.message)
    }
}
