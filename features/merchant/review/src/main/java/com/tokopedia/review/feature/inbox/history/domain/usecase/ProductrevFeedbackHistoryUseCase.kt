package com.tokopedia.review.feature.inbox.history.domain.usecase

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
        private val query by lazy {
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
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductrevFeedbackHistoryResponseWrapper::class.java)
    }

    fun setParams(searchQuery: String?, page: Int, limit: Int = ReviewInboxConstants.REVIEW_INBOX_DATA_PER_PAGE){
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