package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.raw.GROUP_VALIDATION_QUERY
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

@GqlQuery("EditGroupQuery", GROUP_VALIDATION_QUERY)
class ValidGroupUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ResponseGroupValidateName>(graphqlRepository) {


    fun setParams(groupName: String) {
        val params = mutableMapOf(
                ParamObject.SHOP_ID to userSession.shopId.toInt(),
                ParamObject.GROUP_NAME to groupName
        )
        setRequestParams(params)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(ResponseGroupValidateName::class.java)
        setGraphqlQuery(EditGroupQuery.GQL_QUERY)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it.topAdsGroupValidateName)

        }, onError)
    }
}