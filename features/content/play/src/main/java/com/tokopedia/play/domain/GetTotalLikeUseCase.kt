package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.TotalLikeContent
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetTotalLikeUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<TotalLike>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): TotalLike {
        val gqlRequest = GraphqlRequest(query, TotalLikeContent.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val response = gqlResponse.getData<TotalLikeContent.Response>(TotalLikeContent.Response::class.java)

        if (response.totalLikeContent.error.isEmpty()) {
            response.totalLikeContent.data?.let {
                return TotalLike(it.like.value, it.like.fmt)
            }
            return TotalLike(0, "")
        } else {
            throw MessageErrorException(response.totalLikeContent.error)
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

        fun createParam(contentId: Int, contentType: Int, isLive: Boolean): HashMap<String, Any> {
            return hashMapOf(
                    CONTENT_ID to contentId.toString(),
                    CONTENT_TYPE to contentType,
                    LIKE_TYPE to if(isLive) 1 else 2
            )
        }
    }
}
