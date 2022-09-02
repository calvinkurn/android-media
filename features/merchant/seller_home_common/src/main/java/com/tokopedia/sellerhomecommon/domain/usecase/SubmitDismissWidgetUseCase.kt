package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.model.WidgetDismissWithFeedbackResponse

/**
 * Created by @ilhamsuaib on 02/09/22.
 * GQL Doc : https://tokopedia.atlassian.net/wiki/spaces/SHA/pages/2017823109/Tech+Plan+Feedback+Loop+Information+Dismissal#API-Contract
 */

@GqlQuery("SubmitDismissWidgetGqlMutation", SubmitDismissWidgetUseCase.QUERY)
class SubmitDismissWidgetUseCase(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<WidgetDismissWithFeedbackResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(SubmitDismissWidgetGqlMutation())
        setTypeClass(WidgetDismissWithFeedbackResponse::class.java)
    }

    companion object {
        internal const val QUERY = """
            mutation dashboardDismissWithFeedback(${'$'}action: String!, ${'$'}dismissKey: String!, ${'$'}dismissObjectIDList: [String!]!, ${'$'}dismissSign: String!, ${'$'}feedbackReason1: Boolean!, ${'$'}feedbackReason2: Boolean!, ${'$'}feedbackReason3: Boolean!, ${'$'}feedbackReasonOtherText: String!, ${'$'}feedbackWidgetIDParent: String!, ${'$'}shopID: Int!) {
              dashboardDismissWithFeedback(action: ${'$'}action, dismissKey: ${'$'}dismissKey, dismissObjectIDList: ${'$'}dismissObjectIDList, dismissSign: ${'$'}dismissSign, feedbackReason1: ${'$'}feedbackReason1, feedbackReason2: ${'$'}feedbackReason2, feedbackReason3: ${'$'}feedbackReason3, feedbackReasonOtherText: ${'$'}feedbackReasonOtherText, feedbackWidgetIDParent: ${'$'}feedbackWidgetIDParent, shopID: ${'$'}shopID, feedbackPositive: false, dismissToken: "") {
                error
                errorMsg
                dismissToken
              }
            }
        """
    }
}