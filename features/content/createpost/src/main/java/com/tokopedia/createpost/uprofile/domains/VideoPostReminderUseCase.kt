package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileDoFollowModelBase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.model.VideoPostReimderModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class VideoPostReminderUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun updateReminder(channelID: String, isActive: Boolean): VideoPostReimderModel {
        val request = GraphqlRequest(
            getQuery(),
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

    private fun getQuery(): String {
        return "mutation Play_playToggleChannelReminder(\$channelID: String, \$setActive: Boolean) {\n" +
                "  playToggleChannelReminder(input: {channelID: \$channelID, setActive: \$setActive}) {\n" +
                "    header {\n" +
                "      message\n" +
                "      status\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }
}