package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.KOLUnFollowStatus
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 02/03/22
 */
@GqlQuery(PostUnfollowKolUseCase.QUERY_NAME, PostUnfollowKolUseCase.QUERY)
class PostUnfollowKolUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<KOLUnFollowStatus>(graphqlRepository) {

    init {
        setGraphqlQuery(PostUnfollowKolUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(KOLUnFollowStatus::class.java)
    }

    fun createParam(userId: String): RequestParams {
        return RequestParams.create().apply {
            mapOf(
                USER_ID to userId
            )
        }
    }

    companion object {
        const val USER_ID = "userIDEnc"
        const val QUERY_NAME = "PostUnfollowKolUseCaseQuery"
        const val QUERY = """
            mutation unfollowKOL(${'$'}userIDEnc: String){
                SocialNetworkUnfollow(userIDEnc: ${'$'}userIDEnc){
                    data{
                        is_success
                    }
                    messages
                }
            }
        """
    }
}