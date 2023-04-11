package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationBannerResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationBannerUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
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
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateEducationLandingViewModelTest {
    private val educationBannerUseCase: AffiliateEducationBannerUseCase = mockk()
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase = mockk()
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase = mockk()
    private val viewModel: AffiliateEducationLandingViewModel = spyk(
        AffiliateEducationLandingViewModel(
            educationBannerUseCase,
            educationCategoryUseCase,
            educationArticleCardsUseCase
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
    fun `should return data properly`() {
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

        val card: AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem =
            mockk(relaxed = true)
        val cardList = listOf(card, card)
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
        val cardResponse = AffiliateEducationArticleCardsResponse(
            AffiliateEducationArticleCardsResponse.CardsArticle(
                AffiliateEducationArticleCardsResponse.CardsArticle.Data(cardList)
            )
        )
        val categoryResponse = AffiliateEducationCategoryResponse(categoryTree)
        coEvery {
            educationBannerUseCase.getEducationBanners()
        } returns bannerResponse
        coEvery {
            educationArticleCardsUseCase.getEducationArticleCards(
                categoryId = any(),
                limit = any(),
                filter = any()
            )
        } returns cardResponse
        coEvery {
            educationCategoryUseCase.getEducationCategoryTree()
        } returns categoryResponse
        viewModel.getEducationLandingPageData()
        assertNotNull(viewModel.getEducationPageData().value)
    }
}
