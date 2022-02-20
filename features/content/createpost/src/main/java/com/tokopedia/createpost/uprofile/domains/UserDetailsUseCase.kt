package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class UserDetailsUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun getUserProfileDetail(userName: String) : ProfileHeaderBase {
            val request = GraphqlRequest(getQuery(),
                ProfileHeaderBase::class.java,
                getRequestParams(userName))

        useCase.clearRequest()
            useCase.addRequest(request)
            val response = useCase.executeOnBackground()
            return response.getData(ProfileHeaderBase::class.java)
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
}