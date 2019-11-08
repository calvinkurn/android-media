package com.tokopedia.emoney.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.emoney.data.BrizziTokenResponse
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BrizziTokenViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val tokenBrizzi = MutableLiveData<String>()

    //token on server will refresh automatically per 30 minutes
    fun getTokenBrizzi(rawQuery: String, refresh: Boolean) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(REFRESH_TOKEN, refresh)

            val data = withContext(Dispatchers.IO) {
                val graphqlRequest = GraphqlRequest(rawQuery, BrizziTokenResponse::class.java, mapParam)
                if (refresh) {
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                } else {
                    graphqlRepository.getReseponse(listOf(graphqlRequest), GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 30).build())
                }
            }.getSuccessData<BrizziTokenResponse>()

            tokenBrizzi.value = data.tokenResponse.token
        }) {
            tokenBrizzi.value = ""
        }
    }

    companion object {
        const val REFRESH_TOKEN = "refresh"
    }
}