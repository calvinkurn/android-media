package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.ProfileFollowerListBase
import com.tokopedia.createpost.uprofile.model.ProfileFollowingListBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

class FollowerFollowingListingUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun getProfileFollowerList(
        userName: String,
        cursor: String,
        limit: Int
    ): ProfileFollowerListBase {
        val request = GraphqlRequest(
            queryFollower(),
            ProfileFollowerListBase::class.java,
            getRequestParams(userName, cursor, limit)
        )
        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(ProfileFollowerListBase::class.java)
    }

    suspend fun getProfileFollowingList(
        userName: String,
        cursor: String,
        limit: Int
    ): ProfileFollowingListBase {
        val request = GraphqlRequest(
            queryFollowing(),
            ProfileFollowingListBase::class.java,
            getRequestParams(userName, cursor, limit)
        )
        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(ProfileFollowingListBase::class.java)
    }

    private fun getRequestParams(
        userName: String,
        cursor: String,
        limit: Int
    ): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_USERNAME] = userName
        requestMap[KEY_CURSOR] = cursor
        requestMap[KEY_LIMIT] = limit
        return requestMap
    }

    companion object {
        const val KEY_USERNAME = "userName"
        const val KEY_CURSOR = "cursor"
        const val KEY_LIMIT = "limit"
    }

    private fun queryFollower(): String {
        return "query ProfileFollowerList(\$userName: String!, \$cursor: String!, \$limit: Int!) {\n" +
                "  feedXProfileFollowerList(req: {username: \$userName, cursor: \$cursor, limit: \$limit}) {\n" +
                "    followers {\n" +
                "      profile {\n" +
                "        userID\n" +
                "        encryptedUserID\n" +
                "        imageCover\n" +
                "        name\n" +
                "        username\n" +
                "        biography\n" +
                "        sharelink {\n" +
                "          weblink\n" +
                "          applink\n" +
                "        }\n" +
                "        badges\n" +
                "      }\n" +
                "      isFollow\n" +
                "    }\n" +
                "    newCursor\n" +
                "  }\n" +
                "}\n"
    }

    private fun queryFollowing(): String {
        return "query ProfileFollowerList(\$userName: String!, \$cursor: String!, \$limit: Int!) {\n" +
                "  feedXProfileFollowingList(req: {username: \$userName, cursor: \$cursor, limit: \$limit}) {\n" +
                "    followings {\n" +
                "      profile {\n" +
                "        userID\n" +
                "        encryptedUserID\n" +
                "        imageCover\n" +
                "        name\n" +
                "        username\n" +
                "        biography\n" +
                "        sharelink {\n" +
                "          weblink\n" +
                "          applink\n" +
                "        }\n" +
                "        badges\n" +
                "      }\n" +
                "      isFollow\n" +
                "    }\n" +
                "    newCursor\n" +
                "  }\n" +
                "}\n"
    }
}