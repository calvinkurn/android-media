package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DigitalHomePageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: DigitalHomePageDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

    private val mutableRechargeHomepageSections = MutableLiveData<Result<RechargeHomepageSections>>()
    val rechargeHomepageSections: LiveData<Result<RechargeHomepageSections>>
        get() = mutableRechargeHomepageSections

    fun getRechargeHomepageSections(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeHomepageSections.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeHomepageSections.Response>().response

            // TODO: Adjust filter with addition of sections & remove filter when all sections are done
            data.sections = data.sections.filter {
                it.template == RechargeHomepageSections.SECTION_TOP_ICONS ||
                it.template == RechargeHomepageSections.SECTION_DYNAMIC_ICONS ||
                it.template == RechargeHomepageSections.SECTION_DUAL_ICONS
            }
            mutableRechargeHomepageSections.postValue(Success(data))
        }) {
            mutableRechargeHomepageSections.postValue(Fail(it))
        }
    }

    fun createRechargeHomepageSectionsParams(enablePersonalize: Boolean = false): Map<String, Any> {
        return mapOf(PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize)
    }

    companion object {
        const val PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE = "enablePersonalize"
    }
}
