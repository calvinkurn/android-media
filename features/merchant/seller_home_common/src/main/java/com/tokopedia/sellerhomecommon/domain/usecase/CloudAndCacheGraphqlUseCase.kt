package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.BaseResponseMapper
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class CloudAndCacheGraphqlUseCase<R : Any, U : Any> @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val mapper: BaseResponseMapper<R, U>,
        private val sessionIncluded: Boolean,
        private val classType: Class<R>,
        private val graphqlQuery: String,
        private val doQueryHash: Boolean) {

    private var mFingerprintManager: FingerprintManager? = null
    private var mCacheManager: GraphqlCacheManager? = null
    private var resultFlow: Channel<U> = Channel(Channel.BUFFERED)
    var collectingResult: Boolean = false

    private fun initCacheManager() {
        if (mCacheManager == null) {
            mCacheManager = GraphqlCacheManager()
        }
        if (mFingerprintManager == null) {
            mFingerprintManager = GraphqlClient.getFingerPrintManager()
        }
    }

    private suspend fun getCachedResponse(request: GraphqlRequest): U? {
        try {
            val response = graphqlRepository.getReseponse(listOf(request), getCacheOnlyCacheStrategy())

            val error = response.getError(classType)

            if (error == null || error.isEmpty()) {
                val successResponse = response.getData<R>(classType)
                return mapper.mapRemoteDataToUiData(successResponse, true)
            }
        } catch (e: Exception) {
            // ignore exception from cache
        }
        return null
    }

    private suspend fun getCloudResponse(request: GraphqlRequest): U {
        val response = graphqlRepository.getReseponse(listOf(request), getCloudCacheStrategy())

        val error = response.getError(classType)

        if (error == null || error.isEmpty()) {
            val successResponse = response.getData<R>(classType)
            return mapper.mapRemoteDataToUiData(successResponse, false)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    private fun getCloudCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
                .setSessionIncluded(sessionIncluded)
                .build()
    }

    private fun getCacheOnlyCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setSessionIncluded(sessionIncluded)
                .build()
    }

    open suspend fun executeOnBackground(requestParams: RequestParams = RequestParams.EMPTY, includeCache: Boolean = true): Unit = withContext(Dispatchers.IO) {
        if (graphqlQuery.isBlank()) {
            throw RuntimeException("GQL query must not empty!")
        }
        val request = GraphqlRequest(doQueryHash, graphqlQuery, classType, requestParams.parameters)
        if (includeCache) {
            launch {
                getCachedResponse(request)?.let {
                    resultFlow.offer(it)
                }
            }
        }
        launch {
            resultFlow.offer(getCloudResponse(request))
        }
        Unit
    }


    fun clearCache(requestParams: RequestParams = RequestParams.EMPTY) {
        try {
            initCacheManager()
            val request = GraphqlRequest(doQueryHash, graphqlQuery, classType, requestParams.parameters)
            mCacheManager!!.delete(mFingerprintManager!!.generateFingerPrint(request.toString(), sessionIncluded))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getResultFlow(): Flow<U> = resultFlow.consumeAsFlow()
}