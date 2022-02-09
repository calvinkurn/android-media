package com.tokopedia.play.domain

import com.tokopedia.usecase.RequestParams

/**
 * @author by astidhiyaa on 09/02/22
 */
class CheckUpcomingCampaignReminderUseCase {
    companion object {
        private const val CAMPAIGN_ID = "campaign_id"
        private const val SOURCE_PARAM = "source"
        private const val SOURCE_PARAM_VALUE = "campaign"
        private const val REQUEST_PARAM = "params"
        const val QUERY_NAME = "CheckUpcomingCampaignReminderUseCaseQuery"
        const val QUERY = """
            query getStatusReminder(${'$'}$REQUEST_PARAM: GetCampaignNotifyMeRequest) {
              getCampaignNotifyMe($REQUEST_PARAM: ${'$'}$REQUEST_PARAM) {
                campaign_id
                success
                message
                error_message
                is_available
              }
            }
        """

        fun createParam(campaignId: String): RequestParams {
            val params = mapOf(
                CAMPAIGN_ID to campaignId,
                SOURCE_PARAM to SOURCE_PARAM_VALUE,
            )
            return RequestParams.create().apply {
                putObject(REQUEST_PARAM, params)
            }
        }
    }
}