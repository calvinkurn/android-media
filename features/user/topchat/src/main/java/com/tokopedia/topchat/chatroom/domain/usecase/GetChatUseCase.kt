package com.tokopedia.topchat.chatroom.domain.usecase

import android.content.res.Resources
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.*
import com.tokopedia.graphql.data.source.cache.GraphqlCacheDataStore
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topchat.R
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.reflect.Type
import javax.inject.Inject


/**
 * @author : Steven 30/11/18
 */

class GetChatUseCase @Inject constructor(
        val resources: Resources,
        private val graphqlUseCase: GraphqlUseCase,
        private val cacheDataStore: GraphqlCacheDataStore
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_topchat_replies)
        val graphqlRequest = GraphqlRequest(query,
                GetExistingChatPojo::class.java, requestParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(requestParams[PARAM_CACHE_STRATEGY] as CacheType?)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

    fun getCache(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_topchat_replies)
        val graphqlRequest = GraphqlRequest(query,
                GetExistingChatPojo::class.java, requestParams)
        var requests = arrayListOf(graphqlRequest)
        val cacheStrategy = GraphqlCacheStrategy.Builder(requestParams[PARAM_CACHE_STRATEGY] as CacheType?)
                .setSessionIncluded(true).build()
        var cache = observeCache(requests, cacheStrategy, subscriber)
    }

    private fun observeCache(requests: ArrayList<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy, subscriber: Subscriber<GraphqlResponse>){
        var observable = cacheDataStore.getResponse(requests, cacheStrategy)
        observable.map{
            response ->
            val results = java.util.HashMap<Type, Any>()
            val errors = java.util.HashMap<Type, List<GraphqlError>>()
            for (i in 0 until response.originalResponse.size()) {
                try {
                    val data = response.originalResponse.get(i).asJsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
                    if (data != null && !data.isJsonNull) {
                        //Lookup for data
                        results[requests.get(i).getTypeOfT()] = CommonUtils.fromJson(data.toString(), requests.get(i).getTypeOfT())
                    }

                    val error = response.originalResponse.get(i).asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                    if (error != null && !error.isJsonNull) {
                        //Lookup for error
                        errors[requests.get(i).getTypeOfT()] = CommonUtils.fromJson(error.toString(), object : TypeToken<List<GraphqlError>>() {

                        }.type)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    //Just to avoid any accidental data loss
                }
            }

            GraphqlResponse(results, errors, response.isCached())
        }.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)


    }

    companion object {

        private val PARAM_MESSAGE_ID: String = "msgId"
        private val PARAM_PAGE: String = "page"
        private val PARAM_CACHE_STRATEGY: String = "cache"


        fun generateParamFirstTime(messageId: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = if (messageId.isNotBlank()) messageId.toInt() else 0
            requestParams[PARAM_PAGE] = 1
            requestParams[PARAM_CACHE_STRATEGY] = CacheType.CLOUD_THEN_CACHE
            return requestParams
        }

        fun generateParam(messageId: String, page: Int): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = if (messageId.isNotBlank()) messageId.toInt() else 0
            requestParams[PARAM_PAGE] = page
            requestParams[PARAM_CACHE_STRATEGY] = CacheType.CLOUD_THEN_CACHE
            return requestParams
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}