package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.PAGE_EDUCATION_TUTORIAL
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
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
    fun `should fetch event cards upon success`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 382,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_EVENT, "")

        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should fetch article cards upon success`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 381,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_ARTICLE, "")

        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should fetch tutorial cards upon success`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 383,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_TUTORIAL, "")

        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should not update category on null page type`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 383,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(null, "")

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should fetch event cards upon success with categoryId`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 123
            )
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 382,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_EVENT, "123")

        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should fetch article cards upon success with categoryId`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 123
            )
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 381,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_ARTICLE, "123")

        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should fetch tutorial cards upon success with categoryId`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 123
            )
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 383,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_TUTORIAL, "123")

        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should not update category on null page type with categoryId`() {
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

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 123
            )
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 383,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        affiliateEducationSeeAllViewModel.fetchSeeAllData(null, "123")

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        assertFalse(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(true, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertTrue(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should not fetch chips when not empty`() {
        val educationArticleResponse: AffiliateEducationArticleCardsResponse = mockk(relaxed = true)

        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
        } returns educationArticleResponse

        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())

        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 382,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )

        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        // calling first time should fetch chips
        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_EVENT, "")
        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
        val chipSize = affiliateEducationSeeAllViewModel.getEducationCategoryChip().value?.size
        // calling again should not change chip size
        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_EVENT, "")
        assertTrue(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value?.size == chipSize)
    }

    @Test
    fun `should not populate data when response null or empty`() {
        val educationArticleResponse: AffiliateEducationArticleCardsResponse = mockk(relaxed = true)
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
        } returns educationArticleResponse

        affiliateEducationSeeAllViewModel.fetchSeeAllData("", "")
        assertTrue(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(null, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertEquals(null, affiliateEducationSeeAllViewModel.getTotalCount().value)
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

        affiliateEducationSeeAllViewModel.fetchSeeAllData("", "")

        assertTrue(affiliateEducationSeeAllViewModel.getEducationSeeAllData().value.isNullOrEmpty())
        assertEquals(null, affiliateEducationSeeAllViewModel.hasMoreData().value)
        assertFalse(affiliateEducationSeeAllViewModel.getTotalCount().value.isMoreThanZero())
    }

    @Test
    fun `should fetch category list`() {
        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 382,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )

        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        val educationArticleResponse: AffiliateEducationArticleCardsResponse = spyk()
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
        } returns educationArticleResponse
        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_EVENT, "")
        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
    }

    @Test
    fun `reset list and should fetch category list`() {
        val eduCategoryChild =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
        val eduCategory =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = 382,
                children = listOf(eduCategoryChild)
            )
        val eduTreeResponse = AffiliateEducationCategoryResponse(
            AffiliateEducationCategoryResponse.CategoryTree(
                AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                    listOf(eduCategory)
                )
            )
        )

        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns eduTreeResponse

        affiliateEducationSeeAllViewModel.resetList(PAGE_EDUCATION_EVENT, "")
        val educationArticleResponse: AffiliateEducationArticleCardsResponse = spyk()

        if (affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty()) {
            coEvery {
                educationArticleCardsUseCase.getEducationArticleCards(any(), offset = any())
            } returns educationArticleResponse
        }

        affiliateEducationSeeAllViewModel.fetchSeeAllData(PAGE_EDUCATION_EVENT, "")
        assertFalse(affiliateEducationSeeAllViewModel.getEducationCategoryChip().value.isNullOrEmpty())
    }
}
