package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.SubmitWidgetDismissalMapper
import com.tokopedia.sellerhomecommon.domain.model.WidgetDismissWithFeedbackResponse
import com.tokopedia.sellerhomecommon.presentation.model.SubmitWidgetDismissUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetDismissalResultUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 02/09/22.
 * GQL Doc : https://tokopedia.atlassian.net/wiki/spaces/SHA/pages/2017823109/Tech+Plan+Feedback+Loop+Information+Dismissal#API-Contract
 */

@GqlQuery("SubmitWidgetDismissalGqlMutation", SubmitWidgetDismissUseCase.QUERY)
class SubmitWidgetDismissUseCase @Inject constructor(
    private val mapper: SubmitWidgetDismissalMapper,
    private val gqlRepository: GraphqlRepository
) : UseCase<WidgetDismissalResultUiModel>() {

    private var param: SubmitWidgetDismissUiModel = SubmitWidgetDismissUiModel()

    override suspend fun executeOnBackground(): WidgetDismissalResultUiModel {
        val params = getRequestParam(param)
        val typeClass = WidgetDismissWithFeedbackResponse::class.java
        val gqlRequest = GraphqlRequest(
            SubmitWidgetDismissalGqlMutation(),
            typeClass,
            params.parameters
        )
        val response = gqlRepository.response(listOf(gqlRequest))
        val result = response.getData<WidgetDismissWithFeedbackResponse>(typeClass)
        if (result.data.isError) {
            throw MessageErrorException(result.data.errorMsg)
        } else {
            return mapper.mapRemoteModelToUiModel(param, result)
        }
    }

    suspend fun execute(param: SubmitWidgetDismissUiModel): WidgetDismissalResultUiModel {
        this.param = param
        return executeOnBackground()
    }

    private fun getRequestParam(data: SubmitWidgetDismissUiModel): RequestParams {
        return RequestParams().apply {
            putString(ACTION, data.action.actionName)
            putString(DISMISS_KEY, data.dismissKey)
            putObject(DISMISS_OBJECT_ID_LIST, data.dismissObjectIDs)
            putString(DISMISS_SIGN, data.dismissSign)
            putString(DISMISS_TOKEN, data.dismissToken)
            putBoolean(FEEDBACK_REASON_1, data.feedbackReason1)
            putBoolean(FEEDBACK_REASON_2, data.feedbackReason2)
            putBoolean(FEEDBACK_REASON_3, data.feedbackReason3)
            putString(FEEDBACK_REASON_OTHER, data.feedbackReasonOther)
            putString(FEEDBACK_ID_PARENT, data.feedbackWidgetIDParent)
            putLong(SHOP_ID, data.shopId.toLongOrZero())
            putBoolean(POSITIVE_FEEDBACK, data.isFeedbackPositive)
        }
    }

    companion object {
        private const val ACTION = "action"
        private const val DISMISS_KEY = "dismissKey"
        private const val DISMISS_OBJECT_ID_LIST = "dismissObjectIDList"
        private const val DISMISS_SIGN = "dismissSign"
        private const val DISMISS_TOKEN = "dismissToken"
        private const val FEEDBACK_REASON_1 = "feedbackReason1"
        private const val FEEDBACK_REASON_2 = "feedbackReason2"
        private const val FEEDBACK_REASON_3 = "feedbackReason3"
        private const val FEEDBACK_REASON_OTHER = "feedbackReasonOtherText"
        private const val FEEDBACK_ID_PARENT = "feedbackWidgetIDParent"
        private const val SHOP_ID = "shopID"
        private const val POSITIVE_FEEDBACK = "feedbackPositive"
        private const val NULL_PARAM_ERROR = "Parameter is null, please invoke execute(...) method"

        internal const val QUERY = """
            mutation dashboardDismissWithFeedback($$ACTION: String!, $$DISMISS_KEY: String!, $$DISMISS_OBJECT_ID_LIST: [String!]!, $$DISMISS_SIGN: String!, $$FEEDBACK_REASON_1: Boolean!, $$FEEDBACK_REASON_2: Boolean!, $$FEEDBACK_REASON_3: Boolean!, $$FEEDBACK_REASON_OTHER: String!, $$FEEDBACK_ID_PARENT: String!, $$SHOP_ID: Int!, $$DISMISS_TOKEN: String!, $$POSITIVE_FEEDBACK: Boolean!) {
              dashboardDismissWithFeedback($ACTION: $$ACTION, $DISMISS_KEY: $$DISMISS_KEY, $DISMISS_OBJECT_ID_LIST: $$DISMISS_OBJECT_ID_LIST, $DISMISS_SIGN: $$DISMISS_SIGN, $FEEDBACK_REASON_1: $$FEEDBACK_REASON_1, $FEEDBACK_REASON_2: $$FEEDBACK_REASON_2, $FEEDBACK_REASON_3: $$FEEDBACK_REASON_3, $FEEDBACK_REASON_OTHER: $$FEEDBACK_REASON_OTHER, $FEEDBACK_ID_PARENT: $$FEEDBACK_ID_PARENT, $SHOP_ID: $$SHOP_ID, $DISMISS_TOKEN: $$DISMISS_TOKEN, $POSITIVE_FEEDBACK: $$POSITIVE_FEEDBACK) {
                error
                errorMsg
                dismissToken
              }
            }
        """
    }
}
