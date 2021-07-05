package com.tokopedia.review.feature.reviewreminder.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.reviewreminder.data.ProductrevSendReminderResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevSendReminderUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductrevSendReminderResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TEMPLATE = "template"
        const val SEND_REMINDER_QUERY_CLASS_NAME = "SendReminder"
        const val SEND_REMINDER_MUTATION = """
            mutation productrevSendReminder(${'$'}template: String!) {
              productrevSendReminder(template: ${'$'}template) {
                success
              }
            }
        """
    }

    init {
        init()
    }

    @GqlQuery(SEND_REMINDER_QUERY_CLASS_NAME, SEND_REMINDER_MUTATION)
    private fun init() {
        setTypeClass(ProductrevSendReminderResponseWrapper::class.java)
        setGraphqlQuery(SendReminder.GQL_QUERY)
    }

    fun setParams(template: String) {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_TEMPLATE, template)
        }.parameters)
    }
}