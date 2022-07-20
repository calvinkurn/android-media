package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.people.model.ProfileDoFollowModelBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

const val DO_FOLLOW = """
    mutation DoFollow(\${'$'}followingUserID: String!, \${'$'}followStatus: Boolean!){
                feedXProfileFollow(req:{
                  followingUserID:\${'$'}followingUserID,
                  followStatus:\${'$'}followStatus
                }) {
                  status
                }
                }
"""

@GqlQuery("DoFollow", DO_FOLLOW)
class ProfileDoFollowUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun doFollow(followingUserId: String, followStatus: Boolean): ProfileDoFollowModelBase {
        val request = GraphqlRequest(
            DoFollow.GQL_QUERY,
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
}