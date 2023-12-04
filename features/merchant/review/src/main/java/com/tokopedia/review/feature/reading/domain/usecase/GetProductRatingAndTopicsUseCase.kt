package com.tokopedia.review.feature.reading.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reading.data.ProductRatingAndTopic
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetProductRatingAndTopicsUseCase.GET_PRODUCT_RATING_USE_CASE_CLASS_NAME, GetProductRatingAndTopicsUseCase.GET_PRODUCT_RATING_QUERY)
class GetProductRatingAndTopicsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductRatingAndTopic>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_PAGE_SOURCE = "pageSource"
        const val PARAM_FILTER_BY = "filterBy"
        const val GET_PRODUCT_RATING_USE_CASE_CLASS_NAME = "ProductRatingQuery"
        const val GET_PRODUCT_RATING_QUERY = """
            query productrevGetProductRatingAndTopics(${'$'}productID: String!, ${'$'}pageSource: String!, ${'$'}filterBy: String!) {
              productrevGetProductRatingAndTopics(productID: ${'$'}productID, pageSource: ${'$'}pageSource, filterBy: ${'$'}filterBy) {
                rating {
                  positivePercentageFmt
                  ratingScore
                  totalRating
                  totalRatingWithImage
                  totalRatingTextAndImage
                  totalRatingFmt
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
                  show
                }
                variants
                availableFilters {
                  withAttachment
                  rating
                  topics
                  helpfulness
                  variant
                }
                keywords {
                  text
                  count
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

    fun setParams(productId: String, pageSource: String = "", filterBy: String = "") {
        requestParams.putString(PARAM_PRODUCT_ID, productId)
        requestParams.putString(PARAM_PAGE_SOURCE, pageSource)
        requestParams.putString(PARAM_FILTER_BY, filterBy)
        setRequestParams(requestParams.parameters)
    }
}
