package com.tokopedia.review.feature.inbox.container.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.container.data.InboxReviewReputationListResponseWrapper
import javax.inject.Inject

class InboxReviewReputationClearUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<InboxReviewReputationListResponseWrapper>(graphqlRepository) {

    companion object {
        private val query by lazy {
            """
                {
                  inboxReviewReputationList(page: 1, perPage: 10, role: 1, timeFilter: 4, status: 1) {
                                    hasNext
                                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(InboxReviewReputationListResponseWrapper::class.java)
    }
}