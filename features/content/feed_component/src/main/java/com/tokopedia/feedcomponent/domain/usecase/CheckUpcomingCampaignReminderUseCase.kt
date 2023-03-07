package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.pojo.CheckUpcomingCampaign
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by shruti on 30/08/22
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

        fun createParam(campaignId: Long): Map<String, Any> {
            return mapOf<String, Any>(
                CAMPAIGN_ID to campaignId,
                SOURCE_PARAM to SOURCE_PARAM_VALUE,
            )
        }
    }
}