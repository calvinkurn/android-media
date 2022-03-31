package com.tokopedia.review.feature.inboxreview.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetInboxReviewUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<InboxReviewResponse.ProductGetInboxReviewByShop>() {

    companion object {
        private const val FILTER_BY = "filterBy"
        private const val PAGE = "page"
        private const val LIMIT = "limit"
        private const val MAX_LIMIT = 15
        const val INBOX_REVIEW_QUERY_CLASS_NAME = "InboxReview"
        const val INBOX_REVIEW_QUERY = """
            query productrevGetInboxReviewByShop(${'$'}page: Int!, ${'$'}limit: Int!, ${'$'}filterBy: String!) {
              productrevGetInboxReviewByShop(page: ${'$'}page, limit: ${'$'}limit, filterBy: ${'$'}filterBy) {
                list {
                    feedbackIDStr
                    rating
                    user {
                        userIDStr
                        userName
                    }
                    product {
                        productIDStr
                        productName
                        productImageURL
                        productVariant {
                            variantID
                            variantName
                        }
                        productPageURL
                    }
                    attachments {
                      thumbnailURL
                      fullsizeURL
                    }
                    videoAttachments {
                      videoUrl
                    }
                    invoiceID
                    reviewText
                    reviewTime
                    replyText
                    replyTime
                    isAutoReply
                    isKejarUlasan
                    badRatingReasonFmt
                    ratingDisclaimer
                }
                filterBy
                remainder
                reviewCount
                limit
                page
                hasNext
                useAutoReply
              }
            }
        """

        @JvmStatic
        fun createParams(filterBy: String, page: Int): Map<String, Any> = mapOf(FILTER_BY to filterBy, LIMIT to MAX_LIMIT, PAGE to page)
    }

    var params = mapOf<String, Any>()

    @GqlQuery(INBOX_REVIEW_QUERY_CLASS_NAME, INBOX_REVIEW_QUERY)
    override suspend fun executeOnBackground(): InboxReviewResponse.ProductGetInboxReviewByShop {
        val gqlRequest = GraphqlRequest(InboxReview.GQL_QUERY, InboxReviewResponse::class.java, params)
        val gqlResponse = graphQlRepository.response(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<InboxReviewResponse>(InboxReviewResponse::class.java).productrevGetInboxReviewByShop
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }

}