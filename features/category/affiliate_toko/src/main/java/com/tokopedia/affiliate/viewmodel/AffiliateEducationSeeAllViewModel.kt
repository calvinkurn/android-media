package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE_TOPIC
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.PAGE_EDUCATION_TUTORIAL
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationSearchResultUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationSeeAllViewModel @Inject constructor(
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase,
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase,
    private val educationSearchResultUseCase: AffiliateEducationSearchResultUseCase,
) : BaseViewModel() {
    companion object {
        private val isStaging = TokopediaUrl.getInstance().GQL.contains("staging")
        private val TYPE_ARTICLE_TOPIC = if (isStaging) 1222 else 381
        private val TYPE_EVENT_L1 = if (isStaging) 1223 else 382
        private val TYPE_TUTORIAL = if (isStaging) 1224 else 383
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
                loadCategory(pageType, categoryID)
            }
            convertToVisitable(educationArticleCards, pageType)
        }, onError = { Timber.e(it) })
    }

    fun fetchSearchData(pageType: String?, keyword: String?){
        launchCatchError(block = {
            val educationArticleCards =
                educationSearchResultUseCase.getEducationSearchResultCards(

                )
            convertToVisitable(educationArticleCards, pageType)
        }, onError = { Timber.e(it) })
    }

    fun resetList(pageType: String?, categoryID: String?) {
        offset = 0
        fetchSeeAllData(pageType, categoryID)
    }

    private suspend fun loadCategory(pageType: String?, categoryID: String?) {
        educationCategoryUseCase.getEducationCategoryTree().categoryTree.let { response ->
            response?.data?.categories?.let { educationCategories ->
                if (educationCategories.isNotEmpty()) {
                    val categoryGroup = educationCategories.groupBy { it?.id?.toInt().orZero() }
                    val type = when (pageType) {
                        PAGE_EDUCATION_EVENT -> TYPE_EVENT_L1
                        PAGE_EDUCATION_ARTICLE, PAGE_EDUCATION_ARTICLE_TOPIC -> TYPE_ARTICLE_TOPIC
                        PAGE_EDUCATION_TUTORIAL -> TYPE_TUTORIAL
                        else -> null
                    }
                    educationCategoryChip.value =
                        categoryGroup[type]?.getOrNull(0)?.children?.mapNotNull { category ->
                            AffiliateEduCategoryChipModel(
                                category.apply {
                                    this?.isSelected = this?.id.toString() == categoryID
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
                hasMoreData.value = it[0]?.hasMore.orFalse()
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
