package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewCommentResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkCreateNewCommentUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TalkCreateNewCommentResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TEXT = "text"
        const val PARAM_TALK_ID = "talk_id"
        const val PARAM_PRODUCT_IDS = "product_ids"

        private val query by lazy {
            val text = "\$text"
            val talkId = "\$talk_id"
            val productIds = "\$product_ids"
            """
                mutation talkCreateNewComment($text: String, $talkId: Int, $productIds: String) {
                  talkCreateNewComment(text:$text, talk_id:$talkId, product_ids:$productIds) {
                    status
                    messageError
                    data {
                      isSuccess
                      commentID
                    }
                    messageErrorOriginal
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(TalkCreateNewCommentResponseWrapper::class.java)
    }

    fun setParams(text: String, talkId: Int, productIds: String) {
        val requestParams = RequestParams()
        requestParams.putString(PARAM_TEXT, text)
        requestParams.putInt(PARAM_TALK_ID, talkId)
        requestParams.putString(PARAM_PRODUCT_IDS, productIds)
        setRequestParams(requestParams.parameters)
    }
}