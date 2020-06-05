package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.BANNER_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.CATEGORY_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.FAVORITES_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.NEW_USER_ZONE_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.PROMO_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.RECOMMENDATION_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.SPOTLIGHT_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.SUBSCRIPTION_ORDER
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase.Companion.TRUST_MARK_ORDER
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.Util.DigitalHomePageDispatchersProvider
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DigitalHomePageViewModel @Inject constructor(
        private val digitalHomePageUseCase: DigitalHomePageUseCase,
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: DigitalHomePageDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

    private val mutableDigitalHomePageList = MutableLiveData<List<DigitalHomePageItemModel>>()
    val digitalHomePageList: LiveData<List<DigitalHomePageItemModel>>
        get() = mutableDigitalHomePageList
    private val mutableIsAllError = MutableLiveData<Boolean>()
    val isAllError: LiveData<Boolean>
        get() = mutableIsAllError
    private val mutableRechargeHomepageSections = MutableLiveData<Result<RechargeHomepageSections>>()
    val rechargeHomepageSections: LiveData<Result<RechargeHomepageSections>>
        get() = mutableRechargeHomepageSections

    fun initialize(queryList: Map<String, String>) {
        val list: List<DigitalHomePageItemModel> = digitalHomePageUseCase.getEmptyList()
        digitalHomePageUseCase.queryList = queryList
        digitalHomePageUseCase.sectionOrdering = SECTION_ORDERING
        mutableDigitalHomePageList.value = list
        mutableIsAllError.value = false
    }

    fun getData(isLoadFromCloud: Boolean) {
        digitalHomePageUseCase.isFromCloud = isLoadFromCloud
        launch(dispatcher.IO) {
            val data = digitalHomePageUseCase.executeOnBackground()
            if (data.isEmpty() || checkError(data)) {
                mutableIsAllError.postValue(true)
            } else {
                mutableDigitalHomePageList.postValue(data)
            }
        }
    }

    private fun checkError(data: List<DigitalHomePageItemModel>): Boolean {
        return data.all { !it.isSuccess }
    }

    fun getRechargeHomepageSections(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeHomepageSections.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeHomepageSections.Response>().response

            // Map views based on section type
            data.sections = data.sections.map {
                when (it.template) {
                    SECTION_TOP_BANNER -> RechargeHomepageTopBannerModel(it)
                    SECTION_TOP_BANNER_EMPTY -> RechargeHomepageTopBannerEmptyModel(it)
                    SECTION_TOP_ICONS -> RechargeHomepageTopIconsModel(it)
                    SECTION_DYNAMIC_ICONS -> RechargeHomepageDynamicIconsModel(it)
                    SECTION_DUAL_ICONS -> RechargeHomepageDualIconsModel(it)
                    SECTION_URGENCY_WIDGET -> RechargeHomepageUrgencyWidgetModel(it)
                    SECTION_VIDEO_HIGHLIGHT -> RechargeHomepageVideoHighlightModel(it)
                    SECTION_VIDEO_HIGHLIGHTS -> RechargeHomepageVideoHighlightsModel(it)
                    SECTION_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it)
                    SECTION_COUNTDOWN_SINGLE_BANNER -> RechargeHomepageCountdownSingleBannerModel(it)
                    SECTION_DUAL_BANNERS -> RechargeHomepageDualBannersModel(it)
                    SECTION_LEGO_BANNERS -> RechargeHomepageLegoBannersModel(it)
                    SECTION_PRODUCT_CARD_ROW -> RechargeHomepageProductCardRowModel(it)
                    SECTION_COUNTDOWN_PRODUCT_BANNER -> RechargeHomepageCountdownProductBannerModel(it)
                    else -> it
                }
            }

            mutableRechargeHomepageSections.postValue(Success(data))
        }) {
            mutableRechargeHomepageSections.postValue(Fail(it))
        }
    }

    fun createRechargeHomepageSectionsParams(enablePersonalize: Boolean): Map<String, Any> {
        return mapOf(PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize)
    }

    companion object {
        const val CATEGORY_SECTION_ORDER: Int = 7
        val SECTION_ORDERING = mapOf(
                BANNER_ORDER to 0,
                FAVORITES_ORDER to 1,
                TRUST_MARK_ORDER to 2,
                RECOMMENDATION_ORDER to 3,
                NEW_USER_ZONE_ORDER to 4,
                SPOTLIGHT_ORDER to 5,
                SUBSCRIPTION_ORDER to 6,
                CATEGORY_ORDER to CATEGORY_SECTION_ORDER,
                PROMO_ORDER to 8
        )

        const val SECTION_TOP_BANNER = "TOP_BANNER"
        const val SECTION_TOP_BANNER_EMPTY = "TOP_BANNER_EMPTY"
        const val SECTION_TOP_ICONS = "TOP_ICONS"
        const val SECTION_DYNAMIC_ICONS = "DYNAMIC_ICONS"
        const val SECTION_DUAL_ICONS = "DUAL_ICONS"
        const val SECTION_URGENCY_WIDGET = "URGENCY_WIDGET"
        const val SECTION_VIDEO_HIGHLIGHT = "VIDEO_HIGHLIGHT"
        const val SECTION_VIDEO_HIGHLIGHTS = "VIDEO_HIGHLIGHTS"
        const val SECTION_SINGLE_BANNER = "SINGLE_BANNER"
        const val SECTION_COUNTDOWN_SINGLE_BANNER = "COUNTDOWN_SINGLE_BANNER"
        const val SECTION_DUAL_BANNERS = "DUAL_BANNERS"
        const val SECTION_LEGO_BANNERS = "LEGO_BANNERS"
        const val SECTION_PRODUCT_CARD_ROW = "PRODUCT_CARD_ROW"
        const val SECTION_COUNTDOWN_PRODUCT_BANNER = "COUNTDOWN_PRODUCT_BANNER"

        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE = "enablePersonalize"
    }
}
