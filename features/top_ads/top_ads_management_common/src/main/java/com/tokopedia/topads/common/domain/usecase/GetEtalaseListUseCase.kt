package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.response.ResponseEtalase
import javax.inject.Inject

const val GET_ETALASE_LIST_QUERY: String = """query getEtalaseList(${'$'}shopId:String!){
  shopShowcasesByShopID(shopId:${'$'}shopId, hideNoCount:true,hideShowcaseGroup:true,isOwner:false) {
    result {
      id
      name
      count
      type
    }
  }
}
"""

private const val SHOP_ID = "shopId"
@GqlQuery("GetEtalaseListQuery", GET_ETALASE_LIST_QUERY)
class GetEtalaseListUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ResponseEtalase.Data>(graphqlRepository) {

    init {
        setTypeClass(ResponseEtalase.Data::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetEtalaseListQuery.GQL_QUERY)
    }

    fun setParams(shopId: String) {
        val params = mutableMapOf(
                SHOP_ID to shopId
        )
        setRequestParams(params)
    }

}