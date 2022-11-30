package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.AffiliateAnalytics.ActionKeys
import com.tokopedia.affiliate.AffiliateAnalytics.CategoryKeys
import com.tokopedia.affiliate.FACEBOOK
import com.tokopedia.affiliate.FILTER_HIGHLIGHTED
import com.tokopedia.affiliate.INSTAGRAM
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.SOCIAL_CHANNEL_FOLLOW_COUNT
import com.tokopedia.affiliate.SOCIAL_CHANNEL_HEADER
import com.tokopedia.affiliate.SOCIAL_CHANNEL_LINK
import com.tokopedia.affiliate.TELEGRAM
import com.tokopedia.affiliate.YOUTUBE
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateEducationSocialData
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationBannerResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationBannerUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationLandingViewModel @Inject constructor(
    private val educationBannerUseCase: AffiliateEducationBannerUseCase,
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase,
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase
) : BaseViewModel() {

    companion object {
        private const val TYPE_ARTICLE_TOPIC = 1222
        private const val TYPE_ARTICLE = 1232
        private const val TYPE_EVENT = 1238
        private const val TYPE_TUTORIAL = 1224
    }

    init {
        getEducationLandingPageData()
    }

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val educationPageData = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()

    private fun getEducationLandingPageData() {
        launchCatchError(block = {
            val educationBanners = educationBannerUseCase.getEducationBanners()
            val educationCategories = educationCategoryUseCase.getEducationCategoryTree()
            val educationEventCards = educationArticleCardsUseCase.getEducationArticleCards(
                TYPE_EVENT,
                limit = 4,
                filter = FILTER_HIGHLIGHTED
            )
            val educationArticleCards = educationArticleCardsUseCase.getEducationArticleCards(
                TYPE_ARTICLE,
                limit = 4
            )
            convertToVisitable(
                educationBanners,
                educationCategories,
                educationEventCards,
                educationArticleCards
            )
        }, onError = { Timber.e(it) })
    }

    private fun convertToVisitable(
        educationBannerResponse: AffiliateEducationBannerResponse,
        educationCategoryResponse: AffiliateEducationCategoryResponse,
        educationEventCards: AffiliateEducationArticleCardsResponse,
        educationArticleCards: AffiliateEducationArticleCardsResponse
    ) {
        val tempList = mutableListOf<Visitable<AffiliateAdapterTypeFactory>>()
        educationBannerResponse.dynamicBanner?.let { educationBanners ->
            tempList.add(AffiliateEducationBannerUiModel(educationBanners.data?.banners))
            sendEducationImpressions(
                educationBanners.data?.banners?.get(0)?.title,
                educationBanners.data?.banners?.get(0)?.bannerId.toString(),
                ActionKeys.IMPRESSION_MAIN_BANNER,
                CategoryKeys.AFFILIATE_EDUKASI_PAGE
            )
        }
        educationCategoryResponse.categoryTree?.let { educationCategories ->
            val categoryGroup =
                educationCategories.data?.categories?.groupBy { it?.id?.toInt().orZero() }
            val articleTopics = categoryGroup?.get(TYPE_ARTICLE_TOPIC)?.getOrNull(0)?.children
            val tutorial =
                categoryGroup?.get(TYPE_TUTORIAL)?.getOrNull(0)?.children?.toMutableList()
            if (articleTopics?.isNotEmpty() == true) {
                tempList.add(AffiliateEducationArticleTopicRVUiModel(articleTopics))
            }
            educationEventCards.cardsArticle?.data?.cards?.let {
                tempList.add(AffiliateEducationEventRVUiModel(it[0]))
                sendEducationImpressions(
                    it[0]?.title,
                    it[0]?.id,
                    ActionKeys.IMPRESSION_EVENT_CARD,
                    CategoryKeys.AFFILIATE_EDUKASI_PAGE
                )
            }
            educationArticleCards.cardsArticle?.data?.cards?.let {
                tempList.add(AffiliateEducationArticleRVUiModel(it[0]))
                sendEducationImpressions(
                    it[0]?.title,
                    it[0]?.id,
                    ActionKeys.IMPRESSION_LATEST_ARTICLE_CARD,
                    CategoryKeys.AFFILIATE_EDUKASI_PAGE
                )
            }
            if (tutorial?.isNotEmpty() == true) {
                tutorial.add(
                    PAGE_ZERO,
                    AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem()
                )
                tempList.add(AffiliateEducationTutorialRVUiModel(tutorial))
            }
        }
        tempList.add(
            AffiliateEducationSocialRVUiModel(
                listOf(
                    AffiliateEducationSocialData(
                        socialChannel = INSTAGRAM,
                        followCount = SOCIAL_CHANNEL_FOLLOW_COUNT[INSTAGRAM],
                        headerImage = SOCIAL_CHANNEL_HEADER[INSTAGRAM],
                        icon = IconUnify.INSTAGRAM,
                        url = SOCIAL_CHANNEL_LINK[INSTAGRAM]
                    ),
                    AffiliateEducationSocialData(
                        socialChannel = FACEBOOK,
                        followCount = SOCIAL_CHANNEL_FOLLOW_COUNT[FACEBOOK],
                        headerImage = SOCIAL_CHANNEL_HEADER[FACEBOOK],
                        icon = IconUnify.FACEBOOK,
                        url = SOCIAL_CHANNEL_LINK[FACEBOOK]
                    ),
                    AffiliateEducationSocialData(
                        socialChannel = TELEGRAM,
                        followCount = SOCIAL_CHANNEL_FOLLOW_COUNT[TELEGRAM],
                        headerImage = SOCIAL_CHANNEL_HEADER[TELEGRAM],
                        icon = IconUnify.TELEGRAM,
                        url = SOCIAL_CHANNEL_LINK[TELEGRAM]
                    ),
                    AffiliateEducationSocialData(
                        socialChannel = YOUTUBE,
                        followCount = SOCIAL_CHANNEL_FOLLOW_COUNT[YOUTUBE],
                        headerImage = SOCIAL_CHANNEL_HEADER[YOUTUBE],
                        icon = IconUnify.YOUTUBE,
                        url = SOCIAL_CHANNEL_LINK[YOUTUBE]
                    )
                )
            )
        )
        tempList.add(AffiliateEducationLearnUiModel())
        educationPageData.value = tempList
    }

    fun getEducationPageData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationPageData

    private fun sendEducationImpressions(
        creativeName: String?,
        id: String?,
        actionKeys: String,
        categoryKeys: String
    ) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.VIEW_ITEM,
            actionKeys,
            categoryKeys,
            id,
            position = 0,
            id,
            userSessionInterface.userId,
            creativeName
        )
    }
}
