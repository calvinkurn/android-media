package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.util.LoggingUtils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

open class GraphqlUseCase<T: Any> @Inject constructor(private val graphqlRepository: GraphqlRepository): UseCase<T>() {

    private var cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()
    private var graphqlQuery: String? = null
    private var requestParams = mapOf<String, Any?>()
    private var tClass: Class<T>? = null
    private var doQueryHash = false

    private var mCacheManager: GraphqlCacheManager? = null
    private var mFingerprintManager: FingerprintManager? = null

    fun setCacheStrategy(cacheStrategy: GraphqlCacheStrategy){
        this.cacheStrategy = cacheStrategy
    }

    fun setTypeClass(tClass: Class<T>){
        this.tClass = tClass
    }

    fun setGraphqlQuery(query: String){
        this.graphqlQuery = query
    }

    fun setRequestParams(params: Map<String, Any?>){
        this.requestParams = params
    }

    fun setQueryHashFlag(doQueryHash: Boolean){
        this.doQueryHash = doQueryHash;
    }

    fun clearCache(){
        try {
            Observable.fromCallable {
                initCacheManager()
                if (graphqlQuery != null) {
                    val request = GraphqlRequest(doQueryHash, graphqlQuery, tClass, requestParams)
                    mCacheManager!!.delete(mFingerprintManager!!.generateFingerPrint(
                            request.toString(),
                            cacheStrategy.isSessionIncluded))

                }
            }.subscribeOn(Schedulers.io()).subscribe({
                //no op
            }, {
                //ignore error
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initCacheManager() {
        if (mCacheManager == null) {
            mCacheManager = GraphqlCacheManager()
        }
        if (mFingerprintManager == null) {
            mFingerprintManager = GraphqlClient.getFingerPrintManager()
        }
    }

    override suspend fun executeOnBackground(): T {
        if (graphqlQuery == null){
            throw RuntimeException("Please set valid GraphQL query parameter before executing the use-case")
        }

        val type = tClass ?: throw RuntimeException("Please set valid class type before call execute()")
        val request = GraphqlRequest(doQueryHash, graphqlQuery, type, requestParams)
        val listOfRequest = listOf(request)
        val response = graphqlRepository.getReseponse(listOfRequest, cacheStrategy)

        val error = response.getError(tClass)

        if (error == null || error.isEmpty()){
            return response.getData(tClass)
        } else {
            val errorMessage = error.mapNotNull { it.message }.joinToString(separator = ", ")
            LoggingUtils.logGqlErrorBackend("executeOnBackground", listOfRequest.toString() ,errorMessage)
            throw MessageErrorException(errorMessage)
        }
    }
}