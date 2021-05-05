package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital.home.model.RechargeHomepageSectionAction
import com.tokopedia.digital.home.model.RechargeHomepageSectionSkeleton
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeTickerHomepageModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeHomepageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.io) {

    private val mutableRechargeHomepageSectionSkeleton = MutableLiveData<Result<RechargeHomepageSectionSkeleton>>()
    val rechargeHomepageSectionSkeleton: LiveData<Result<RechargeHomepageSectionSkeleton>>
        get() = mutableRechargeHomepageSectionSkeleton

    var localRechargeHomepageSections: List<RechargeHomepageSections.Section> = listOf()

    private val mutableRechargeHomepageSections = MutableLiveData<List<RechargeHomepageSections.Section>>()
    val rechargeHomepageSections: LiveData<List<RechargeHomepageSections.Section>>
        get() = mutableRechargeHomepageSections

    private val mutableRechargeHomepageSectionAction = MutableLiveData<Result<RechargeHomepageSectionAction>>()
    val rechargeHomepageSectionAction: LiveData<Result<RechargeHomepageSectionAction>>
        get() = mutableRechargeHomepageSectionAction

    private val mutableRechargeTickerHomepageModel = MutableLiveData<Result<RechargeTickerHomepageModel>>()
    val rechargeTickerHomepageModel: LiveData<Result<RechargeTickerHomepageModel>>
        get() = mutableRechargeTickerHomepageModel

    val calledSectionIds = hashSetOf<Int>()

    fun getRechargeHomepageSectionSkeleton(mapParams: Map<String, Any>) {
        onRefreshData()
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    RechargeHomepageQueries.SKELETON_QUERY,
                    RechargeHomepageSectionSkeleton.Response::class.java, mapParams
            )
            val data = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeHomepageSectionSkeleton.Response>().response

            mutableRechargeHomepageSectionSkeleton.postValue(Success(data))
            // Add initial section data
            localRechargeHomepageSections = RechargeHomepageSectionMapper.mapInitialHomepageSections(data.sections)
            mutableRechargeHomepageSections.postValue(localRechargeHomepageSections)
        }) {
            mutableRechargeHomepageSectionSkeleton.postValue(Fail(it))
        }
    }

    fun getRechargeHomepageSections(mapParams: Map<String, Any>) {
        val requestIDs = (mapParams[PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS] as? List<Int>)
                ?: listOf()

        if (calledSectionIds.contains(requestIDs.firstOrNull() ?: 0)) return
        calledSectionIds.add(requestIDs.firstOrNull() ?: 0)

        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    RechargeHomepageQueries.SECTION_QUERY,
                    RechargeHomepageSections.Response::class.java, mapParams
            )
            val data = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeHomepageSections.Response>().response
            data.requestIDs = requestIDs

            /*
                Update local (viewmodel) section then update LiveData in order to
                prevent missing section updates caused by postValue override
             */
            withContext(dispatcher.main) {
                localRechargeHomepageSections = RechargeHomepageSectionMapper.updateSectionsData(localRechargeHomepageSections, data)
                mutableRechargeHomepageSections.value = localRechargeHomepageSections
            }
        }) {
            // Because error occured, remove sections
            withContext(dispatcher.main) {
                localRechargeHomepageSections = RechargeHomepageSectionMapper.updateSectionsData(
                        localRechargeHomepageSections,
                        RechargeHomepageSections(requestIDs = requestIDs)
                )
                mutableRechargeHomepageSections.value = localRechargeHomepageSections
            }
        }
    }

    private fun onRefreshData() {
        calledSectionIds.clear()
    }

    fun triggerRechargeSectionAction(mapParams: Map<String, Any>) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    RechargeHomepageQueries.ACTION_QUERY,
                    RechargeHomepageSectionAction.Response::class.java, mapParams
            )
            val data = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeHomepageSectionAction.Response>().response

            mutableRechargeHomepageSectionAction.postValue(Success(data))
        }) {
            mutableRechargeHomepageSectionAction.postValue(Fail(it))
        }
    }

    fun getTickerHomepageSection(mapParams: Map<String, Any>) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    RechargeHomepageQueries.TICKER_QUERY,
                    RechargeTickerHomepageModel::class.java, mapParams
            )
            val data = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeTickerHomepageModel>()

            mutableRechargeTickerHomepageModel.postValue(Success(data))
        }) {
            mutableRechargeTickerHomepageModel.postValue(Fail(it))
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

    fun createRechargeHomepageTickerParams(categoryId: List<Int>, deviceId: Int): Map<String, Any> {
        return mapOf(
                PARAM_RECHARGE_HOMEPAGE_SECTION_CATEGORY_ID to categoryId,
                PARAM_RECHARGE_HOMEPAGE_SECTION_DEVICE_ID to deviceId
        )
    }

    fun createRechargeHomepageSectionActionParams(
            sectionId: Int,
            actionName: String,
            sectionObjectId: String,
            itemObjectId: String
    ): Map<String, Any> {
        return mapOf(
                PARAM_RECHARGE_HOMEPAGE_SECTION_ID to sectionId,
                PARAM_RECHARGE_HOMEPAGE_SECTION_ACTION to String.format("%s:%s:%s", actionName, sectionObjectId, itemObjectId)
        )
    }

    fun getDynamicIconsSectionIds(): ArrayList<String> {
        return if (rechargeHomepageSectionSkeleton.value is Success) {
            val dynamicIconSectionsIds = arrayListOf<String>()
            (rechargeHomepageSectionSkeleton.value as Success).data.sections.filter { it.template.equals(SECTION_DYNAMIC_ICONS) }
                    .forEach { dynamicIconSectionsIds.add(it.id) }
            dynamicIconSectionsIds
        } else arrayListOf()
    }

    fun getSearchBarPlaceholder(): String {
        rechargeHomepageSectionSkeleton.value.let {
            return if (it is Success) it.data.searchBarPlaceholder else ""
        }
    }

    companion object {
        const val ID_TICKER = "0"

        const val PARAM_RECHARGE_HOMEPAGE_SECTION_ID = "sectionID"
        const val PARAM_RECHARGE_HOMEPAGE_SECTION_ACTION = "action"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID = "platformID"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS = "sectionIDs"
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE = "enablePersonalize"
        const val PARAM_RECHARGE_HOMEPAGE_SECTION_CATEGORY_ID = "categoryIDs"
        const val PARAM_RECHARGE_HOMEPAGE_SECTION_DEVICE_ID = "deviceID"

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
        const val SECTION_PRODUCT_CARD_CUSTOM_BANNER = "PRODUCT_CARD_CUSTOM_BANNER"
        const val SECTION_MINI_CAROUSELL = "MINI_CAROUSELL"
        const val SECTION_TICKER = "TICKER"
    }
}
