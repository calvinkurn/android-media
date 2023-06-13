package com.tokopedia.feedplus.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.data.pojo.CheckUpcomingCampaign
import com.tokopedia.feedcomponent.data.pojo.UpcomingCampaignResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 04/04/23
 */
class FeedCampaignCheckReminderUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, UpcomingCampaignResponse>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): UpcomingCampaignResponse =
        graphqlRepository.request<Map<String, Any>, CheckUpcomingCampaign>(
            graphqlQuery(),
            params
        ).response

    override fun graphqlQuery(): String = """
            query getStatusReminder(${'$'}params: GetCampaignNotifyMeRequest) {
              getCampaignNotifyMe(params: ${'$'}params) {
                campaign_id
                success
                message
                error_message
                is_available
              }
            }
        """

    fun createParams(campaignId: Long): Map<String, Any> = mapOf(
        KEY_PARAM to mapOf(
            KEY_CAMPAIGN_ID to campaignId,
            KEY_SOURCE_PARAM to SOURCE_PARAM_VALUE
        )
    )

    companion object {
        private const val KEY_PARAM = "params"

        private const val KEY_CAMPAIGN_ID = "campaign_id"
        private const val KEY_SOURCE_PARAM = "source"

        private const val SOURCE_PARAM_VALUE = "campaign"
    }
}
