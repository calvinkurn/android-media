package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.IsLikedContent
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetIsLikeUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<Boolean>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(query, IsLikedContent.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val response = gqlResponse.getData<IsLikedContent.Response>(IsLikedContent.Response::class.java)

        if (response.isLikedContent.error.isEmpty()) {
            response.isLikedContent.data?.let {
                return it.isLike
            }
            return false
        } else {
            throw MessageErrorException(response.isLikedContent.error)
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
                    CONTENT_ID to contentId.toString(),
                    CONTENT_TYPE to contentType
            )
        }
    }
}
