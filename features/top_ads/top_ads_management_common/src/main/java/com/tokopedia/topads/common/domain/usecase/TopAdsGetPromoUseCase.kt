package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.SingleAdInFo
import javax.inject.Inject

const val TOP_ADS_GET_PROMO_QUERY = """query topAdsGetPromo(${'$'}shopID: String!, ${'$'}adID: String!) {
  topAdsGetPromo(shopID: ${'$'}shopID, adID: ${'$'}adID) {
    data {
      adID
      adType
      groupID
      shopID
      itemID
      status
      priceBid
      priceDaily
      adStartDate
      adStartTime
      adEndDate
      adEndTime
      adImage
      adTitle
      cpmDetails {
        link
        title
        description {
          slogan
        }
        product {
          productID
          productName
          productImage
          productURL
          productPrice
          productActive
          productRating
          productReviewCount
          departmentID
          departmentName
        }
      }
    }
    errors {
      code
      detail
      title
    }
  }
}
"""

@GqlQuery("TopAdsGetPromoQuery", TOP_ADS_GET_PROMO_QUERY)
class TopAdsGetPromoUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<SingleAdInFo>(graphqlRepository) {

    init {
        setTypeClass(SingleAdInFo::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(TopAdsGetPromoQuery.GQL_QUERY)
    }

    fun setParams(adId:String, shopId:String) {
        val queryMap = mutableMapOf(
                ParamObject.AD_ID to adId,
                ParamObject.SHOP_ID to shopId
        )
        setRequestParams(queryMap)
    }
}