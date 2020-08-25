package com.tokopedia.review.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevGetReviewDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevGetReviewDetailResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_FEEDBACK_ID = "feedbackID"
        private val query by lazy {
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
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductrevGetReviewDetailResponseWrapper::class.java)
    }

    fun setRequestParams(feedbackID: Int) {
        setRequestParams(
                RequestParams.create().apply {
                    putInt(PARAM_FEEDBACK_ID, feedbackID)
                }.parameters
        )
    }
}