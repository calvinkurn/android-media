package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileDoFollowModelBase
import com.tokopedia.createpost.uprofile.model.ProfileDoUnFollowModelBase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class ProfileUnfollowedUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun doUnfollow(unFollowingUserIdEnc: String): ProfileDoUnFollowModelBase {
        val request = GraphqlRequest(
            getQuery(),
            ProfileDoUnFollowModelBase::class.java,
            getRequestParams(unFollowingUserIdEnc)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(ProfileDoUnFollowModelBase::class.java)
    }

    private fun getRequestParams(unFollowingUserIdEnc: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_FOLLOWING_USERID_ENC] = unFollowingUserIdEnc
        return requestMap
    }

    companion object {
        const val KEY_FOLLOWING_USERID_ENC = "userIDEnc"
    }

    private fun getQuery(): String {
        return "mutation SocialNetworkUnfollow(\$userIDEnc: String) {\n" +
                "  SocialNetworkUnfollow(userIDEnc: \$userIDEnc) {\n" +
                "    data {\n" +
                "      is_success\n" +
                "    }\n" +
                "    messages\n" +
                "    error_code\n" +
                "  }\n" +
                "}"
    }
}