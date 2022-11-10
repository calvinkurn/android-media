package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationBannerResponse
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationArticleTopicRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationBannerUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationEventRVUiModel
import com.tokopedia.affiliate.usecase.AffiliateEducationBannerUseCase
import com.tokopedia.affiliate.usecase.AffiliateEducationCategoryTreeUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import timber.log.Timber
import javax.inject.Inject

class AffiliateEducationLandingViewModel @Inject constructor(
    private val educationBannerUseCase: AffiliateEducationBannerUseCase,
    private val educationCategoryUseCase: AffiliateEducationCategoryTreeUseCase
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
            convertToVisitable(educationBanners, educationCategories)
        }, onError = { Timber.e(it) })
    }

    private fun convertToVisitable(
        educationBannerResponse: AffiliateEducationBannerResponse,
        educationCategoryResponse: AffiliateEducationCategoryResponse
    ) {
        val tempList = mutableListOf<Visitable<AffiliateAdapterTypeFactory>>()
        educationBannerResponse.dynamicBanner?.let { educationBanners ->
            tempList.add(AffiliateEducationBannerUiModel(educationBanners.data?.banners))
        }
        educationCategoryResponse.categoryTree?.let { educationCategories ->
            val categoryGroup =
                educationCategories.data?.categories?.groupBy { it?.id.orZero() }
            tempList.add(
                AffiliateEducationArticleTopicRVUiModel(
                    categoryGroup?.get(TYPE_ARTICLE)?.get(0)?.children
                )
            )
            tempList.add(
                AffiliateEducationEventRVUiModel(
                    categoryGroup?.get(TYPE_EVENT)?.get(0)?.children
                )
            )
            tempList.add(
                AffiliateEducationArticleRVUiModel(
                    categoryGroup?.get(TYPE_ARTICLE)?.get(0)?.children
                )
            )
        }

        educationPageData.value = tempList
    }

    fun getEducationPageData(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> =
        educationPageData

}
