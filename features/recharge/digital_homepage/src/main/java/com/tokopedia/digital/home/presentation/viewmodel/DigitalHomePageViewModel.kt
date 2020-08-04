package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import com.tokopedia.digital.home.model.RechargeHomepageSectionSkeleton
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.DigitalHomePageDispatchersProvider
import com.tokopedia.graphql.GraphqlConstant
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

    private val mutableRechargeHomepageSectionSkeleton = MutableLiveData<Result<List<RechargeHomepageSectionSkeleton.Item>>>()
    val rechargeHomepageSectionSkeleton: LiveData<Result<List<RechargeHomepageSectionSkeleton.Item>>>
        get() = mutableRechargeHomepageSectionSkeleton
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

    fun getRechargeHomepageSectionSkeleton(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeHomepageSectionSkeleton.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeHomepageSectionSkeleton.Response>().response

            mutableRechargeHomepageSectionSkeleton.postValue(Success(data.sections))
//            val filteredSections = data.sections.filter { it.template != SECTION_DYNAMIC_ICONS }
//            mutableRechargeHomepageSectionSkeleton.postValue(Success(filteredSections))
        }) {
            mutableRechargeHomepageSectionSkeleton.postValue(Fail(it))
        }
    }

    fun getRechargeHomepageSections(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeHomepageSections.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeHomepageSections.Response>().response
            data.requestIDs = (mapParams[PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS] as? List<Int>) ?: listOf()

            mutableRechargeHomepageSections.postValue(Success(data))
        }) {
            mutableRechargeHomepageSections.postValue(Fail(it))
        }
    }

    fun createRechargeHomepageSectionSkeletonParams(platformId: Int, enablePersonalize: Boolean = false): Map<String, Any> {
        return mapOf(
                PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to platformId,
                PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize
        )
    }

    fun createRechargeHomepageSectionsParams(platformId: Int, sectionIDs: List<Int>, enablePersonalize: Boolean = false): Map<String, Any> {
        return mapOf(
                PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to platformId,
                PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS to sectionIDs,
                PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize
        )
    }

    companion object {
        const val CATEGORY_SECTION_ORDER: Int = 7
        val SECTION_ORDERING = mapOf(
                DigitalHomePageUseCase.BANNER_ORDER to 0,
                DigitalHomePageUseCase.FAVORITES_ORDER to 1,
                DigitalHomePageUseCase.TRUST_MARK_ORDER to 2,
                DigitalHomePageUseCase.RECOMMENDATION_ORDER to 3,
                DigitalHomePageUseCase.NEW_USER_ZONE_ORDER to 4,
                DigitalHomePageUseCase.SPOTLIGHT_ORDER to 5,
                DigitalHomePageUseCase.SUBSCRIPTION_ORDER to 6,
                DigitalHomePageUseCase.CATEGORY_ORDER to CATEGORY_SECTION_ORDER,
                DigitalHomePageUseCase.PROMO_ORDER to 8
        )

        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID = "platformID"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS = "sectionIDs"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE = "enablePersonalize"

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

        val SECTION_HOME_COMPONENTS = listOf(SECTION_URGENCY_WIDGET, SECTION_LEGO_BANNERS)
    }
}
