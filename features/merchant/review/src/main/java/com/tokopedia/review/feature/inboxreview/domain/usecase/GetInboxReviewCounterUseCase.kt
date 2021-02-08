package com.tokopedia.review.feature.inboxreview.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.inboxreview.domain.response.InboxReviewTabCounterResponse
import com.tokopedia.review.feature.inboxreview.domain.usecase.GetInboxReviewCounterUseCase.Companion.INBOX_REVIEW_COUNTER_QUERY
import com.tokopedia.review.feature.inboxreview.domain.usecase.GetInboxReviewCounterUseCase.Companion.INBOX_REVIEW_COUNTER_QUERY_CLASS_NAME
import javax.inject.Inject

@GqlQuery(INBOX_REVIEW_COUNTER_QUERY_CLASS_NAME, INBOX_REVIEW_COUNTER_QUERY)
class GetInboxReviewCounterUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<InboxReviewTabCounterResponse>(graphqlRepository) {

    companion object {
        const val INBOX_REVIEW_COUNTER_QUERY_CLASS_NAME = "InboxReviewCounter"
        const val INBOX_REVIEW_COUNTER_QUERY = """
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
        setGraphqlQuery(InboxReviewCounter.GQL_QUERY)
        setTypeClass(InboxReviewTabCounterResponse::class.java)
    }
}