package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.SINGLE_ROW
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.common.domain.interactor.TOP_ADS_GET_GROUP_LIST_QUERY
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 29/5/20.
 */


@GqlQuery("GetTopadsGroupListQuery", TOP_ADS_GET_GROUP_LIST_QUERY)
class TopAdsGetGroupListUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<GroupItemResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetTopadsGroupListQuery.GQL_QUERY)
    }

    fun setParamsForKeyWord(search: String) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toIntOrZero()
        queryMap[ParamObject.SEPARATE_STAT] = "true"
        queryMap[KEYWORD] = search
        queryMap[GROUP_TYPE] = 1
        queryMap[SINGLE_ROW] = "1"/*keywords*/
        setRequestParams(mapOf(QUERY_INPUT to queryMap))
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (GroupItemResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(GroupItemResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}