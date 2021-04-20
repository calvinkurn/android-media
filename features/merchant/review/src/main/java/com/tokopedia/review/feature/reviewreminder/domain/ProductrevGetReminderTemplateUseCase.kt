package com.tokopedia.review.feature.reviewreminder.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderTemplateResponseWrapper
import javax.inject.Inject

class ProductrevGetReminderTemplateUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductrevGetReminderTemplateResponseWrapper>(graphqlRepository) {
    companion object {
        const val GET_REMINDER_TEMPLATE_QUERY_CLASS_NAME = "ReminderTemplate"
        const val GET_REMINDER_TEMPLATE_QUERY = """
            query productrevGetReminderTemplate {
              productrevGetReminderTemplate {
                template
              }
            }
        """
    }

    init {
        init()
    }

    @GqlQuery(GET_REMINDER_TEMPLATE_QUERY_CLASS_NAME, GET_REMINDER_TEMPLATE_QUERY)
    private fun init() {
        setTypeClass(ProductrevGetReminderTemplateResponseWrapper::class.java)
        setGraphqlQuery(ReminderTemplate.GQL_QUERY)
    }
}