package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val GROUP_VALIDATE_NAME_QUERY: String = """query topAdsGroupValidateNameV2(${'$'}shopID: String!, ${'$'}groupName: String!, ${'$'}source : String!){
  topAdsGroupValidateNameV2(shopID: ${'$'}shopID, groupName: ${'$'}groupName, source: ${'$'}source){
      data {
        shopID
        groupName
      }
      errors {
         code
         detail
         title
      }
  }
}
"""

@GqlQuery("GroupValidateNameQuery", GROUP_VALIDATE_NAME_QUERY)
class TopAdsGroupValidateNameUseCase @Inject constructor(graphqlRepository: GraphqlRepository
                                                         , val userSession: UserSessionInterface)
    : GraphqlUseCase<ResponseGroupValidateName>(graphqlRepository) {

    init {
        setTypeClass(ResponseGroupValidateName::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GroupValidateNameQuery.GQL_QUERY)

    }

    fun setParams(groupName: String, source: String) {
        val params = mutableMapOf(
            ParamObject.SHOP_ID to userSession.shopId,
            ParamObject.GROUP_NAME to groupName,
            ParamObject.SOURCE to source
        )
        setRequestParams(params)
    }
}