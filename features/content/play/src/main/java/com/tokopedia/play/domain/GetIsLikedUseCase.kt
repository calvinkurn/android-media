package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.data.IsLikedContent
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */
@GqlQuery(GetIsLikeUseCase.QUERY_NAME, GetIsLikeUseCase.QUERY)
class GetIsLikeUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<Boolean>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(GetIsLikeUseCaseQuery.GQL_QUERY, IsLikedContent.Response::class.java, params)

        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(IsLikedContent.Response::class.java)
        if (error != null) return false

        val response = gqlResponse.getData<IsLikedContent.Response>(IsLikedContent.Response::class.java)
                ?: return false
        if (response.isLikedContent.error.isEmpty()) {
            response.isLikedContent.data?.let {
                return it.isLike
            }
            return false
        } else {
            return false
        }
    }

    companion object {

        private const val CONTENT_ID = "contentID"
        private const val CONTENT_TYPE = "contentType"
        const val QUERY_NAME = "GetIsLikeUseCaseQuery"
        const val QUERY = """
           query GetIsLiked(${'$'}contentID: Int, ${'$'}contentType: Int) {
                feedGetIsLikePost(contentID: ${'$'}contentID, contentType: ${'$'}contentType) {
                    error
                    data {
                        isLike
                    }
                }
            }
        """

        fun createParam(contentId: Long, contentType: Int): HashMap<String, Any> {
            return hashMapOf(
                    CONTENT_ID to contentId,
                    CONTENT_TYPE to contentType
            )
        }
    }
}
