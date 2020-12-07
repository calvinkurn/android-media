package com.tokopedia.sellerappwidget.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerappwidget.data.model.GetChatResponse
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.domain.mapper.ChatMapper
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 01/12/20
 */

@AppWidgetScope
class GetChatUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: ChatMapper
) : BaseUseCase<List<ChatUiModel>>() {

    override suspend fun executeOnBackground(): List<ChatUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetChatResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val errors: List<GraphqlError>? = gqlResponse.getError(GetChatResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetChatResponse>()
            val chatList = data.chatListMessage?.list.orEmpty()
            return mapper.mapRemoteModelToUiModel(chatList)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val KEY_PARAM_PAGE = "page"
        private const val KEY_PARAM_FILTER = "filter"
        private const val KEY_PARAM_TAB = "tab"
        private const val VALUE_PARAM_PAGE = 1
        private const val VALUE_PARAM_FILTER = "unread"
        private const val VALUE_PARAM_TAB = "tab-seller"

        fun creteParams(): RequestParams {
            return RequestParams.create().apply {
                putInt(KEY_PARAM_PAGE, VALUE_PARAM_PAGE)
                putString(KEY_PARAM_FILTER, VALUE_PARAM_FILTER)
                putString(KEY_PARAM_TAB, VALUE_PARAM_TAB)
            }
        }

        private val QUERY = """
            query getUserChatListMessage(${'$'}paramPage: Int!, ${'$'}paramFilter: String!, ${'$'}paramTab: String!) {
              status
              chatListMessage(page: ${'$'}paramPage, filter:${'$'}paramFilter, tab:${'$'}paramTab) {
                list {
                  msgID
                  messageKey
                  attributes {
                    contact {
                      name
                    }
                    lastReplyMessage
                    lastReplyTimeStr
                  }
                }
              }
            }
        """.trimIndent()
    }
}