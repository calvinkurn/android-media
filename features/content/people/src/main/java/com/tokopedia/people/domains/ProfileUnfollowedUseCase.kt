package com.tokopedia.people.domains
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.people.model.ProfileDoUnFollowModelBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

const val DO_UNFOLLOW_NEW = """
    mutation SocialNetworkUnfollow(\${'$'}userIDEnc: String) {
                SocialNetworkUnfollow(userIDEnc: \${'$'}userIDEnc) {
                  data {
                    is_success
                  }
                  messages
                  error_code
                }
                }
"""

@GqlQuery("DoUnFollowNew", DO_UNFOLLOW_NEW)
class ProfileUnfollowedUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun doUnfollow(unFollowingUserIdEnc: String): ProfileDoUnFollowModelBase {
        val request = GraphqlRequest(
            DoUnFollowNew.GQL_QUERY,
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
}
