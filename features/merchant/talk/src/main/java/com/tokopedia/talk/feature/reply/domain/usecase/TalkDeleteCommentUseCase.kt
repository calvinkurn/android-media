package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkDeleteCommentUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TalkDeleteCommentResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        const val PARAM_COMMENT_ID = "comment_id"

        private val query by lazy {
            val talkId = "\$talk_id"
            val commentId = "\$comment_id"
            """
                mutation talkDeleteComment($talkId: Int,$commentId: Int) {
                  talkDeleteComment(talk_id:$talkId, comment_id:$commentId) {
                    status
                    messageError
                    data {
                      isSuccess
                      commentId
                    }
                    messageErrorOriginal
                  }
                }

            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(TalkDeleteCommentResponseWrapper::class.java)
    }

    fun setParams(talkId: Int, commentId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_TALK_ID, talkId)
        requestParams.putInt(PARAM_COMMENT_ID, commentId)
        setRequestParams(requestParams.parameters)
    }
}