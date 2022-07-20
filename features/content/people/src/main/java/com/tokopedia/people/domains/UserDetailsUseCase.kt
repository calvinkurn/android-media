package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserProfileIsFollow
import javax.inject.Inject

const val USER_DETAILS = """
    query ProfileHeader(\${'$'}userName: String!) {
                feedXProfileHeader(username:\${'$'}userName) {
                  profile {
                    userID
                    encryptedUserID
                    imageCover
                    name
                    username
                    biography
                    sharelink {
                      weblink
                      applink
                    }
                    badges
                    liveplaychannel {
                      islive
                      liveplaychannelid
                      liveplaychannellink {
                        applink
                        weblink
                      }
                    }
                  }
                  stats {
                    totalPost
                    totalPostFmt
                    totalFollower
                    totalFollowerFmt
                    totalFollowing
                    totalFollowingFmt
                  }
                  hasAcceptTnC
                  shouldSeoIndex
                }
                }
"""

@GqlQuery("UserDetails", USER_DETAILS)
class UserDetailsUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun getUserProfileDetail(userName: String, profileId: MutableList<String>): GraphqlResponse {
        val request = GraphqlRequest(
            UserDetails.GQL_QUERY,
            ProfileHeaderBase::class.java,
            getRequestParams(userName)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        return useCase.executeOnBackground()
    }

    private fun getRequestParams(userName: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_USERNAME] = userName
        return requestMap
    }

    private fun getRequestParams(profileIds: MutableList<String>): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[ProfileTheyFollowedUseCase.KEY_USERIDS] = profileIds
        return requestMap
    }

    companion object {
        const val KEY_USERNAME = "userName"
    }

    private fun getQuery(): String {
        return ""
    }
}
