package com.tokopedia.review.feature.reading.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reading.data.ShopRatingAndTopic
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(
        GetShopRatingAndTopicsUseCase.GET_SHOP_RATING_USE_CASE_CLASS_NAME,
        GetShopRatingAndTopicsUseCase.GET_SHOP_RATING_QUERY
)
class GetShopRatingAndTopicsUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<ShopRatingAndTopic>(graphqlRepository) {

    companion object {
        const val PARAM_SHOP_ID = "shopID"
        const val GET_SHOP_RATING_USE_CASE_CLASS_NAME = "ShopRatingQuery"
        const val GET_SHOP_RATING_QUERY = """
            query productrevGetShopRatingAndTopics(${'$'}shopID: String!) {
              productrevGetShopRatingAndTopics(shopID: ${'$'}shopID) {
                rating {
                  positivePercentageFmt
                  ratingScore
                  totalRating
                  totalRatingFmt
                  totalRatingTextAndImage
                  totalRatingTextAndImageFmt
                  detail {
                    rate
                    totalReviews
                    percentageFloat
                    formattedTotalReviews
                  }
                }
                topics {
                  rating
                  formatted
                  reviewCount
                  key
                }
                availableFilters {
                  withAttachment
                  rating
                  topics
                  helpfulness
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(ShopRatingQuery.GQL_QUERY)
        setTypeClass(ShopRatingAndTopic::class.java)
    }

    private val requestParams = RequestParams.create()

    fun setParams(shopId: String) {
        requestParams.putString(PARAM_SHOP_ID, shopId)
        setRequestParams(requestParams.parameters)
    }
}