package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.model.RechargeHomepageAbstractSectionModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.DigitalHomePageDispatchersProvider
import com.tokopedia.digital.home.presentation.Util.RechargeHomepageSectionMapper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DigitalHomePageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: DigitalHomePageDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

    private val mutableRechargeHomepageSections = MutableLiveData<Result<List<RechargeHomepageAbstractSectionModel>>>()
    val rechargeHomepageSections: LiveData<Result<List<RechargeHomepageAbstractSectionModel>>>
        get() = mutableRechargeHomepageSections

    fun getRechargeHomepageSections(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeHomepageSections.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeHomepageSections.Response>().response

            val mappedData = RechargeHomepageSectionMapper.mapHomepageSections(data.sections).filterNotNull()
            mutableRechargeHomepageSections.postValue(Success(mappedData))
        }) {
            mutableRechargeHomepageSections.postValue(Fail(it))
        }
    }

    fun createRechargeHomepageSectionsParams(enablePersonalize: Boolean = false): Map<String, Any> {
        return mapOf(PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize)
    }

    companion object {
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
    }
}
