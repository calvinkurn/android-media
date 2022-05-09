package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.people.model.ProfileDoFollowModelBase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

const val DO_FOLLOW_NEW = """
    mutation SocialNetworkFollow(\${'$'}userIDEnc: String) {
                SocialNetworkFollow(userIDEnc: \${'$'}userIDEnc) {
                  data {
                    user_id_source
                    user_id_target
                    relation
                  }
                  messages
                  error_code
                }
                }
"""

@GqlQuery("DoFollowNew", DO_FOLLOW_NEW)
class ProfileFollowUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun doFollow(followingUserIdEnc: String): ProfileDoFollowModelBase {
        val request = GraphqlRequest(
            DoFollowNew.GQL_QUERY,
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
}
