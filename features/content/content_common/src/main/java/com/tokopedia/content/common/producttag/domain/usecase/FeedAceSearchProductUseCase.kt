package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.FeedAceSearchProductResponse
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 28, 2022
 */
@GqlQuery(FeedAceSearchProductUseCase.QUERY_NAME, FeedAceSearchProductUseCase.QUERY)
class FeedAceSearchProductUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedAceSearchProductResponse>(gqlRepository) {

    init {
        setGraphqlQuery(FeedAceSearchProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedAceSearchProductResponse::class.java)
    }

    companion object {
        private const val PARAMS = "params"

        const val QUERY_NAME = "FeedAceSearchProductUseCaseQuery"
        const val QUERY = """
            query FeedAceSearchProduct(${"$$PARAMS"}: String!) {
              ace_search_product_v4($PARAMS: ${"$$PARAMS"}) {
                header {
                  totalData
                  totalDataText
                  responseCode
                  keywordProcess
                  componentId
                }
                data {
                  suggestion {
                    currentKeyword
                    suggestion
                    text
                    query
                  }
                  ticker {
                    text
                    query
                  }
                  products {
                    id
                    name
                    shop {
                      id
                      name
                      url
                      city
                      isOfficial
                      isPowerBadge
                    }
                    freeOngkir {
                      isActive
                      imgUrl
                    }
                    url
                    imageUrl300
                    price
                    priceInt
                    originalPrice
                    discountPercentage
                    ratingAverage
                    badges {
                      title
                      imageUrl
                      show
                    }
                    count_sold
                    stock
                  }
                }
              }
            }
        """

        fun createParams(
            param: SearchParamUiModel,
        ): Map<String, Any> = mapOf(
            PARAMS to param.joinToString()
        )
    }
}