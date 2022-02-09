package com.tokopedia.play.domain

import com.tokopedia.usecase.RequestParams

/**
 * @author by astidhiyaa on 09/02/22
 */
class PostUpcomingCampaignReminderUseCase {
    companion object {
        private const val CAMPAIGN_ID = "campaign_id"
        private const val ACTION_PARAM = "action"
        private const val ACTION_PARAM_VALUE = "REGISTER"
        private const val SOURCE_PARAM = "source"
        private const val SOURCE_PARAM_VALUE = "campaign"
        private const val REQUEST_TYPE_PARAM = "requestType"
        private const val REQUEST_TYPE_PARAM_VALUE = "CAMPAIGN"
        private const val REQUEST_PARAM = "params"
        const val QUERY_NAME = "PostUpcomingCampaignReminderUseCaseQuery"
        const val QUERY = """
            mutation checkReminder(${'$'}$REQUEST_PARAM: CheckCampaignNotifyMeRequest) {
              checkCampaignNotifyMe($REQUEST_PARAM: ${'$'}$REQUEST_PARAM) {
                campaign_id
                success
                message
                error_message
              }
            }
        """

        fun createParam(campaignId: String): RequestParams {
            val params = mapOf(
                CAMPAIGN_ID to campaignId,
                ACTION_PARAM to ACTION_PARAM_VALUE,
                SOURCE_PARAM to SOURCE_PARAM_VALUE,
                REQUEST_TYPE_PARAM to REQUEST_TYPE_PARAM_VALUE
            )
            return RequestParams.create().apply {
                putObject(REQUEST_PARAM, params)
            }
        }
    }
}