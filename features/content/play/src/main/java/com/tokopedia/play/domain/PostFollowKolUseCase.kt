package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.KOLFollowStatus
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 02/03/22
 */
@GqlQuery(PostFollowKolUseCase.QUERY_NAME, PostFollowKolUseCase.QUERY)
class PostFollowKolUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<KOLFollowStatus>(graphqlRepository) {

    init {
        setGraphqlQuery(PostFollowKolUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(KOLFollowStatus::class.java)
    }

    fun createParam(userId: String): RequestParams {
        return RequestParams.create().apply {
            mapOf(
                PostUnfollowKolUseCase.USER_ID to userId
            )
        }
    }

    companion object {
        const val QUERY_NAME = "PostFollowKolUseCaseQuery"
        const val QUERY = """
            mutation followKOL(${'$'}userIDEnc: String){
                SocialNetworkFollow(userIDEnc: ${'$'}userIDEnc){
                    data{
                        is_success
                    }
                    messages
                }
            }
        """
    }
}