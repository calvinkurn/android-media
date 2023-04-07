package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
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

    private val hasMoreDataObserver: Observer<Boolean> = mockk(relaxed = true)
    private val totalCount: Observer<Int> = mockk(relaxed = true)
    private val educationCategoryChipObserver: Observer<List<Visitable<AffiliateAdapterTypeFactory>>> =
        mockk(relaxed = true)

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        affiliateEducationSeeAllViewModel.hasMoreData().observeForever(hasMoreDataObserver)
        affiliateEducationSeeAllViewModel.getTotalCount().observeForever(totalCount)
        affiliateEducationSeeAllViewModel.getEducationCategoryChip()
            .observeForever(educationCategoryChipObserver)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should fetch cards upon success`() {
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

        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
        } returns educationArticleResponse
        val expectedList: List<AffiliateEduCategoryChipModel> = mockk(relaxed = true)

        coEvery {
            educationCategoryUseCase.getEducationFilterChips(
                any(),
                any()
            )
        } returns expectedList

        affiliateEducationSeeAllViewModel.fetchSeeAllData("", "")
        verifyOrder {
            educationCategoryChipObserver.onChanged(expectedList)
            hasMoreDataObserver.onChanged(cardItem.hasMore)
            totalCount.onChanged(cardItem.totalCount)
        }
        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `cards should be empty upon empty response`() {
        val educationArticleResponse =
            AffiliateEducationArticleCardsResponse(
                AffiliateEducationArticleCardsResponse.CardsArticle(
                    AffiliateEducationArticleCardsResponse.CardsArticle.Data()
                )
            )

        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
        } returns educationArticleResponse
        val expectedList: List<AffiliateEduCategoryChipModel> = mockk(relaxed = true)
        coEvery {
            educationCategoryUseCase.getEducationFilterChips(any(), any())
        } returns expectedList

        affiliateEducationSeeAllViewModel.fetchSeeAllData("", "")

        assertTrue(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(null, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertFalse(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should fetch category list`() {
        val expectedList: List<AffiliateEduCategoryChipModel> = mockk(relaxed = true)
        val educationArticleResponse: AffiliateEducationArticleCardsResponse = spyk()
        coEvery {
            educationCategoryUseCase.getEducationFilterChips(
                any(),
                any()
            )
        } returns expectedList
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
        } returns educationArticleResponse
        affiliateEducationSeeAllViewModel.fetchSeeAllData("", "")
        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
    }

    @Test
    fun `reset list and should fetch category list`() {
        affiliateEducationSeeAllViewModel.resetList(PAGE_EDUCATION_EVENT, "")
        val educationArticleResponse: AffiliateEducationArticleCardsResponse = spyk()
        val expectedList: List<AffiliateEduCategoryChipModel> = mockk(relaxed = true)

        coEvery {
            educationCategoryUseCase.getEducationFilterChips(
                any(),
                any()
            )
        } returns expectedList
        if (affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty()) {
            coEvery {
                educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
            } returns educationArticleResponse
        }

        affiliateEducationSeeAllViewModel.fetchSeeAllData("", "")
        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
    }
}
