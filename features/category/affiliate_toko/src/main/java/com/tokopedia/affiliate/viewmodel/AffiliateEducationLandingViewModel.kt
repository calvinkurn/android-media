package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
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
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationBannerUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationLearnUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSocialRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationTutorialRVUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationBannerUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationLandingViewModel @Inject constructor(
    private val educationBannerUseCase: AffiliateEducationBannerUseCase,
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase,
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase,
    private val affiliateUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase
) : BaseViewModel() {
    private val _unreadNotificationCount = MutableLiveData(Int.ZERO)
    fun getUnreadNotificationCount(): LiveData<Int> = _unreadNotificationCount
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        Timber.e(e)
    }

    companion object {
        private val isStaging = TokopediaUrl.getInstance().TYPE == Env.STAGING
        private val TYPE_ARTICLE = if (isStaging) 1222 else 381
        private val TYPE_EVENT = if (isStaging) 1223 else 382
        private val TYPE_TUTORIAL = if (isStaging) 1224 else 383
    }

    private val educationPageData = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val socialList = listOf(
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

    fun getEducationLandingPageData() {
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            val tempList = mutableListOf<Visitable<AffiliateAdapterTypeFactory>>()
            val educationBanners = async { educationBannerUseCase.getEducationBanners() }
            val educationCategories = async { educationCategoryUseCase.getEducationCategoryTree() }
            val educationEventCards = async {
                educationArticleCardsUseCase.getEducationArticleCards(
                    TYPE_EVENT,
                    limit = 3,
                    filter = FILTER_HIGHLIGHTED
                )
            }
            val educationArticleCards = async {
                educationArticleCardsUseCase.getEducationArticleCards(
                    TYPE_ARTICLE,
                    limit = 4
                )
            }
            educationBanners.await().dynamicBanner?.let {
                tempList.add(AffiliateEducationBannerUiModel(it.data?.banners))
            }
            educationCategories.await().categoryTree?.let { educationCategoryResponse ->
                val categoryGroup =
                    educationCategoryResponse.data?.categories?.groupBy {
                        it?.id?.toInt().orZero()
                    }
                val articleTopics =
                    categoryGroup?.get(TYPE_ARTICLE)?.firstOrNull()?.children
                val tutorial =
                    categoryGroup?.get(TYPE_TUTORIAL)?.firstOrNull()?.children?.toMutableList()
                if (articleTopics?.isNotEmpty() == true) {
                    tempList.add(AffiliateEducationArticleTopicRVUiModel(articleTopics))
                }
                educationEventCards.await().cardsArticle?.data?.cards?.let {
                    tempList.add(AffiliateEducationEventRVUiModel(it[0]))
                }
                educationArticleCards.await().cardsArticle?.data?.cards?.let {
                    tempList.add(AffiliateEducationArticleRVUiModel(it[0]))
                }
                if (tutorial?.isNotEmpty() == true) {
                    tutorial.add(
                        PAGE_ZERO,
                        AffiliateEducationCategoryResponse.CategoryTree
                            .CategoryTreeData.CategoriesItem.ChildrenItem()
                    )
                    tempList.add(AffiliateEducationTutorialRVUiModel(tutorial))
                }
            }
            tempList.add(AffiliateEducationSocialRVUiModel(socialList))
            tempList.add(AffiliateEducationLearnUiModel())
            educationPageData.value = tempList
        }
    }

    fun fetchUnreadNotificationCount() {
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            _unreadNotificationCount.value =
                affiliateUnreadNotificationUseCase.getUnreadNotifications()
        }
    }

    fun resetNotificationCount() {
        _unreadNotificationCount.value = Int.ZERO
    }

    fun getEducationPageData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationPageData
}
