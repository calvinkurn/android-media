package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.model.UserProfileIsFollow
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class UserDetailsUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun getUserProfileDetail(userName: String, profileId: MutableList<String>): GraphqlResponse {
        val request = GraphqlRequest(
            getQuery(),
            ProfileHeaderBase::class.java,
            getRequestParams(userName)
        )

        val request2 = GraphqlRequest(
            getQueryTheyFollowed(),
            UserProfileIsFollow::class.java,
            getRequestParams(profileId)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        useCase.addRequest(request2)
        return useCase.executeOnBackground()
    }

    private fun getRequestParams(userName: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_USERNAME] = userName
        return requestMap
    }

    companion object {
        const val KEY_USERNAME = "userName"
    }

    private fun getQuery(): String {
        return "query ProfileHeader(\$userName: String!) {\n" +
                "  feedXProfileHeader(username:\$userName) {\n" +
                "    profile {\n" +
                "      userID\n" +
                "      encryptedUserID\n" +
                "      imageCover\n" +
                "      name\n" +
                "      username\n" +
                "      biography\n" +
                "      sharelink {\n" +
                "        weblink\n" +
                "        applink\n" +
                "      }\n" +
                "      badges\n" +
                "      liveplaychannel {\n" +
                "        islive\n" +
                "        liveplaychannelid\n" +
                "        liveplaychannellink {\n" +
                "          applink\n" +
                "          weblink\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    stats {\n" +
                "      totalPost\n" +
                "      totalPostFmt\n" +
                "      totalFollower\n" +
                "      totalFollowerFmt\n" +
                "      totalFollowing\n" +
                "      totalFollowingFmt\n" +
                "    }\n" +
                "    hasAcceptTnC\n" +
                "    shouldSeoIndex\n" +
                "  }\n" +
                "}"
    }

    private fun getQueryTheyFollowed(): String {
        return "query ProfileIsFollowing(\$userIds: [String!]!) {\n" +
                "  feedXProfileIsFollowing(followingUserIDs: \$userIds) {\n" +
                "    isUserFollowing {\n" +
                "      userID\n" +
                "      encryptedUserID\n" +
                "      status\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }

    private fun getRequestParams(profileIds: MutableList<String>): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[ProfileTheyFollowedUseCase.KEY_USERIDS] = profileIds
        return requestMap
    }
}