package com.tokopedia.feedcomponent.people.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.feedcomponent.people.model.ProfileDoUnFollowModelBase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(ProfileUnfollowedUseCase.QUERY_NAME, ProfileUnfollowedUseCase.QUERY)
class ProfileUnfollowedUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProfileDoUnFollowModelBase>(graphqlRepository) {

    init {
        setGraphqlQuery(ProfileUnfollowedUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProfileDoUnFollowModelBase::class.java)
    }

    suspend fun executeOnBackground(followedUserIdEnc: String): ProfileDoUnFollowModelBase {
        val request = mapOf(
            KEY_USER_ID_ENC to followedUserIdEnc
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_USER_ID_ENC = "userIDEnc"

        const val QUERY_NAME = "ProfileUnfollowedUseCaseQuery"
        const val QUERY = """
            mutation SocialNetworkUnfollow(
                ${"$$KEY_USER_ID_ENC"}: String
            ) {
                SocialNetworkUnfollow(
                    $KEY_USER_ID_ENC: ${"$$KEY_USER_ID_ENC"}
                ) {
                  data {
                    is_success
                  }
                  messages
                  error_code
                }
            }
        """
    }
}
