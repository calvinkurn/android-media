package com.tokopedia.review.feature.inbox.history.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistoryResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevFeedbackHistoryUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevFeedbackHistoryResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_SEARCH_QUERY = "searchQuery"
        const val PARAM_LIMIT = "limit"
        const val PARAM_PAGE = "page"
        const val HISTORY_QUERY_CLASS_NAME = "ReviewHistory"
        const val HISTORY_QUERY =
            """
                query productrevFeedbackHistory(${'$'}searchQuery: String, ${'$'}limit: Int!, ${'$'}page: Int!) {
                    productrevFeedbackHistory(searchQuery:${'$'}searchQuery, limit: ${'$'}limit, page: ${'$'}page) {
                      list {
                        product {
                          productID
                          productName
                          productVariantName
                        }
                        timestamp {
                          createTime
                          createTimeFormatted
                        }
                        status {
                          hasResponse
                          anonymous
                        }
                        review {
                          feedbackID
                          userName
                          rating
                          reviewText
                          attachmentsURL {
                            fullSize
                            thumbnail
                          }
                        }
                      }
                      hasNext
                    }
                }
            """
    }

    @GqlQuery(HISTORY_QUERY_CLASS_NAME, HISTORY_QUERY)
    fun setParams(searchQuery: String?, page: Int, limit: Int = ReviewInboxConstants.REVIEW_INBOX_DATA_PER_PAGE){
        setGraphqlQuery(ReviewHistory.GQL_QUERY)
        setTypeClass(ProductrevFeedbackHistoryResponseWrapper::class.java)
        setRequestParams(
                RequestParams.create().apply {
                    if(!searchQuery.isNullOrEmpty()) {
                        putString(PARAM_SEARCH_QUERY, searchQuery)
                    }
                    putInt(PARAM_LIMIT, limit)
                    putInt(PARAM_PAGE, page)
                }.parameters
        )
    }
}