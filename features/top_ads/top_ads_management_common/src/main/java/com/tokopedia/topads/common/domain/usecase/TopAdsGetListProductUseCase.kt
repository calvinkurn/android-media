package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.ResponseProductList
import javax.inject.Inject

const val TOP_ADS_GET_LIST_PRODUCT_QUERY: String = """query topadsGetListProduct(${'$'}shopID: Int!, ${'$'}keyword: String, 
    ${'$'}etalase: String, ${'$'}sortBy: String,  ${'$'}rows: Int!, ${'$'}start: Int,${'$'}status:String)
    { topadsGetListProduct(shopID: ${'$'}shopID, keyword: ${'$'}keyword, etalase: ${'$'}etalase, sortBy: ${'$'}sortBy,  
    rows: ${'$'}rows, start: ${'$'}start,status:${'$'}status) {
    data {
      productID
            productName
            productPrice
            productPriceNum
            productImage
            productIsPromoted
            productURI
            adID
            adStatus
            groupName
            groupID
            departmentID
            departmentName
            priceBid
            suggestedBid
            productRating
            productReviewCount
    }
    eof
    errors {
      code
      detail
      title
    }
  }
}
"""

@GqlQuery("TopAdsGetListProductQuery", TOP_ADS_GET_LIST_PRODUCT_QUERY)
class TopAdsGetListProductUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ResponseProductList.Result>(graphqlRepository) {

    init {
        setTypeClass(ResponseProductList.Result::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(TopAdsGetListProductQuery.GQL_QUERY)
    }

    fun setParams(keyword: String, etalaseId: String, sortBy: String, promoted: String, rows: Int, start: Int, shopId:Int) {
        val queryMap = mutableMapOf(
                ParamObject.KEYWORD to keyword,
                ParamObject.ETALASE to etalaseId,
                ParamObject.SORT_BY to sortBy,
                ParamObject.ROWS to rows,
                ParamObject.START to start,
                ParamObject.STATUS to promoted,
                ParamObject.SHOP_ID to shopId
        )
        setRequestParams(queryMap)
    }
}