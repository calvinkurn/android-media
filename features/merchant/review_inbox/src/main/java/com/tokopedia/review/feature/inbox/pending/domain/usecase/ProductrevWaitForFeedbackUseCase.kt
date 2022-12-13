package com.tokopedia.review.feature.inbox.pending.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackLabelAndImage
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevWaitForFeedbackUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProductrevWaitForFeedbackResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_LIMIT = "limit"
        const val PARAM_PAGE = "page"
        const val WAIT_FOR_FEEDBACK_QUERY_CLASS_NAME = "WaitForFeedback"
        const val WAIT_FOR_FEEDBACK_QUERY =
            """
                query productrevWaitForFeedback(${'$'}limit: Int!, ${'$'}page: Int!) {
                    productrevWaitForFeedback(limit: ${'$'}limit, page: ${'$'}page) {
                      list {
                        reputationIDStr
                        inboxReviewIDStr
                        product {
                          productIDStr
                          productName
                          productImageURL
                          productVariantName
                        }
                        timestamp {
                          createTime
                          createTimeFormatted
                        }
                        status {
                          seen
                          isEligible
                          incentiveLabel
                        }
                      }
                      page
                      limit
                      hasNext
                      state {
                        labelTitle
                        labelSubtitle
                        imageURL
                      }
                      banners {
                        labelTitle
                        labelSubtitle
                        imageURL
                        applink
                      }
                    }
                }
            """
    }

    override suspend fun executeOnBackground(): ProductrevWaitForFeedbackResponseWrapper {
        return super.executeOnBackground().run {
            copy(
                productrevWaitForFeedbackWaitForFeedback = productrevWaitForFeedbackWaitForFeedback.copy(
                    banners = productrevWaitForFeedbackWaitForFeedback.banners.plus(
                        ProductrevWaitForFeedbackLabelAndImage(
                            labelTitle = "Lebih cepat ulas sekaligus ✨",
                            labelSubtitle = "Yuk, lihat 10 barang ini & mulai ulas!",
                            imageURL = "https://thumb.viva.co.id/media/frontend/thumbs3/2022/07/06/62c503d49d9ef-thor-love-and-thunder_1265_711.jpg",
                            appLink = "tokopedia://product-review/bulk-create/"
                        )
                    )
                )
            )
        }
    }

    @GqlQuery(WAIT_FOR_FEEDBACK_QUERY_CLASS_NAME, WAIT_FOR_FEEDBACK_QUERY)
    fun setParams(limit: Int = ReviewInboxConstants.REVIEW_INBOX_DATA_PER_PAGE, page: Int) {
        setGraphqlQuery(WaitForFeedback.GQL_QUERY)
        setTypeClass(ProductrevWaitForFeedbackResponseWrapper::class.java)
        setRequestParams(
            RequestParams.create().apply {
                putInt(PARAM_LIMIT, limit)
                putInt(PARAM_PAGE, page)
            }.parameters
        )
    }
}
