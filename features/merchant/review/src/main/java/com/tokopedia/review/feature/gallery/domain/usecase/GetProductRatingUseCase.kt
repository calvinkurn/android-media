package com.tokopedia.review.feature.gallery.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.gallery.data.ProductReviewRatingResponse
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetProductRatingUseCase.GET_PRODUCT_RATING_USE_CASE_CLASS_NAME, GetProductRatingUseCase.GET_PRODUCT_RATING_QUERY)
class GetProductRatingUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProductReviewRatingResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val GET_PRODUCT_RATING_USE_CASE_CLASS_NAME = "ProductRatingQuery"
        const val GET_PRODUCT_RATING_QUERY = """
            query productrevGetProductRating(${'$'}productID: String!) {
              productrevGetProductRating(productID: ${'$'}productID) {
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
            }
        """
    }

    init {
        setGraphqlQuery(ProductRatingQuery.GQL_QUERY)
        setTypeClass(ProductReviewRatingResponse::class.java)
    }

    private val requestParams = RequestParams.create()

    fun setParams(productId: String) {
        requestParams.putString(GetProductRatingAndTopicsUseCase.PARAM_PRODUCT_ID, productId)
        setRequestParams(requestParams.parameters)
    }
}