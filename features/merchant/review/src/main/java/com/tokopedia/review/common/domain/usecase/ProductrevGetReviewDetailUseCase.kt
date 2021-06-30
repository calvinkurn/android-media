package com.tokopedia.review.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(ProductrevGetReviewDetailUseCase.REVIEW_DETAIL_QUERY_CLASS_NAME, ProductrevGetReviewDetailUseCase.REVIEW_DETAIL_QUERY)
class ProductrevGetReviewDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevGetReviewDetailResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_FEEDBACK_ID = "feedbackID"
        const val REVIEW_DETAIL_QUERY_CLASS_NAME = "ReviewDetail"
        const val REVIEW_DETAIL_QUERY =
            """
                query productrevGetReviewDetailV2(${'$'}feedbackID: String!) {
                  productrevGetReviewDetailV2(feedbackID: ${'$'}feedbackID) {
                    product {
                      productIDStr
                      productName
                      productPageURL
                      productImageURL
                      productVariantName
                    }
                    review {
                      feedbackIDStr
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
                      reputationIDStr
                      score
                      editable
                      lockTime
                      isLocked
                    }
                  }
                }
            """
    }

    init {
        setGraphqlQuery(ReviewDetail.GQL_QUERY)
        setTypeClass(ProductrevGetReviewDetailResponseWrapper::class.java)
    }

    fun setRequestParams(feedbackID: String) {
        setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_FEEDBACK_ID, feedbackID)
                }.parameters
        )
    }
}