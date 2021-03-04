package com.tokopedia.review.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevGetReviewDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevGetReviewDetailResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_FEEDBACK_ID = "feedbackID"
        const val REVIEW_DETAIL_QUERY_CLASS_NAME = "ReviewDetail"
        private const val REVIEW_DETAIL_QUERY =
            """
                query productrevGetReviewDetail(${'$'}feedbackID: Int!) {
                  productrevGetReviewDetail(feedbackID: ${'$'}feedbackID) {
                    product {
                      productID
                      productName
                      productPageURL
                      productImageURL
                      productVariantName
                    }
                    review {
                      feedbackID
                      rating
                      reviewText
                      reviewTimeFormatted
                      attachmentsURL {
                        fullSize
                        thumbnail
                      }
                      editable
                      reviewerName
                      sentAsAnonymous
                    }
                    response {
                      responseText
                      responseTimeFormatted
                      shopName
                      shopID
                    }
                    reputation {
                      reputationID
                      score
                      editable
                      lockTime
                      isLocked
                    }
                  }
                }
            """
    }

    @GqlQuery(REVIEW_DETAIL_QUERY_CLASS_NAME, REVIEW_DETAIL_QUERY)
    fun setRequestParams(feedbackID: Long) {
        setGraphqlQuery(ReviewDetail.GQL_QUERY)
        setTypeClass(ProductrevGetReviewDetailResponseWrapper::class.java)
        setRequestParams(
                RequestParams.create().apply {
                    putLong(PARAM_FEEDBACK_ID, feedbackID)
                }.parameters
        )
    }
}