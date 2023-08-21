package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.common.domain.query.GetTopadsKeywordSuggestionV3_1
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 6/11/20.
 */

class SuggestionKeywordUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<KeywordSuggestionResponse.Result>(graphqlRepository) {

    fun setParams(groupId: Int?, productIds: String?,type:Int = 1) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.PRODUCT_IDS] = productIds
        queryMap[ParamObject.GROUP_ID] = groupId
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[ParamObject.TYPE] = type
        setRequestParams(queryMap)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (KeywordSuggestionResponse.Result) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(KeywordSuggestionResponse.Result::class.java)
        setGraphqlQuery(GetTopadsKeywordSuggestionV3_1)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}
