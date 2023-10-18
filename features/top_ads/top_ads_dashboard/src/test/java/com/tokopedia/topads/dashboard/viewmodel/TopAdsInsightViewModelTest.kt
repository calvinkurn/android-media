package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.domain.usecase.CreateHeadlineAdsUseCase
import com.tokopedia.topads.common.domain.model.createheadline.TopAdsManageHeadlineInput2
import com.tokopedia.topads.common.domain.model.createheadline.TopadsManageHeadlineAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.data.model.insightkey.TopAdsShopHeadlineKeyword
import com.tokopedia.topads.dashboard.data.model.insightkey.TopadsHeadlineKeywordSuggestion
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopKeywordSuggestionUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
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

        val mockObject = TopAdsManageHeadlineInput2(
            TopAdsManageHeadlineInput2.Operation(group = TopAdsManageHeadlineInput2.Operation.Group(
                keywordOperations = listOf(TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation())
            )))
        val responseMockObject : TopadsManageHeadlineAdResponse.Data = mockk()

        every { createHeadlineAdsUseCase.setParams(mockObject) } returns Unit
        coEvery { createHeadlineAdsUseCase.executeOnBackground() } returns responseMockObject
        every { responseMockObject.topadsManageHeadlineAd.success.id } returns "12"

        viewModel.applyRecommendedKeywords(mockObject)
        assertEquals(viewModel.applyKeyword.value,
            mockObject.operation.group.keywordOperations.size)
    }

    @Test
    fun `applyRecommendedKeywords onError block test`() {
        every {
            createHeadlineAdsUseCase.setParams(mockk<TopAdsManageHeadlineInput2>())
        } answers {
            throw Exception()
        }

        viewModel.applyRecommendedKeywords(mockk())
        assert(!viewModel.error.value.isNullOrEmpty())
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
        assertEquals(viewModel.recommendedKeyword.value , mockObject.suggestion!!.recommendedKeywordData)
    }

    @Test
    fun `getShopKeywords onError block test`() = testRule.runTest{
        val exp = "error"

        coEvery { shopKeywordSuggestionUseCase.getKeywordRecommendation(any()) } throws Exception(exp)

        viewModel.getShopKeywords("", emptyArray())
        assertEquals(exp, viewModel.error.value)
    }
}
