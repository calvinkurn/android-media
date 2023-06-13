package com.tokopedia.feedplus.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.data.pojo.PostUpcomingCampaign
import com.tokopedia.feedcomponent.data.pojo.UpcomingCampaignResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 04/04/23
 */
class FeedCampaignReminderUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, UpcomingCampaignResponse>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): UpcomingCampaignResponse =
        graphqlRepository.request<Map<String, Any>, PostUpcomingCampaign>(
            graphqlQuery(),
            params
        ).response

    override fun graphqlQuery(): String = """
            mutation checkReminder(${'$'}params: CheckCampaignNotifyMeRequest) {
              checkCampaignNotifyMe(params: ${'$'}params) {
                campaign_id
                success
                message
                error_message
              }
            }
        """

    fun createParams(campaignId: Long, setReminder: Boolean): Map<String, Any> =
        mapOf(
            KEY_PARAM to mapOf(
                KEY_CAMPAIGN_ID to campaignId,
                KEY_ACTION_PARAM to if (setReminder) ACTION_SET_REMINDER else ACTION_UNSET_REMINDER,
                KEY_SOURCE_PARAM to SOURCE_PARAM_VALUE,
                KEY_REQUEST_TYPE_PARAM to REQUEST_TYPE_PARAM_VALUE
            )
        )

    companion object {
        private const val KEY_PARAM = "params"

        private const val KEY_CAMPAIGN_ID = "campaign_id"
        private const val KEY_ACTION_PARAM = "action"
        private const val KEY_SOURCE_PARAM = "source"
        private const val KEY_REQUEST_TYPE_PARAM = "requestType"

        private const val ACTION_SET_REMINDER = "REGISTER"
        private const val ACTION_UNSET_REMINDER = "UNREGISTER"
        private const val SOURCE_PARAM_VALUE = "campaign"
        private const val REQUEST_TYPE_PARAM_VALUE = "CAMPAIGN"
    }
}
