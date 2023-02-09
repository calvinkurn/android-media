package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationSearchArticleCardsResponse
import com.tokopedia.affiliate.ui.fragment.AffiliateEducationSearchArticleFragment.Companion.ARTICLE
import com.tokopedia.affiliate.ui.fragment.AffiliateEducationSearchArticleFragment.Companion.EVENT
import com.tokopedia.affiliate.ui.fragment.AffiliateEducationSearchArticleFragment.Companion.TUTORIAL
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationSearchResultUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.url.TokopediaUrl
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationSearchArticleViewModel @Inject constructor(
    private val educationSearchResultUseCase: AffiliateEducationSearchResultUseCase,
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase,
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase
) : BaseViewModel() {

    companion object {
        private val isStaging = TokopediaUrl.getInstance().GQL.contains("staging")
        private val TYPE_ARTICLE_L1 = if (isStaging) 1222 else 381
        private val TYPE_EVENT_L1 = if (isStaging) 1223 else 382
        private val TYPE_TUTORIAL_L1 = if (isStaging) 1224 else 383
    }

    private val totalCount = MutableLiveData<Int>()
    private val educationCategoryChip =
        MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val educationSearchPageData =
        MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private var offset: Int? = 0
    private var categoryId: Long? = 0
    private var type: Int = 0
    private var categoryToArticleCardMap =
        mutableMapOf<String, AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data?>()
    var currentCategoryId: String? = null
    private var highlightedArticleCardMap =
        mutableMapOf<Int, AffiliateEducationArticleCardsResponse>()

    fun fetchSearchData(pageType: String?, keyword: String?, categoryID: String? = null) {
        launchCatchError(block = {
            if (educationCategoryChip.value.isNullOrEmpty()) {
                loadCategory(pageType)
            }
            val key = buildString {
                append(categoryID ?: categoryId.toString()).append("_").append(keyword)
            }

            if (categoryToArticleCardMap.containsKey(key)) {
                setVisitable(categoryToArticleCardMap[key], pageType)
            } else {
                val educationSearchArticleCards =
                    educationSearchResultUseCase.getEducationSearchResultCards(
                        limit = 10,
                        offset,
                        keyword,
                        categoryID?.toLongOrNull() ?: categoryId
                    )
                categoryToArticleCardMap[key] = educationSearchArticleCards.searchEducation?.data
                setVisitable(educationSearchArticleCards.searchEducation?.data, pageType)
            }

        }, onError = { Timber.e(it) })
    }

    private fun setVisitable(
        data: AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data?,
        pageType: String?,
    ) {
        launchCatchError(block = {
            val totalItems = data?.results?.getOrNull(0)?.section?.filter { it?.id == "articles" }
                ?.getOrNull(0)?.meta?.totalHits
            if (totalItems.isZero()) {
                val educationArticleCards = if (highlightedArticleCardMap.containsKey(type)) {
                    highlightedArticleCardMap[type]
                } else {
                    educationArticleCardsUseCase.getEducationArticleCards(
                        type,
                        offset = offset ?: 0,
                        filter = "highlights"
                    ).also { highlightedArticleCardMap[type] = it }
                }
                totalCount.value = 0
                educationArticleCards?.let { convertToVisitableArticleCard(it, pageType) }
            } else {
                convertToVisitableSearchResult(data, pageType)
            }
        }, onError = { Timber.e(it) })
    }


    private fun convertToVisitableArticleCard(
        educationArticleCards: AffiliateEducationArticleCardsResponse,
        pageType: String?
    ) {
        val tempList = mutableListOf<Visitable<AffiliateAdapterTypeFactory>>()
        educationArticleCards.cardsArticle?.data?.cards?.let {
            if (it.isNotEmpty()) {
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
            educationSearchPageData.value = tempList
        }
    }

    private fun convertToVisitableSearchResult(
        educationArticleCards: AffiliateEducationSearchArticleCardsResponse.SearchEducation.Data?,
        pageType: String?
    ) {
        val tempList = mutableListOf<Visitable<AffiliateAdapterTypeFactory>>()
        val resultCards =
            educationArticleCards?.results?.getOrNull(0)?.section?.filter { it?.id == "articles" }
                ?.getOrNull(0)
        val articleCards = resultCards?.items?.mapNotNull {
            AffiliateEducationSeeAllUiModel(
                AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem.Article(
                    title = it?.title,
                    description = it?.description,
                    attributes = AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem.Article.Attributes(
                        it?.attributes?.readTime
                    ),
                    thumbnail = AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem.Article.Thumbnail(
                        android = it?.thumbnail?.android
                    ),
                    modifiedDate = it?.modifiedDate,
                    publishTime = it?.publishTime,
                    slug = it?.url,
                    categories = listOf(
                        AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem.Article.CategoriesItem(
                            level = it?.categories?.get(0)?.level,
                            id = it?.categories?.get(0)?.id, title = it?.categories?.get(0)?.title
                        )
                    )
                ),
                pageType.orEmpty()
            )
        }
        tempList.addAll(
            articleCards.orEmpty()
        )
        offset = resultCards?.meta?.offset
        totalCount.value = resultCards?.meta?.totalHits.orZero()
        educationSearchPageData.value = tempList
    }

    private suspend fun loadCategory(pageType: String?) {
        educationCategoryUseCase.getEducationCategoryTree().categoryTree.let { response ->
            response?.data?.categories?.let { educationCategories ->
                if (educationCategories.isNotEmpty()) {
                    val categoryGroup = educationCategories.groupBy { it?.id?.toInt().orZero() }
                    type = when (pageType) {
                        EVENT -> TYPE_EVENT_L1
                        ARTICLE -> TYPE_ARTICLE_L1
                        TUTORIAL -> TYPE_TUTORIAL_L1
                        else -> 0
                    }
                    educationCategoryChip.value =
                        categoryGroup[type]?.getOrNull(0)?.children?.mapNotNull { category ->
                            if (categoryId?.toInt() == 0) {
                                AffiliateEduCategoryChipModel(
                                    category.apply {
                                        this?.isSelected = true;
                                        categoryId = this?.id
                                    }
                                )
                            } else {
                                AffiliateEduCategoryChipModel(category)
                            }
                        }
                }
            }
        }

    }

    fun getTotalCount(): LiveData<Int> = totalCount
    fun getEducationSearchData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationSearchPageData

    fun getEducationCategoryChip(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationCategoryChip

    fun resetList(pageType: String?, keyword: String?, categoryID: String?) {
        offset = 0
        categoryID?.let { currentCategoryId = categoryID }
        fetchSearchData(pageType, keyword, currentCategoryId)
    }

}
