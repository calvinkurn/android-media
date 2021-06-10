package com.tokopedia.review.feature.reviewreminder.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounterResponseWrapper
import javax.inject.Inject

class ProductrevGetReminderCounterUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductrevGetReminderCounterResponseWrapper>(graphqlRepository) {

    companion object {
        const val GET_REMINDER_COUNTER_QUERY_CLASS_NAME = "ReminderCounter"
        const val GET_REMINDER_COUNTER_QUERY = """
            query productrevGetReminderCounter {
              productrevGetReminderCounter {
                totalProduct
                totalBuyer
                isWhitelisted
              }
            }
        """
    }

    init {
        init()
    }

    @GqlQuery(GET_REMINDER_COUNTER_QUERY_CLASS_NAME, GET_REMINDER_COUNTER_QUERY)
    private fun init() {
        setTypeClass(ProductrevGetReminderCounterResponseWrapper::class.java)
        setGraphqlQuery(ReminderCounter.GQL_QUERY)
    }
}