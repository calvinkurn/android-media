package com.tokopedia.review.feature.reviewreminder.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderStatsResponseWrapper
import javax.inject.Inject

class ProductrevGetReminderStatsUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<ProductrevGetReminderStatsResponseWrapper>(graphqlRepository) {

    companion object {
        const val GET_REMINDER_STATS_QUERY_CLASS_NAME = "ReminderStats"
        const val GET_REMINDER_STATS_QUERY = """
            query productrevGetReminderStats {
              productrevGetReminderStats {
                timeRange
                totalReminderStats
                lastReminderTime
                lastReminderStats
                reviewPercentage
              }
            }
        """
    }

    init {
        init()
    }

    @GqlQuery(GET_REMINDER_STATS_QUERY_CLASS_NAME, GET_REMINDER_STATS_QUERY)
    private fun init() {
        setTypeClass(ProductrevGetReminderStatsResponseWrapper::class.java)
        setGraphqlQuery(ReminderStats.GQL_QUERY)
    }
}