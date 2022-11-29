package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.ProfileHeaderBase
import javax.inject.Inject

/**
isBlocking
isBlockedBy
 */
@GqlQuery(UserDetailsUseCase.QUERY_NAME, UserDetailsUseCase.QUERY)
class UserDetailsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<ProfileHeaderBase>(graphqlRepository) {

    init {
        setGraphqlQuery(UserDetailsUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProfileHeaderBase::class.java)
    }

    suspend fun executeOnBackground(username: String): ProfileHeaderBase {
        val request = mapOf(
            KEY_USERNAME to username,
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_USERNAME = "username"

        const val QUERY_NAME = "UserDetailsUseCaseQuery"
        const val QUERY = """
            query ProfileHeader(${"$$KEY_USERNAME"}: String!) {
                feedXProfileHeader(
                    $KEY_USERNAME: ${"$$KEY_USERNAME"}
                ) {
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
    }
}
