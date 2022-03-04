package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.FollowKOL
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 02/03/22
 */
@GqlQuery(GetFollowingKOLUseCase.QUERY_NAME, GetFollowingKOLUseCase.QUERY)
class GetFollowingKOLUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FollowKOL.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(GetFollowingKOLUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FollowKOL.Response::class.java)
    }

    fun createParam(followedUserId: String): RequestParams {
        return RequestParams.create().apply {
            mapOf(
                FOLLOWED_USER_ID_PARAMS to followedUserId
            )
        }
    }

    companion object {
        const val FOLLOWED_USER_ID_PARAMS = "followingUserIDs"
        const val QUERY_NAME = "GetFollowingKOLUseCaseQuery"
        const val QUERY = """
            query getFollowingKol(${'$'}followingUserIDs: [String!]!){
                feedXProfileIsFollowing(followingUserIDs: ${'$'}followingUserIDs){
                    isUserFollowing {
                      userID
                      status
                    }
                }
            }
        """
    }
}