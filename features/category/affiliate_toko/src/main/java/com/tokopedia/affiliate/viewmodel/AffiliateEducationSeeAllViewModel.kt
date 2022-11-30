package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE_TOPIC
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.PAGE_EDUCATION_TUTORIAL
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationSeeAllViewModel @Inject constructor(
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase
) : BaseViewModel() {
    companion object {
        private const val TYPE_ARTICLE_TOPIC = 1222
        private const val TYPE_ARTICLE = 1232
        private const val TYPE_EVENT = 1238
        private const val TYPE_TUTORIAL = 1224
    }

    private var offset: Int = 0

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val educationPageData = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val educationCategoryChip =
        MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val totalCount = MutableLiveData<Int>()
    private val hasMoreData = MutableLiveData<Boolean>()

    fun fetchSeeAllData(pageType: String?, categoryID: String?) {
        launchCatchError(block = {
            val educationArticleCards =
                educationArticleCardsUseCase.getEducationArticleCards(
                    categoryID.toIntOrZero(),
                    offset = offset
                )
            if (educationCategoryChip.value.isNullOrEmpty()) {
                loadCategory(pageType)
            }
            convertToVisitable(educationArticleCards, pageType)

        }, onError = { Timber.e(it) })
    }

    fun resetList(pageType: String?, categoryID: String?) {
        offset = 0
        fetchSeeAllData(pageType, categoryID)
    }

    private suspend fun loadCategory(pageType: String?) {
        val categoryID = when (pageType) {
            PAGE_EDUCATION_EVENT -> TYPE_EVENT
            PAGE_EDUCATION_ARTICLE -> TYPE_ARTICLE
            PAGE_EDUCATION_ARTICLE_TOPIC -> TYPE_ARTICLE_TOPIC
            PAGE_EDUCATION_TUTORIAL -> TYPE_TUTORIAL
            else -> PAGE_ZERO
        }
        educationArticleCardsUseCase.getEducationArticleCards(categoryID).let { response ->
            response.cardsArticle?.data?.cards?.let {
                if (it.isNotEmpty()) {
                    val categories = it[0]?.articles?.mapNotNull { data ->
                        data?.categories
                    }
                    educationCategoryChip.value =
                        categories?.flatten()
                            ?.distinctBy { cat -> cat?.id }
                            ?.mapIndexedNotNull { index, categoriesItem ->
                                AffiliateEduCategoryChipModel(
                                    categoriesItem.apply {
                                        this?.isSelected = index == PAGE_ZERO
                                    }
                                )
                            }
                }
            }
        }
    }

    private fun convertToVisitable(
        educationArticleCards: AffiliateEducationArticleCardsResponse,
        pageType: String?
    ) {
        val tempList = mutableListOf<Visitable<AffiliateAdapterTypeFactory>>()
        educationArticleCards.cardsArticle?.data?.cards?.let {
            if (it.isNotEmpty()) {
                hasMoreData.value = it[0]?.hasMore
                totalCount.value = it[0]?.totalCount.orZero()
                offset = it[0]?.offset.orZero()
                it[0]?.articles?.mapNotNull { data ->
                    tempList.add(
                        AffiliateEducationSeeAllUiModel(
                            data,
                            pageType.orEmpty()
                        )
                    )
                }
                offset = it[0]?.offset.orZero() + tempList.size
            }
            when (pageType) {
                PAGE_EDUCATION_EVENT -> sendEducationImpressions(
                    it[0]?.articles?.get(0)?.title,
                    it[0]?.articles?.get(0)?.articleId.toString(),
                    AffiliateAnalytics.ActionKeys.IMPRESSION_EVENT_CARD,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_EVENT
                )
                PAGE_EDUCATION_ARTICLE -> sendEducationImpressions(
                    it[0]?.articles?.get(0)?.title,
                    it[0]?.articles?.get(0)?.articleId.toString(),
                    AffiliateAnalytics.ActionKeys.IMPRESSION_ARTICLE_CARD,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_ARTICLE
                )
                PAGE_EDUCATION_TUTORIAL -> sendEducationImpressions(
                    it[0]?.articles?.get(0)?.title,
                    it[0]?.articles?.get(0)?.articleId.toString(),
                    AffiliateAnalytics.ActionKeys.IMPRESSION_TUTORIAL_CARD,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_CATEGORY_LANDING_TUTORIAL
                )
                PAGE_EDUCATION_ARTICLE_TOPIC -> sendEducationImpressions(
                    it[0]?.articles?.get(0)?.title,
                    it[0]?.articles?.get(0)?.articleId.toString(),
                    AffiliateAnalytics.ActionKeys.IMPRESSION_ARTICLE_CATEGORY,
                    AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE
                )
            }
        }
        educationPageData.value = tempList
    }

    fun getEducationSeeAllData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationPageData

    fun getEducationCategoryChip(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationCategoryChip

    fun getTotalCount(): LiveData<Int> = totalCount
    fun hasMoreData(): LiveData<Boolean> = hasMoreData

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
