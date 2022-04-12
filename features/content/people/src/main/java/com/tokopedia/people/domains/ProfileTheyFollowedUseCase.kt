package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.people.model.UserProfileIsFollow
import javax.inject.Inject

const val THEY_FOLLOW = """
    query ProfileIsFollowing(\${'$'}userIds: [String!]!) {
                feedXProfileIsFollowing(followingUserIDs: \${'$'}userIds) {
                  isUserFollowing {
                    userID
                    encryptedUserID
                    status
                  }
                }
                }
"""

@GqlQuery("TheyFollow", THEY_FOLLOW)
class ProfileTheyFollowedUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun profileIsFollowing(profileIds: MutableList<String>) : UserProfileIsFollow {
            val request = GraphqlRequest(TheyFollow.GQL_QUERY,
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
        return ""
    }
}