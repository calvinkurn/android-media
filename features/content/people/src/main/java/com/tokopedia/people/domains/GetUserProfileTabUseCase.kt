package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.UserProfileTabModel
import javax.inject.Inject

/**
 * created by fachrizalmrsln on 10/11/2022
 */
@GqlQuery(GetUserProfileTabUseCase.QUERY_NAME, GetUserProfileTabUseCase.QUERY)
class GetUserProfileTabUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<UserProfileTabModel>(graphqlRepository) {

    init {
        setGraphqlQuery(GetUserProfileTabUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserProfileTabModel::class.java)
    }

    suspend fun executeOnBackground(userID: String): UserProfileTabModel {
        val request = mapOf(
            KEY_USER_ID to userID,
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_USER_ID = "userID"
        const val QUERY_NAME = "GetUserProfileTabUseCaseQuery"
        const val QUERY = """
            query FeedXProfileTabs(${"$$KEY_USER_ID"}: String!) {
                feedXProfileTabs($KEY_USER_ID: ${"$$KEY_USER_ID"}) {
                    data {
                      title
                      key
                      position
                      isActive
                    }
                    meta {
                      selectedIndex
                    }
                }
            }
        """
    }
}
