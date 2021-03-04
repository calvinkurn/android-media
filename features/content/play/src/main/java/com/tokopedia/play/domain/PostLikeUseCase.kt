package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.LikeContent
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-05.
 */
class PostLikeUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<Boolean>(graphqlRepository) {

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(query, LikeContent.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val response = gqlResponse.getData<LikeContent.Response>(LikeContent.Response::class.java)
        if (response.doLikeKolPost.error.isEmpty())  {
            response.doLikeKolPost.data?.let {
                return it.success == PLAY_FEED_SUCCESS
            }
            return false
        } else {
            throw MessageErrorException(response.doLikeKolPost.error)
        }
    }

    companion object {

        private const val POST_ID = "idPost"
        private const val ACTION = "action"
        private const val CONTENT_ID = "contentId"
        private const val CONTENT_TYPE = "contentType"
        private const val LIKE_TYPE = "likeType"

        private const val PLAY_FEED_SUCCESS = 1

        private val query = getQuery()

        private fun getQuery() : String {
            val postId = "\$idPost"
            val action = "\$action"
            val contentId = "\$contentId"
            val contentType = "\$contentType"
            val likeType = "\$likeType"

            return """
                mutation PostLike($postId: Int, $action: Int, $contentId: Int, $contentType: Int, $likeType: Int){
                    do_like_kol_post(idPost: $postId, action:$action, contentId:$contentId, contentType: $contentType, likeType: $likeType) {
                        error
                        data {
                            success
                        }
                    }
                }
            """.trimIndent()
        }

        fun createParam(contentId: Int, contentType: Int, likeType: Int, action: Boolean): HashMap<String, Any> {
            return hashMapOf(
                    POST_ID to 0,
                    ACTION to if(action) 1 else 0,
                    CONTENT_ID to contentId,
                    CONTENT_TYPE to contentType,
                    LIKE_TYPE to likeType
            )
        }
    }

}
