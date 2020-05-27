package com.tokopedia.topads.edit.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.topads.edit.R
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class SuggestionKeywordUseCase @Inject constructor(@ActivityContext val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<KeywordSuggestionResponse.Result>(graphqlRepository) {

    fun setParams(groupId: Int?, productIds: String?) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.PRODUCT_IDS] = productIds
        queryMap[ParamObject.GROUP_ID] = groupId
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toInt()
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_ad_keyword_suggestion)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (KeywordSuggestionResponse.Result) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(KeywordSuggestionResponse.Result::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}