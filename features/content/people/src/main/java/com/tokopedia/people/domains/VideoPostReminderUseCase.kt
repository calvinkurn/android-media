package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.people.model.VideoPostReimderModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

const val CHANNEL_REMINDER = """
    mutation Play_playToggleChannelReminder(\${'$'}channelID: String, \${'$'}setActive: Boolean) {
                playToggleChannelReminder(input: {channelID: \${'$'}channelID, setActive: \${'$'}setActive}) {
                  header {
                    message
                    status
                  }
                }
                }
"""

@GqlQuery("ChannelReminder", CHANNEL_REMINDER)
class VideoPostReminderUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun updateReminder(channelID: String, isActive: Boolean): VideoPostReimderModel {
        val request = GraphqlRequest(
            ChannelReminder.GQL_QUERY,
            VideoPostReimderModel::class.java,
            getRequestParams(channelID,isActive)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(VideoPostReimderModel::class.java)
    }

    private fun getRequestParams(
        followingUserIdEnc: String,
        isActive: Boolean
    ): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_CHANNEL_ID] = followingUserIdEnc
        requestMap[KEY_STATUS] = isActive
        return requestMap
    }

    companion object {
        const val KEY_CHANNEL_ID = "channelID"
        const val KEY_STATUS = "setActive"
    }
}
