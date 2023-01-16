package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.ProfileFollowerListBase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 16, 2022
 */
@GqlQuery(GetFollowerListUseCase.QUERY_NAME, GetFollowerListUseCase.QUERY)
class GetFollowerListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<ProfileFollowerListBase>(graphqlRepository) {

    init {
        setGraphqlQuery(GetFollowerListUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProfileFollowerListBase::class.java)
    }

    suspend fun executeOnBackground(
        username: String,
        cursor: String,
        limit: Int,
    ): ProfileFollowerListBase {
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

        const val QUERY_NAME = "GetFollowerListUseCaseQuery"
        const val QUERY = """
                query ProfileFollowerList(
                    ${"$$KEY_USERNAME"}: String!, 
                    ${"$$KEY_CURSOR"}: String!, 
                    ${"$$KEY_LIMIT"}: Int!
                ) {
                  feedXProfileFollowerList(req: {
                    $KEY_USERNAME: ${"$$KEY_USERNAME"}, 
                    $KEY_CURSOR: ${"$$KEY_CURSOR"}, 
                    $KEY_LIMIT: ${"$$KEY_LIMIT"}
                }) {
                    followers {
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
