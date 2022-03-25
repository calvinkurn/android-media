package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.top_ads_headline_usecase.CreateHeadlineAdsUseCase
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput2
import com.tokopedia.top_ads_headline_usecase.model.TopadsManageHeadlineAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.data.model.insightkey.TopAdsShopHeadlineKeyword
import com.tokopedia.topads.dashboard.data.model.insightkey.TopadsHeadlineKeywordSuggestion
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopKeywordSuggestionUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsInsightViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TopAdsInsightViewModel
    private lateinit var createHeadlineAdsUseCase: CreateHeadlineAdsUseCase
    private lateinit var shopKeywordSuggestionUseCase: TopAdsShopKeywordSuggestionUseCase

    @Before
    fun setUp() {
        createHeadlineAdsUseCase = mockk(relaxed = true)
        shopKeywordSuggestionUseCase = mockk(relaxed = true)
        viewModel =
            spyk(TopAdsInsightViewModel(createHeadlineAdsUseCase, shopKeywordSuggestionUseCase))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `applyRecommendedKeywords on non empty id should invoke keyword count in live data`() {
            val actualCount = 1
            val mockObject = TopAdsManageHeadlineInput2(
                TopAdsManageHeadlineInput2.Operation(
                    group = TopAdsManageHeadlineInput2.Operation.Group(
                        keywordOperations = listOf(TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation())
                    )
                ))
            val responseMockObject =
                TopadsManageHeadlineAdResponse.Data(TopadsManageHeadlineAdResponse.Data.TopadsManageHeadlineAd(
                    TopadsManageHeadlineAdResponse.Data.TopadsManageHeadlineAd.Success("1", ""),
                    emptyList()))

            coEvery {
                createHeadlineAdsUseCase.setParams(mockObject)
            } returns Unit

            coEvery { createHeadlineAdsUseCase.executeOnBackground() } returns responseMockObject
            viewModel.applyRecommendedKeywords(mockObject)

            assertEquals(viewModel.applyKeyword.value, actualCount)
        }

    @Test
    fun `valid object response on getShopKeyword method  should invoke success value to live data`() {
            val mockObject =
                TopAdsShopHeadlineKeyword(TopadsHeadlineKeywordSuggestion(RecommendedKeywordData()))
            val gqlResponse =
                mockk<com.tokopedia.graphql.data.model.GraphqlResponse>(relaxed = true)

            coEvery {
                shopKeywordSuggestionUseCase.getKeywordRecommendation(any())
            } returns gqlResponse

            coEvery {
                gqlResponse.getData<TopAdsShopHeadlineKeyword>(TopAdsShopHeadlineKeyword::class.java)
            } returns mockObject

            viewModel.getShopKeywords("", emptyArray())

            viewModel.recommendedKeyword.value?.let {
                assertTrue(it == mockObject.suggestion!!.recommendedKeywordData)
            }
        }
}