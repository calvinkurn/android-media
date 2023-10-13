package com.tokopedia.entertainment.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.entertainment.common.model.EventDppoConsentModel
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.viewmodel.LoadingHomeModel
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.entertainment.home.utils.MapperHomeData.mapDppoConsentToEventModel
import com.tokopedia.entertainment.home.utils.MapperHomeData.mappingItem
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventHomeViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getDppoConsentUseCase: GetDppoConsentUseCase,
    private val graphqlRepository: GraphqlRepository
) : BaseViewModel(dispatcher) {

    private val mutableEventHomeListData = MutableLiveData<Result<List<HomeEventItem>>>()
    val eventHomeListData: LiveData<Result<List<HomeEventItem>>>
        get() = mutableEventHomeListData

    private val mutableDppoConsent = MutableLiveData<Result<EventDppoConsentModel>>()
    val dppoConsent: LiveData<Result<EventDppoConsentModel>>
        get() = mutableDppoConsent

    fun getIntialList() {
        val list: List<HomeEventItem> = requestEmptyItem()
        mutableEventHomeListData.value = Success(list)
    }

    fun getHomeData(isLoadFromCloud: Boolean) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                EventQuery.getEventHomeQuery(),
                EventHomeDataResponse.Data::class.java,
                mapOf(CATEGORY to "event")
            )
            val cacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXPIRY_TIME).build()
            val data = withContext(dispatcher) {
                graphqlRepository.response(listOf(graphqlRequest), cacheStrategy)
            }.getSuccessData<EventHomeDataResponse.Data>()

            mutableEventHomeListData.postValue(Success(mappingItem(data)))
        }) {
            mutableEventHomeListData.postValue(Fail(it))
        }
    }

    fun getDppoConsent() {
        launchCatchError(block = {
            val data = getDppoConsentUseCase.execute(DPPO_CATEGORY_ID)
            val eventModel = mapDppoConsentToEventModel(data)
            mutableDppoConsent.postValue(Success(eventModel))
        }) {
            mutableDppoConsent.postValue(Fail(it))
        }
    }

    private fun requestEmptyItem(): List<HomeEventItem> {
        return listOf(LoadingHomeModel())
    }

    companion object {
        private const val CATEGORY = "category"
        private const val EXPIRY_TIME = 5
        private const val DPPO_CATEGORY_ID = 23
    }
}
