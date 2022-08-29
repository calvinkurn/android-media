package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.CheckUpcomingCampaign
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/02/22
 */

@GqlQuery(CheckUpcomingCampaignReminderUseCase.QUERY_NAME, CheckUpcomingCampaignReminderUseCase.QUERY)
class CheckUpcomingCampaignReminderUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<CheckUpcomingCampaign>(graphqlRepository){

    init {
        setGraphqlQuery(CheckUpcomingCampaignReminderUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(CheckUpcomingCampaign::class.java)
    }

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

        fun createParam(campaignId: Long): RequestParams {
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