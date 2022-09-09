package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.pojo.PostUpcomingCampaign
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by shruti on 30/08/22
 */

@GqlQuery(PostUpcomingCampaignReminderUseCase.QUERY_NAME, PostUpcomingCampaignReminderUseCase.QUERY)
class PostUpcomingCampaignReminderUseCase@Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PostUpcomingCampaign>(graphqlRepository) {

    init {
        setGraphqlQuery(PostUpcomingCampaignReminderUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PostUpcomingCampaign::class.java)
    }

    companion object {
        private const val CAMPAIGN_ID = "campaign_id"
        private const val ACTION_PARAM = "action"
        private const val ACTION_REGISTER_PARAM_VALUE = "REGISTER"
        private const val ACTION_UNREGISTER_PARAM_VALUE = "UNREGISTER"
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

        fun createParam(campaignId: Long, reminderType: FeedASGCUpcomingReminderStatus): RequestParams {
            val params = mapOf(
                CAMPAIGN_ID to campaignId,
                ACTION_PARAM to if(reminderType is FeedASGCUpcomingReminderStatus.On) ACTION_UNREGISTER_PARAM_VALUE else ACTION_REGISTER_PARAM_VALUE,
                SOURCE_PARAM to SOURCE_PARAM_VALUE,
                REQUEST_TYPE_PARAM to REQUEST_TYPE_PARAM_VALUE
            )
            return RequestParams.create().apply {
                putObject(REQUEST_PARAM, params)
            }
        }
    }
}