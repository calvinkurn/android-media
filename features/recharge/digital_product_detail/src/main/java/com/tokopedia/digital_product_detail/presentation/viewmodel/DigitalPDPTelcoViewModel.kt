package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
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
import kotlinx.coroutines.*
import javax.inject.Inject

class DigitalPDPTelcoViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private var debounceJob: Job? = null

    private val _dummy = MutableLiveData<Result<Boolean>>()
    val dummy: LiveData<Result<Boolean>>
        get() = _dummy

    private val _catalogPrefixSelect = MutableLiveData<Result<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<Result<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    fun getDelayedResponse() {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(coroutineContext).launch {
            launchCatchError(block = {
                delay(1000)
                _dummy.postValue(Success(true))
            }) {

            }
        }
    }

    fun getPrefixOperator(rawQuery: String, menuId: Int) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam[KEY_MENU_ID] = menuId

            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogPrefixSelect::class.java, mapParam)
                graphqlRepository.response(listOf(graphqlRequest),
                    GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXP_TIME).build())
            }.getSuccessData<TelcoCatalogPrefixSelect>()

            delay(DELAY_TIME)
            _catalogPrefixSelect.postValue(Success(data))
        }) {
            _catalogPrefixSelect.postValue(Fail(it))
        }
    }

    companion object {
        const val KEY_MENU_ID = "menuID"
        const val EXP_TIME = 10
        const val DELAY_TIME = 200L
    }
}