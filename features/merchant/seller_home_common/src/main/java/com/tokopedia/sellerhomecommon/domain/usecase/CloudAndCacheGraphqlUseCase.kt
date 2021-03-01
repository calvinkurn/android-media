package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhomecommon.domain.mapper.BaseResponseMapper
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private var results: MutableSharedFlow<U> = MutableSharedFlow(replay = 1)
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
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                .setSessionIncluded(sessionIncluded)
                .build()
    }

    private fun getCacheOnlyCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
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
                    results.emit(it)
                }
            }
        }
        launch {
            results.emit(getCloudResponse(request))
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

    fun getResultFlow(): SharedFlow<U> = results.asSharedFlow()
}