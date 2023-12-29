package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationSearchArticleCardsResponse
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationSearchResultUseCase
import com.tokopedia.kotlin.extensions.view.isZero
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
    fun `Education Category Chip Success`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val childrenItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 10
            )
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = listOf(childrenItem)
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = listOf(categoryItem)
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("", "", "")

        assertNotNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on null data`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = null
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on null categories`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(null, null)
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on null categoryItem`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = null
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            listOf(categoryItem),
            null
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip Success on pageType Event`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val childrenItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 10
            )
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = listOf(childrenItem),
                id = 382
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = listOf(categoryItem)
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("event", "", "")

        assertNotNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType Event with null data`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = null
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("event", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType Event with null categoryTreeData`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = null
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("event", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType Event with null categoryItem`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = null,
                id = null
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = listOf(categoryItem)
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("event", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip Success on pageType Article`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val childrenItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 10
            )
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = listOf(childrenItem),
                id = 381
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = listOf(categoryItem)
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("article", "", "")

        assertNotNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType article with null data`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = null
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("article", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType article with null categoryTreeData`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = null
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("article", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType article with null categoryItem`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = null,
                id = null
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = listOf(categoryItem)
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("article", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip Success on pageType Tutorial`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val childrenItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                id = 10
            )
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = listOf(childrenItem),
                id = 383
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = listOf(categoryItem)
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("tutorial", "", "")

        assertNotNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType tutorial with null data`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = null
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("tutorial", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType tutorial with null categoryTreeData`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = null
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("tutorial", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Education Category Chip on pageType tutorial with null categoryItem`() {
        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        val categoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                children = null,
                id = null
            )
        val categoryTreeData = AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
            categories = listOf(categoryItem)
        )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            data = categoryTreeData
        )
        coEvery { affiliateEducationCategoryResponse.categoryTree } returns categoryTree
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("tutorial", "", "")

        assertNull(affiliateEducationSearchArticleViewModel.getEducationCategoryChip().value)
    }

    @Test
    fun `Search Card Reset List`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        coEvery { educationSearchResultUseCase.getEducationSearchResultCards(any()) } returns affiliateEducationSearchArticleCardsResponse

        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.resetList("articles", "", "")

        assertNotNull(affiliateEducationSearchArticleViewModel.getEducationSearchData().value)
        assertEquals(affiliateEducationSearchArticleViewModel.progressBar().value, false)
    }

    @Test
    fun `Search Article Card Success`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val metaData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Meta(
                totalHits = 1
            )
        val categoryItem =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Items.CategoriesItem(
                level = 0,
                id = 0,
                title = ""
            )
        val articleCardData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Items(
                title = "article1",
                description = "articleDescription",
                modifiedDate = "",
                publishTime = "",
                url = "",
                categories = listOf(categoryItem)
            )
        val sectionData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem(
                id = "articles",
                meta = metaData,
                items = listOf(articleCardData)
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

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertNotNull(affiliateEducationSearchArticleViewModel.getEducationSearchData().value)
        assertEquals(affiliateEducationSearchArticleViewModel.progressBar().value, false)
    }

    @Test
    fun `Search Article Card with null data`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val data = AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data(
            results = null
        )
        val searchData = AffiliateEducationSearchArticleCardsResponse.SearchEducation(
            data = data
        )
        coEvery { affiliateEducationSearchArticleCardsResponse.searchEducation } returns searchData
        coEvery { educationSearchResultUseCase.getEducationSearchResultCards(any()) } returns affiliateEducationSearchArticleCardsResponse

        val affiliateEducationCategoryResponse: AffiliateEducationCategoryResponse =
            mockk(relaxed = true)
        coEvery { educationCategoryUseCase.getEducationCategoryTree() } returns affiliateEducationCategoryResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertTrue(affiliateEducationSearchArticleViewModel.getEducationSearchData().value?.size.isZero())
    }

    @Test
    fun `Search Article Card with null resultsItem`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val resultsItem =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem(
                section = null
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

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertTrue(affiliateEducationSearchArticleViewModel.getEducationSearchData().value?.size.isZero())
    }

    @Test
    fun `Search Article Card with null sectionData`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val sectionData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem(
                id = null,
                meta = null,
                items = null
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

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertTrue(affiliateEducationSearchArticleViewModel.getEducationSearchData().value?.size.isZero())
    }

    @Test
    fun `Search Article Card with null articleCardData`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val articleCardData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Items()
        val sectionData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem(
                id = null,
                meta = null,
                items = listOf(articleCardData)
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

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertTrue(affiliateEducationSearchArticleViewModel.getEducationSearchData().value?.size.isZero())
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
        val articleData =
            AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem.Article(
                title = "article1",
                description = "articleDescription",
                articleId = 1
            )
        val cardItemData = AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem(
            totalCount = 1,
            actionLink = "",
            offset = 0,
            appActionLink = "",
            id = "1",
            articles = listOf(articleData)
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
    fun `latest Article Card with null cardsArticleData`() {
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
        val cardsArticleData = AffiliateEducationArticleCardsResponse.CardsArticle(
            data = null
        )
        coEvery { affiliateEducationArticleCardsResponse.cardsArticle } returns cardsArticleData
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(
                any(),
                filter = "latest"
            )
        } returns affiliateEducationArticleCardsResponse

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertTrue(affiliateEducationSearchArticleViewModel.getLatestCardCount().value.isZero())
    }

    @Test
    fun `latest Article Card with null cardItemData`() {
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
            articles = null
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

        assertTrue(affiliateEducationSearchArticleViewModel.getLatestCardCount().value.isZero())
    }

    @Test
    fun `latest Article Card with null cardData`() {
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
        val cardData = AffiliateEducationArticleCardsResponse.CardsArticle.Data(
            cards = null
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

        assertTrue(affiliateEducationSearchArticleViewModel.getLatestCardCount().value.isZero())
    }

    @Test
    fun `latest article card with no data`() {
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
            totalCount = 0,
            actionLink = "",
            offset = 0
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

        assertEquals(affiliateEducationSearchArticleViewModel.getLatestCardCount().value, 0)
    }

    @Test
    fun `Search Article Card with different section id`() {
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val metaData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Meta(
                totalHits = 1
            )
        val sectionData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem(
                id = "SectionId",
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

        affiliateEducationSearchArticleViewModel.fetchSearchData("articles", "", "")

        assertEquals(affiliateEducationSearchArticleViewModel.getTotalCount().value, 0)
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

    @Test
    fun `Education Article card exception`() {
        val exception = "validate Data Exception"
        val affiliateEducationSearchArticleCardsResponse: AffiliateEducationSearchArticleCardsResponse =
            mockk(relaxed = true)
        val metaData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Meta(
                totalHits = 0
            )
        val categoryItem =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Items.CategoriesItem(
                level = 0,
                id = 0,
                title = ""
            )
        val articleCardData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem.Items(
                title = "article1",
                description = "articleDescription",
                modifiedDate = "",
                publishTime = "",
                url = "",
                categories = listOf(categoryItem)
            )
        val sectionData =
            AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data.ResultsItem.SectionItem(
                id = "articles",
                meta = metaData,
                items = listOf(articleCardData)
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

        coEvery { educationArticleCardsUseCase.getEducationArticleCards(any()) } throws Exception(
            exception
        )

        affiliateEducationSearchArticleViewModel.fetchSearchData("", "", "")
        assertEquals(affiliateEducationSearchArticleViewModel.getErrorMessage().value, exception)
    }
}
