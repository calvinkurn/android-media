package com.tokopedia.review.feature.historydetails.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.historydetails.data.ProductrevGetReviewDetailResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevGetReviewDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevGetReviewDetailResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_FEEDBACK_ID = "feedbackID"
        const val PARAM_REPUTATION_ID = "reputationID"
        private val query by lazy {
            """
                query productrevGetReviewDetail(${'$'}feedbackID: Integer!, ${'$'}reputationID: Integer!) {
                  productrevGetReviewDetail(feedbackID:${'$'}feedbackID, reputationID:${'$'}reputationID) {
                    product {
                      productID
                      productName
                      productImageURL
                      productVariantName
                    }
                    review {
                      feedbackID
                      rating
                      reviewText
                      reviewTime
                      reviewTimeFormatted
                      attachmentsURL
                      editable
                      reviewerData {
                        isAnonym
                        fullName
                      }
                    }
                    response {
                      responseText
                      responseTime
                      responseTimeFormatted
                      responderData {
                        shopName
                      }
                    }
                    reputation {
                      reputationID
                      score
                      editable
                      lockTime
                      lockTimeFormatted
                    }
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductrevGetReviewDetailResponseWrapper::class.java)
    }

    fun setRequestParams(feedbackID: Int, reputationID: Int) {
        setRequestParams(
                RequestParams.create().apply {
                    putInt(PARAM_FEEDBACK_ID, feedbackID)
                    putInt(PARAM_REPUTATION_ID, reputationID)
                }.parameters
        )
    }
}