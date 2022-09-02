package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhomecommon.domain.model.WidgetDismissWithFeedbackResponse
import com.tokopedia.sellerhomecommon.presentation.model.SubmitWidgetDismissUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 02/09/22.
 * GQL Doc : https://tokopedia.atlassian.net/wiki/spaces/SHA/pages/2017823109/Tech+Plan+Feedback+Loop+Information+Dismissal#API-Contract
 */

@GqlQuery("SubmitWidgetDismissalGqlMutation", SubmitWidgetDismissUseCase.QUERY)
class SubmitWidgetDismissUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<WidgetDismissWithFeedbackResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(SubmitWidgetDismissalGqlMutation())
        setTypeClass(WidgetDismissWithFeedbackResponse::class.java)
    }

    companion object {
        private const val ACTION = "action"
        private const val DISMISS_KEY = "dismissKey"
        private const val DISMISS_OBJECT_ID_LIST = "dismissObjectIDList"
        private const val DISMISS_SIGN = "dismissSign"
        private const val FEEDBACK_REASON_1 = "feedbackReason1"
        private const val FEEDBACK_REASON_2 = "feedbackReason2"
        private const val FEEDBACK_REASON_3 = "feedbackReason3"
        private const val FEEDBACK_REASON_OTHER = "feedbackReasonOtherText"
        private const val FEEDBACK_ID_PARENT = "feedbackWidgetIDParent"
        private const val SHOP_ID = "shopID"

        internal const val QUERY = """
            mutation dashboardDismissWithFeedback($$ACTION: String!, $$DISMISS_KEY: String!, $$DISMISS_OBJECT_ID_LIST: [String!]!, $$DISMISS_SIGN: String!, $$FEEDBACK_REASON_1: Boolean!, $$FEEDBACK_REASON_2: Boolean!, $$FEEDBACK_REASON_3: Boolean!, $$FEEDBACK_REASON_OTHER: String!, $$FEEDBACK_ID_PARENT: String!, $$SHOP_ID: Int!) {
              dashboardDismissWithFeedback(action: $$ACTION, dismissKey: $$DISMISS_KEY, dismissObjectIDList: $DISMISS_OBJECT_ID_LIST, dismissSign: $$DISMISS_SIGN, feedbackReason1: $$FEEDBACK_REASON_1, feedbackReason2: $$FEEDBACK_REASON_2, feedbackReason3: $FEEDBACK_REASON_3, feedbackReasonOtherText: $$FEEDBACK_REASON_OTHER, feedbackWidgetIDParent: $$FEEDBACK_ID_PARENT, shopID: $$SHOP_ID, feedbackPositive: false, dismissToken: "") {
                error
                errorMsg
                dismissToken
              }
            }
        """

        fun createParam(data: SubmitWidgetDismissUiModel): RequestParams {
            return RequestParams().apply {
                putString(ACTION, data.action.name)
                putString(DISMISS_KEY, data.dismissKey)
                putObject(DISMISS_OBJECT_ID_LIST, data.dismissObjectIDs)
                putString(DISMISS_SIGN, data.dismissSign)
                putBoolean(FEEDBACK_REASON_1, data.feedbackReason1)
                putBoolean(FEEDBACK_REASON_2, data.feedbackReason2)
                putBoolean(FEEDBACK_REASON_3, data.feedbackReason3)
                putString(FEEDBACK_REASON_OTHER, data.feedbackReasonOther)
                putString(FEEDBACK_ID_PARENT, data.feedbackWidgetIDParent)
                putLong(SHOP_ID, data.shopId.toLongOrZero())
            }
        }
    }
}