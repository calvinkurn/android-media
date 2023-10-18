package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE
import com.tokopedia.affiliate.PAGE_EDUCATION_ARTICLE_TOPIC
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.PAGE_EDUCATION_TUTORIAL
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEduCategoryChipModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationSeeAllViewModel @Inject constructor(
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase,
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase
) : BaseViewModel() {
    companion object {
        private val isStaging = TokopediaUrl.getInstance().GQL.contains("staging")
        private val TYPE_ARTICLE_TOPIC = if (isStaging) 1222 else 381
        private val TYPE_EVENT_L1 = if (isStaging) 1223 else 382
        private val TYPE_TUTORIAL = if (isStaging) 1224 else 383
    }

    private var offset: Int = 0

    private val educationPageData = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val educationCategoryChip =
        MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val totalCount = MutableLiveData<Int>()
    private val hasMoreData = MutableLiveData<Boolean>()
    private var categoryId: String? = null

    fun fetchSeeAllData(pageType: String?, categoryID: String?) {
        viewModelScope.launch {
            try {
                categoryId = categoryID
                if (educationCategoryChip.value.isNullOrEmpty()) {
                    loadCategory(pageType)
                }
                val educationArticleCards =
                    educationArticleCardsUseCase.getEducationArticleCards(
                        categoryId.toIntOrZero(),
                        offset = offset
                    )
                educationArticleCards.cardsArticle?.data?.cards?.let {
                    if (it.isNotEmpty()) {
                        hasMoreData.value = it[0]?.hasMore.orFalse()
                        totalCount.value = it[0]?.totalCount.orZero()
                        offset = it[0]?.offset.orZero()
                        educationPageData.value = it[0]?.articles?.mapNotNull { data ->
                            AffiliateEducationSeeAllUiModel(
                                data,
                                pageType.orEmpty()
                            )
                        }.also { seeAllList ->
                            offset = it[0]?.offset.orZero() + seeAllList?.size.orZero()
                        }
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun resetList(pageType: String?, categoryID: String?) {
        offset = 0
        fetchSeeAllData(pageType, categoryID)
    }

    private suspend fun loadCategory(pageType: String?) {
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
                            if (categoryId.isNullOrEmpty()) {
                                AffiliateEduCategoryChipModel(
                                    category.apply {
                                        this?.isSelected = true
                                        categoryId = this?.id.toString()
                                    }
                                )
                            } else {
                                AffiliateEduCategoryChipModel(
                                    category.apply {
                                        this?.isSelected = this?.id.toString() == categoryId
                                    }
                                )
                            }
                        }
                }
            }
        }
    }

    fun getEducationSeeAllData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationPageData

    fun getEducationCategoryChip(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationCategoryChip

    fun getTotalCount(): LiveData<Int> = totalCount
    fun hasMoreData(): LiveData<Boolean> = hasMoreData
}
