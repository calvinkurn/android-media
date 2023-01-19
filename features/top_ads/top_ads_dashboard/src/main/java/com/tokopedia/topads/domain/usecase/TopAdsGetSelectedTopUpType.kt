package com.tokopedia.topads.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.model.GetPersonalisedCopyResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val GET_PERSONALISED_COPY_QUERY = """query getPersonalisedCopy(${'$'}shop_id: String!) {
  GetPersonalisedCopy(shop_id: ${'$'}shop_id) {
    data {
      credit_performance
      is_auto_top_up
      count_top_up
    }
  }
}
"""

@GqlQuery("GetPersonalisedCopyQuery", GET_PERSONALISED_COPY_QUERY)
class TopAdsGetSelectedTopUpType @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<GetPersonalisedCopyResponse>(graphqlRepository) {

    init {
        setTypeClass(GetPersonalisedCopyResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetPersonalisedCopyQuery())
        setParams()
    }

    private fun setParams() {
        val params = mutableMapOf(
            ParamObject.SHOP_id to userSession.shopId
        )
        setRequestParams(params)
    }
}
