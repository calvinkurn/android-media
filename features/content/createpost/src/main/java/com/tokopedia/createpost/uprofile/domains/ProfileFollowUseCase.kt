package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileDoFollowModelBase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class ProfileFollowUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun doFollow(followingUserIdEnc: String): ProfileDoFollowModelBase {
        val request = GraphqlRequest(
            getQuery(),
            ProfileDoFollowModelBase::class.java,
            getRequestParams(followingUserIdEnc)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(ProfileDoFollowModelBase::class.java)
    }

    private fun getRequestParams(followingUserIdEnc: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_FOLLOWING_USERID_ENC] = followingUserIdEnc
        return requestMap
    }

    companion object {
        const val KEY_FOLLOWING_USERID_ENC = "userIDEnc"
    }

    private fun getQuery(): String {
        return "mutation SocialNetworkFollow(\$userIDEnc: String) {\n" +
                "  SocialNetworkFollow(userIDEnc: \$userIDEnc) {\n" +
                "    data {\n" +
                "      user_id_source\n" +
                "      user_id_target\n" +
                "      relation\n" +
                "    }\n" +
                "    messages\n" +
                "    error_code\n" +
                "  }\n" +
                "}\n"
    }
}