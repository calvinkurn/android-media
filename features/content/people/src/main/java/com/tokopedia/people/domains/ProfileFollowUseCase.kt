package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.people.model.ProfileDoFollowModelBase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(ProfileFollowUseCase.QUERY_NAME, ProfileFollowUseCase.QUERY)
class ProfileFollowUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProfileDoFollowModelBase>(graphqlRepository) {

    init {
        setGraphqlQuery(ProfileFollowUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProfileDoFollowModelBase::class.java)
    }

    companion object {
        private const val KEY_USER_ID_ENC = "userIDEnc"

        const val QUERY_NAME = "ProfileFollowUseCaseQuery"
        const val QUERY = """
            mutation SocialNetworkFollow(
                ${"$$KEY_USER_ID_ENC"}: String
            ) {
                SocialNetworkFollow(
                    $KEY_USER_ID_ENC: ${"$$KEY_USER_ID_ENC"}
                ) {
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

        fun createParam(
            followingUserIdEnc: String
        ): Map<String, Any> = mapOf(
            KEY_USER_ID_ENC to followingUserIdEnc
        )
    }
}