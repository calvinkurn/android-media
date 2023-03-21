package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationSearchArticleCardsResponse
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationSearchResultUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateEducationSearchArticleViewModelTest {
    private val educationSearchResultUseCase: AffiliateEducationSearchResultUseCase = mockk()
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase = mockk()
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase = mockk()
    private val affiliateEducationSearchArticleViewModel = spyk(
        AffiliateEducationSearchArticleViewModel(
            educationSearchResultUseCase,
            educationCategoryUseCase,
            educationArticleCardsUseCase
        )
    )

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Search Article Card Success`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        coEvery { educationSearchResultUseCase.getEducationSearchResultCards(any()) } returns affiliateEducationSearchArticleCardsResponse

        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertNotNull(affiliateEducationSearchArticleViewModel.getEducationSearchData().value)
    }

    @Test
    fun `latest Article Card Success`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val metaData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Meta(
                totalHits = 0
            )
        val sectionData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem(
                id = "articles",
                meta = metaData
            )
        val resultsItem =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem(
                section = listOf(sectionData)
            )
        val data = AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data(
            results = listOf(resultsItem)
        )
        val searchData = AffiliateEducationSearchArticleCardsResponse.SearchEducation(
            data = data
        )
        coEvery { affiliateEducationSearchArticleCardsResponse.searchEducation } returns searchData
        coEvery { educationSearchResultUseCase.getEducationSearchResultCards(any()) } returns affiliateEducationSearchArticleCardsResponse

        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        val affiliateEducationArticleCardsResponse: AffiliateEducationArticleCardsResponse =
            mockk(relaxed = true)
        val cardItemData = AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem(
            totalCount = 1,
            actionLink = "",
            offset = 0,
            appActionLink = "",
            id = "1"
        )
        val cardData = AffiliateEducationArticleCardsResponse.CardsArticle.Data(
            cards = listOf(cardItemData)
        )
        val cardsArticleData = AffiliateEducationArticleCardsResponse.CardsArticle(
            data = cardData
        )
        coEvery { affiliateEducationArticleCardsResponse.cardsArticle } returns cardsArticleData
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(
                any(),
                filter = "latest"
            )
        } returns affiliateEducationArticleCardsResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertEquals(affiliateEducationSearchArticleViewModel.getLatestCardCount().value, 1)
    }

    @Test
    fun `Search Article Card Exception on search card`() {
        val exception = "Validate Data Exception"
        coEvery { educationSearchResultUseCase.getEducationSearchResultCards(any()) } throws Exception(
            exception
        )

        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("", "", "")

        assertEquals(affiliateEducationSearchArticleViewModel.getErrorMessage().value, exception)
    }

    @Test
    fun `Search Article Card Exception on category chips`() {
        val exception = "Validate Data Exception"
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        coEvery { educationSearchResultUseCase.getEducationSearchResultCards(any()) } returns affiliateEducationSearchArticleCardsResponse
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } throws Exception(exception)

        affiliateEducationSearchArticleViewModel.fetchSearchData("", "", "")

        assertEquals(affiliateEducationSearchArticleViewModel.getErrorMessage().value, exception)
    }
}
