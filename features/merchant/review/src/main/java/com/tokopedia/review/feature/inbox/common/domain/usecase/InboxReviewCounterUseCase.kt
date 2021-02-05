package com.tokopedia.review.feature.inbox.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inbox.common.domain.response.InboxReviewTabCounterResponse
import javax.inject.Inject

class InboxReviewCounterUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<InboxReviewTabCounterResponse>(graphqlRepository) {

    companion object {
        private const val QUERY = """
            query productrevReviewTabCounter {
              productrevReviewTabCounter{
                list {
                  count
                  tabName
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(InboxReviewTabCounterResponse::class.java)
    }
}