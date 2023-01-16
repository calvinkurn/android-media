package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.ProfileFollowingListBase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 16, 2022
 */
@GqlQuery(GetFollowingListUseCase.QUERY_NAME, GetFollowingListUseCase.QUERY)
class GetFollowingListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<ProfileFollowingListBase>(graphqlRepository) {

    init {
        setGraphqlQuery(GetFollowingListUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProfileFollowingListBase::class.java)
    }

    suspend fun executeOnBackground(
        username: String,
        cursor: String,
        limit: Int,
    ): ProfileFollowingListBase {
        val request = mapOf(
            KEY_USERNAME to username,
            KEY_CURSOR to cursor,
            KEY_LIMIT to limit,
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_CURSOR = "cursor"
        private const val KEY_LIMIT = "limit"

        const val QUERY_NAME = "GetFollowingListUseCaseQuery"
        const val QUERY = """
                query ProfileFollowingList(
                    ${"$$KEY_USERNAME"}: String!, 
                    ${"$$KEY_CURSOR"}: String!, 
                    ${"$$KEY_LIMIT"}: Int!
                ) {
                  feedXProfileFollowingList(req: {
                    $KEY_USERNAME: ${"$$KEY_USERNAME"}, 
                    $KEY_CURSOR: ${"$$KEY_CURSOR"}, 
                    $KEY_LIMIT: ${"$$KEY_LIMIT"}
                }) {
                    followings {
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
                      }
                      isFollow
                    }
                    newCursor
                  }
                }
        """
    }
}
