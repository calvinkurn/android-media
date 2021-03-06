package com.tokopedia.play.domain

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

class GetIsLikeUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<Boolean>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(query, IsLikedContent.Response::class.java, params)

        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
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

        private val query = getQuery()

        private fun getQuery() : String {
            val contentId = "\$contentID"
            val contentType = "\$contentType"

            return """
               query GetIsLiked($contentId: Int, $contentType: Int) {
                    feedGetIsLikePost(contentID: $contentId, contentType: $contentType) {
                        error
                        data {
                            isLike
                        }
                    }
                }
            """.trimIndent()
        }

        fun createParam(contentId: Int, contentType: Int): HashMap<String, Any> {
            return hashMapOf(
                    CONTENT_ID to contentId,
                    CONTENT_TYPE to contentType
            )
        }
    }
}
