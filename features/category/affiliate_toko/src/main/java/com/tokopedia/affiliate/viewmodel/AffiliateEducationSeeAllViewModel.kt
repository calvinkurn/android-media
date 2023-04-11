package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSeeAllUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationArticleCardsUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationSeeAllViewModel @Inject constructor(
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase,
    private val educationArticleCardsUseCase: AffiliateEducationArticleCardsUseCase
) : BaseViewModel() {
    private var offset: Int = 0

    private val educationPageData = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val educationCategoryChip =
        MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val totalCount = MutableLiveData<Int>()
    private val hasMoreData = MutableLiveData<Boolean>()

    fun fetchSeeAllData(pageType: String?, categoryID: String?) {
        viewModelScope.launch {
            try {
                if (educationCategoryChip.value.isNullOrEmpty()) {
                    educationCategoryUseCase.getEducationFilterChips(pageType, categoryID)?.let {
                        educationCategoryChip.value = it
                    }
                }
                val educationArticleCards =
                    educationArticleCardsUseCase.getEducationArticleCards(
                        categoryID.toIntOrZero(),
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

    fun getEducationSeeAllData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationPageData

    fun getEducationCategoryChip(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationCategoryChip

    fun getTotalCount(): LiveData<Int> = totalCount
    fun hasMoreData(): LiveData<Boolean> = hasMoreData
}
