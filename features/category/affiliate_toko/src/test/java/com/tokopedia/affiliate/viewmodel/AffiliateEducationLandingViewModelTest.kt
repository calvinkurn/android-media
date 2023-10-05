package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationBannerResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationBannerUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialRVUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationBannerUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateEducationLandingViewModelTest {
    private val educationBannerUseCase: AffiliateEducationBannerUseCase = mockk()
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase = mockk()
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase = mockk()
    private val affiliateGetUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase = mockk()
    private val viewModel: AffiliateEducationLandingViewModel = spyk(
        AffiliateEducationLandingViewModel(
            educationBannerUseCase,
            educationCategoryUseCase,
            educationArticleCardsUseCase,
            affiliateGetUnreadNotificationUseCase
        )
    )
    private val educationLandingDataObserver: Observer<List<Visitable<AffiliateAdapterTypeFactory>>> =
        mockk(relaxed = true)

    companion object {
        private const val TYPE_ARTICLE_TOPIC = 381L
        private const val TYPE_TUTORIAL = 383L
    }

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel.getEducationPageData().observeForever(educationLandingDataObserver)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should fetch data properly`() {
        val bannerItem: AffiliateEducationBannerResponse.DynamicBanner.BannerData.BannersItem =
            mockk(relaxed = true)
        val bannerResponse = AffiliateEducationBannerResponse(
            AffiliateEducationBannerResponse.DynamicBanner(
                AffiliateEducationBannerResponse.DynamicBanner.BannerData(
                    listOf(
                        bannerItem,
                        bannerItem
                    )
                )
            )
        )
        coEvery {
            educationBannerUseCase.getEducationBanners()
        } returns bannerResponse

        val card: AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem =
            mockk(relaxed = true)
        val cardList = listOf(card, card)
        val cardResponse = AffiliateEducationArticleCardsResponse(
            AffiliateEducationArticleCardsResponse.CardsArticle(
                AffiliateEducationArticleCardsResponse.CardsArticle.Data(cardList)
            )
        )
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(
                categoryId = any(),
                limit = any(),
                filter = any()
            )
        } returns cardResponse

        val childrenItem: AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem =
            mockk()
        val articleTopicCategoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = TYPE_ARTICLE_TOPIC,
                children = listOf(childrenItem, childrenItem)
            )
        val tutorialCategoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = TYPE_TUTORIAL,
                children = listOf(childrenItem, childrenItem)
            )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                listOf(articleTopicCategoryItem, tutorialCategoryItem)
            )
        )
        val categoryResponse = AffiliateEducationCategoryResponse(categoryTree)

        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns categoryResponse

        viewModel.getEducationLandingPageData()

        assertEquals(7, viewModel.getEducationPageData().value?.size)
    }

    @Test
    fun `should not contain banner when empty`() {
        val bannerResponse = AffiliateEducationBannerResponse()
        coEvery {
            educationBannerUseCase.getEducationBanners()
        } returns bannerResponse
        val card: AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem =
            mockk(relaxed = true)
        val cardList = listOf(card, card)
        val cardResponse = AffiliateEducationArticleCardsResponse(
            AffiliateEducationArticleCardsResponse.CardsArticle(
                AffiliateEducationArticleCardsResponse.CardsArticle.Data(cardList)
            )
        )
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(
                categoryId = any(),
                limit = any(),
                filter = any()
            )
        } returns cardResponse

        val childrenItem: AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem =
            mockk()
        val articleTopicCategoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = TYPE_ARTICLE_TOPIC,
                children = listOf(childrenItem, childrenItem)
            )
        val tutorialCategoryItem =
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem(
                id = TYPE_TUTORIAL,
                children = listOf(childrenItem, childrenItem)
            )
        val categoryTree = AffiliateEducationCategoryResponse.CategoryTree(
            AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData(
                listOf(articleTopicCategoryItem, tutorialCategoryItem)
            )
        )
        val categoryResponse = AffiliateEducationCategoryResponse(categoryTree)
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns categoryResponse

        viewModel.getEducationLandingPageData()

        assertEquals(6, viewModel.getEducationPageData().value?.size)
        assertEquals(
            false,
            viewModel.getEducationPageData().value?.any { it is AffiliateEducationBannerUiModel }
        )
    }

    @Test
    fun `should not contain article topics and tutorial when empty`() {
        val bannerResponse = mockk<AffiliateEducationBannerResponse>(relaxed = true)
        coEvery {
            educationBannerUseCase.getEducationBanners()
        } returns bannerResponse

        val card: AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem =
            mockk(relaxed = true)
        val cardList = listOf(card, card)

        val cardResponse = AffiliateEducationArticleCardsResponse(
            AffiliateEducationArticleCardsResponse.CardsArticle(
                AffiliateEducationArticleCardsResponse.CardsArticle.Data(cardList)
            )
        )
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(
                categoryId = any(),
                limit = any(),
                filter = any()
            )
        } returns cardResponse

        val categoryTree = mockk<AffiliateEducationCategoryResponse.CategoryTree>(relaxed = true)
        val categoryResponse = AffiliateEducationCategoryResponse(categoryTree)
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns categoryResponse

        viewModel.getEducationLandingPageData()

        assertEquals(5, viewModel.getEducationPageData().value?.size)
        assertEquals(
            false,
            viewModel.getEducationPageData().value?.any { it is AffiliateEducationArticleTopicRVUiModel }
        )
        assertEquals(
            false,
            viewModel.getEducationPageData().value?.any { it is AffiliateEducationTutorialRVUiModel }
        )
    }

    @Test
    fun `should not any category on null category tree`() {
        val bannerResponse = mockk<AffiliateEducationBannerResponse>(relaxed = true)
        coEvery {
            educationBannerUseCase.getEducationBanners()
        } returns bannerResponse

        val card: AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem =
            mockk(relaxed = true)
        val cardList = listOf(card, card)

        val cardResponse = AffiliateEducationArticleCardsResponse(
            AffiliateEducationArticleCardsResponse.CardsArticle(
                AffiliateEducationArticleCardsResponse.CardsArticle.Data(cardList)
            )
        )
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(
                categoryId = any(),
                limit = any(),
                filter = any()
            )
        } returns cardResponse

        val categoryResponse = AffiliateEducationCategoryResponse()
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns categoryResponse

        viewModel.getEducationLandingPageData()

        assertEquals(3, viewModel.getEducationPageData().value?.size)
        assertEquals(
            false,
            viewModel.getEducationPageData().value?.any { it is AffiliateEducationArticleTopicRVUiModel }
        )
        assertEquals(
            false,
            viewModel.getEducationPageData().value?.any { it is AffiliateEducationEventRVUiModel }
        )
        assertEquals(
            false,
            viewModel.getEducationPageData().value?.any { it is AffiliateEducationArticleRVUiModel }
        )
        assertEquals(
            false,
            viewModel.getEducationPageData().value?.any { it is AffiliateEducationTutorialRVUiModel }
        )
    }

    @Test
    fun `successfully getting unread notification count`() {
        coEvery {
            affiliateGetUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        viewModel.fetchUnreadNotificationCount()
        assertEquals(5, viewModel.getUnreadNotificationCount().value)
    }

    @Test
    fun `should reset notification count to zero`() {
        coEvery {
            affiliateGetUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        viewModel.fetchUnreadNotificationCount()
        assertEquals(5, viewModel.getUnreadNotificationCount().value)

        viewModel.resetNotificationCount()
        assertEquals(0, viewModel.getUnreadNotificationCount().value)
    }
}
