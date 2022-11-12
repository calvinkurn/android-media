package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateEducationSocialData
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationBannerResponse
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
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationSeeAllViewModel @Inject constructor(
    private val educationBannerUseCase: AffiliateEducationBannerUseCase,
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase,
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase
) : BaseViewModel() {

    companion object {
        private const val TYPE_ARTICLE = 1222L
        private const val TYPE_EVENT = 1223L
        private const val TYPE_TUTORIAL = 1224L
    }

    init {
        getEducationLandingPageData()
    }

    private val educationPageData = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()

    private fun getEducationLandingPageData() {
        launchCatchError(block = {
            val educationBanners = educationBannerUseCase.getEducationBanners()
            val educationCategories = educationCategoryUseCase.getEducationCategoryTree()
            val educationArticleCards = educationArticleCardsUseCase.getEducationArticleCards(0, limit = 4)
            convertToVisitable(educationBanners, educationCategories, educationArticleCards)
        }, onError = { Timber.e(it) })
    }

    private fun convertToVisitable(
        educationBannerResponse: AffiliateEducationBannerResponse,
        educationCategoryResponse: AffiliateEducationCategoryResponse,
        educationArticleCards: AffiliateEducationArticleCardsResponse
    ) {
        val tempList = mutableListOf<Visitable<AffiliateAdapterTypeFactory>>()
        educationBannerResponse.dynamicBanner?.let { educationBanners ->
            tempList.add(AffiliateEducationBannerUiModel(educationBanners.data?.banners))
        }
        educationCategoryResponse.categoryTree?.let { educationCategories ->
            val categoryGroup =
                educationCategories.data?.categories?.groupBy { it?.id.orZero() }
            val articleTopics = categoryGroup?.get(TYPE_ARTICLE)?.get(0)?.children
            val tutorial = categoryGroup?.get(TYPE_TUTORIAL)?.get(0)?.children?.toMutableList()
            if (articleTopics?.isNotEmpty() == true) {
                tempList.add(
                    AffiliateEducationArticleTopicRVUiModel(articleTopics)
                )
            }
            educationArticleCards.cardsArticle?.data?.cards?.let {
                tempList.add(AffiliateEducationEventRVUiModel(it[0]?.articles))
                tempList.add(AffiliateEducationArticleRVUiModel(it[0]?.articles))
            }
            if (tutorial?.isNotEmpty() == true) {
                tutorial.add(
                    0,
                    AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem(
                        title = "Yuk pelajari tutorial biar jago promosi!"
                    )
                )
                tempList.add(
                    AffiliateEducationTutorialRVUiModel(tutorial)
                )
            }
        }
        tempList.add(
            AffiliateEducationSocialRVUiModel(
                listOf(
                    AffiliateEducationSocialData(
                        socialChannel = "Instagram",
                        followCount = "12.3k Followers",
                        headerImage = "https://images.unsplash.com/photo-1519638399535-1b036603ac77?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8YW5pbWV8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60",
                        icon = IconUnify.INSTAGRAM
                    ),
                    AffiliateEducationSocialData(
                        socialChannel = "Facebook",
                        followCount = "12.3k Followers",
                        headerImage = "https://images.unsplash.com/photo-1519638399535-1b036603ac77?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8YW5pbWV8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60",
                        icon = IconUnify.FACEBOOK
                    ),
                    AffiliateEducationSocialData(
                        socialChannel = "Telegram",
                        followCount = "12.3k Followers",
                        headerImage = "https://images.unsplash.com/photo-1519638399535-1b036603ac77?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8YW5pbWV8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60",
                        icon = IconUnify.TELEGRAM
                    ),
                    AffiliateEducationSocialData(
                        socialChannel = "Youtube",
                        followCount = "12.3k Followers",
                        headerImage = "https://images.unsplash.com/photo-1519638399535-1b036603ac77?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8YW5pbWV8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60",
                        icon = IconUnify.YOUTUBE
                    )
                )
            )
        )
        tempList.add(AffiliateEducationLearnUiModel())

        educationPageData.value = tempList
    }

    fun getEducationPageData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationPageData

}
