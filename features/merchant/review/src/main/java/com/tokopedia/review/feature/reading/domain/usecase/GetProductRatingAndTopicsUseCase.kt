package com.tokopedia.review.feature.reading.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reading.data.ProductRatingAndTopic
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetProductRatingAndTopicsUseCase.GET_PRODUCT_RATING_USE_CASE_CLASS_NAME, GetProductRatingAndTopicsUseCase.GET_PRODUCT_RATING_QUERY)
class GetProductRatingAndTopicsUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<ProductRatingAndTopic>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val GET_PRODUCT_RATING_USE_CASE_CLASS_NAME = "ProductRatingQuery"
        const val GET_PRODUCT_RATING_QUERY = """
            query productrevGetProductRatingAndTopics(${'$'}productID: String!){
              productrevGetProductRatingAndTopics(productID: ${'$'}productID){
                rating {
                  positivePercentageFmt
                  ratingScore
                  totalRating
                  totalRatingWithImage
                  totalRatingTextAndImage
                  detail {
                    rate
                    totalReviews
                    percentage
                  }
                }
                topics {
                  rating
                  formatted
                  reviewCount
                }
                variants
                availableFilters {
                  withAttachment
                  rating
                  topics
                  helpfulness
                  variant
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(ProductRatingQuery.GQL_QUERY)
        setTypeClass(ProductRatingAndTopic::class.java)
    }

    private val requestParams = RequestParams.create()

    fun setParams(productId: String) {
        requestParams.putString(PARAM_PRODUCT_ID, productId)
        setRequestParams(requestParams.parameters)
    }
}