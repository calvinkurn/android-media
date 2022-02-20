package com.tokopedia.createpost.uprofile.domains

import com.tokopedia.createpost.uprofile.model.FeedXProfileHeader
import com.tokopedia.createpost.uprofile.model.ProfileDoFollowModelBase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class ProfileDoFollowUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun doFollow(followingUserId: String, followStatus: Boolean): ProfileDoFollowModelBase {
        val request = GraphqlRequest(
            getQuery(),
            ProfileDoFollowModelBase::class.java,
            getRequestParams(followingUserId, followStatus)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(ProfileDoFollowModelBase::class.java)
    }

    private fun getRequestParams(
        followingUserId: String,
        followStatus: Boolean
    ): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_FOLLOWING_USERID] = followingUserId
        requestMap[KEY_FOLLOW_STATUS] = followStatus
        return requestMap
    }

    companion object {
        const val KEY_FOLLOWING_USERID = "followingUserID"
        const val KEY_FOLLOW_STATUS = "followStatus"
    }

    private fun getQuery(): String {
        return "mutation DoFollow(\$followingUserID: String!, \$followStatus: Boolean!){\n" +
                "  feedXProfileFollow(req:{\n" +
                "    followingUserID:\$followingUserID,\n" +
                "    followStatus:\$followStatus\n" +
                "  }) {\n" +
                "    status\n" +
                "  }\n" +
                "}"
    }
}