package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.data.GraphqlClient


class MultiRequestGraphqlUseCase(private val graphqlRepository: GraphqlRepository): UseCase<GraphqlResponse>() {

    private val requests = mutableListOf<GraphqlRequest>()
    private var cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()
    private var mCacheManager: GraphqlCacheManager? = null
    private var mFingerprintManager: FingerprintManager? = null

    override suspend fun executeOnBackground(): GraphqlResponse {
        if (requests.isEmpty()){
            throw RuntimeException("Please set valid request parameter before executing the use-case");
        }
        return graphqlRepository.getReseponse(requests, cacheStrategy)
    }

    fun addRequest(request: GraphqlRequest){
        requests += request
    }

    fun addRequests(requests: List<GraphqlRequest>){
        this.requests += requests
    }

    fun clearRequest() = requests.clear()

    fun setCacheStrategy(cacheStrategy: GraphqlCacheStrategy){
        this.cacheStrategy = cacheStrategy
    }

    fun clearCache(){
        try {
            Observable.fromCallable {
                initCacheManager()
                requests.forEach {
                    mCacheManager!!.delete(mFingerprintManager!!.generateFingerPrint(
                        it.toString(),
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
}