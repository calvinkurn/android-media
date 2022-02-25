package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.model.ProfileIsFollowing
import com.tokopedia.createpost.uprofile.model.UserProfileIsFollow
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class ProfileTheyFollowedUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun profileIsFollowing(profileIds: MutableList<String>) : UserProfileIsFollow {
            val request = GraphqlRequest(getQuery(),
                UserProfileIsFollow::class.java,
                getRequestParams(profileIds))

        useCase.clearRequest()
            useCase.addRequest(request)
            val response = useCase.executeOnBackground()
            return response.getData(UserProfileIsFollow::class.java)
    }

    private fun getRequestParams(profileIds: MutableList<String>): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_USERIDS] = profileIds
        return requestMap
    }

    companion object {
        const val KEY_USERIDS = "userIds"
    }

     private fun getQuery(): String {
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
}