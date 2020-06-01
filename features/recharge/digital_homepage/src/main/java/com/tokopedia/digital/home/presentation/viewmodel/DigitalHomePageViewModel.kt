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
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
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
            }.getSuccessData<RechargeHomepageSections.Response>()

            mutableRechargeHomepageSections.postValue(Success(data.response))
        }) {
            mutableRechargeHomepageSections.postValue(Fail(it))
        }
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
    }
}
