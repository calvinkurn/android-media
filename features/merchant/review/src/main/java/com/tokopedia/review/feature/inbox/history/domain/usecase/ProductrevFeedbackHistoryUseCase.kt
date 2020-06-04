package com.tokopedia.review.feature.inbox.history.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistoryResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevFeedbackHistoryUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevFeedbackHistoryResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_FILTER = "filterBy"
        const val PARAM_SORT = "sortBy"
        const val PARAM_LIMIT = "limit"
        const val PARAM_PAGE = "page"
        private val query by lazy {
            """
                query productrevFeedbackHistory(${'$'}filterBy: String, ${'$'}sortBy: String, ${'$'}limit: Int!, ${'$'}page: Int!) {
                    productrevFeedbackHistory(filterBy: ${'$'}filterBy, sortBy: ${'$'}sortBy, limit: ${'$'}limit, page: ${'$'}page) {
                      list {
                        reputationID
                        product {
                          productID
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
                          editable
                        }
                        review {
                          rating
                          reviewText
                        }
                        user {
                          name
                          isAnonym
                        }
                      }
                      filterBy
                      sortBy
                      page
                      limit
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

    fun setParams(filter: String, sort: String, limit: Int, page: Int){
        setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_FILTER, filter)
                    putString(PARAM_SORT, sort)
                    putInt(PARAM_LIMIT, limit)
                    putInt(PARAM_PAGE, page)
                }.parameters
        )
    }
}