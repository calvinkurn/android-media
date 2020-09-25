package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.TotalLikeContent
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetTotalLikeUseCase @Inject constructor(private val gqlUseCase: GraphqlRepository) : UseCase<TotalLike>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): TotalLike {
        val gqlRequest = GraphqlRequest(query, TotalLikeContent.Response::class.java, params)
        val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<TotalLikeContent.Response>(TotalLikeContent.Response::class.java)

        if (response != null &&
                response.totalLikeContent.error.isEmpty()) {
            response.totalLikeContent.data?.let {
                return TotalLike(it.like.value, if (it.like.fmt.isEmpty()
                        || it.like.fmt.equals("Like", false)) "0" else it.like.fmt)
            }
            return TotalLike(0, "0")
        } else {
            return TotalLike(0, "0")
        }
    }

    companion object {

        private const val CONTENT_ID = "contentID"
        private const val CONTENT_TYPE = "contentType"
        private const val LIKE_TYPE = "likeType" // 1 live, 2 vod

        private val query = getQuery()

        private fun getQuery() : String {
            val contentId = "\$contentID"
            val contentType = "\$contentType"
            val likeType = "\$likeType"

            return """
           query GetTotalLike($contentId: String, $contentType: Int, $likeType: Int) {
                feedGetLikeContent(contentID: $contentId, contentType: $contentType, likeType: $likeType) {
                    error
                    data {
                        like {
                            fmt
                            value
                        }
                    }
                }
            }
            """.trimIndent()
        }

        fun createParam(contentId: String, contentType: Int, likeType: Int): HashMap<String, Any> {
            return hashMapOf(
                    CONTENT_ID to contentId,
                    CONTENT_TYPE to contentType,
                    LIKE_TYPE to likeType
            )
        }
    }
}
