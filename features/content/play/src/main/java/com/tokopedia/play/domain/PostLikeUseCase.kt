package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.LikeContent
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-05.
 */
class PostLikeUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<Boolean>() {

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(query, LikeContent.Response::class.java, params)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
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

        private const val PLAY_FEED_CONTENT_TYPE = "1"
        private const val PLAY_FEED_SUCCESS = 1

        private val query = getQuery()

        private fun getQuery() : String {
            val postId = "\$idPost"
            val action = "\$action"
            val contentId = "\$contentId"
            val contentType = "\$contentType"
            val likeType = "\$likeType"

            /**
             * TODO("uncomment this query")
             *
            return """
                mutation LikePost($postId: Int, $action: Int!, $contentId: Int!, $contentType, $likeType: Int!){
                    do_like_kol_post(idPost: $postId, action: $action, contentId:$contentId, contentType: $contentType, likeType: $likeType) {
                        error
                        data {
                            success
                        }
                    }
                }
            """.trimIndent()
            */

            // TODO("comment this query")
            return """
                mutation LikePost($postId: Int, $action: Int!){
                    do_like_kol_post(idPost: $postId, action: $action) {
                        error
                        data {
                            success
                        }
                    }
                }
            """.trimIndent()
        }

        fun createParam(channelId: Int, action: Boolean): HashMap<String, Any> {
            return hashMapOf(
                    POST_ID to 15063139, // TODO("leave idPost null")
                    ACTION to if(action) 1 else 0
                    /**
                     * TODO("uncomment this params")
                    ,
                    CONTENT_ID to channelId,
                    CONTENT_TYPE to PLAY_FEED_CONTENT_TYPE,
                    LIKE_TYPE to if(action) 1 else 0
                     */
            )
        }
    }

}
