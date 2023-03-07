package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.UserProfileIsFollow
import javax.inject.Inject

@GqlQuery(ProfileTheyFollowedUseCase.QUERY_NAME, ProfileTheyFollowedUseCase.QUERY)
class ProfileTheyFollowedUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<UserProfileIsFollow>(graphqlRepository) {

    init {
        setGraphqlQuery(ProfileTheyFollowedUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserProfileIsFollow::class.java)
    }

    suspend fun executeOnBackground(profileIds: List<String>): UserProfileIsFollow {
        val request = mapOf(
            KEY_USER_IDS to profileIds,
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_USER_IDS = "followingUserIDs"

        const val QUERY_NAME = "ProfileTheyFollowedUseCaseQuery"
        const val QUERY = """
            query ProfileIsFollowing(
                ${"$$KEY_USER_IDS"}: [String!]!
            ) {
                feedXProfileIsFollowing(
                    $KEY_USER_IDS: ${"$$KEY_USER_IDS"}
                ) {
                  isUserFollowing {
                    userID
                    encryptedUserID
                    status
                  }
                }
            }
        """
    }
}
