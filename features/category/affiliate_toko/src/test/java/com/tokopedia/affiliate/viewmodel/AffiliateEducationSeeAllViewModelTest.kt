package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateEducationSeeAllViewModelTest {
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase = mockk()
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase = mockk()
    private val affiliateEducationSeeAllViewModel: AffiliateEducationSeeAllViewModel = spyk(
        AffiliateEducationSeeAllViewModel(educationCategoryUseCase, educationArticleCardsUseCase)
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
    fun `fetches cards upon success`() {
        val articleCards =
            AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem.Article()

        val cardItem =
            AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem(
                offset = 1,
                hasMore = true,
                totalCount = 10,
                articles = listOf(articleCards, articleCards, articleCards)
            )

        val educationArticleResponse =
            AffiliateEducationArticleCardsResponse(
                AffiliateEducationArticleCardsResponse.CardsArticle(
                    AffiliateEducationArticleCardsResponse.CardsArticle.Data(
                        listOf(cardItem, cardItem)
                    )
                )
            )

        val educationCategoryTreeResponse: AffiliateEducationCategoryResponse = spyk()

        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
        } returns educationArticleResponse
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns educationCategoryTreeResponse

        affiliateEducationSeeAllViewModel.fetchSeeAllData("", "")

        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }
}
