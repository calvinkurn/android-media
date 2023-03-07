package com.tokopedia.sellerappwidget.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerappwidget.data.model.GetChatResponse
import com.tokopedia.sellerappwidget.domain.mapper.ChatMapper
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 01/12/20
 */

class GetChatUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: ChatMapper
) : BaseUseCase<ChatUiModel>() {

    override suspend fun executeOnBackground(): ChatUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GetChatResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))
        val errors: List<GraphqlError>? = gqlResponse.getError(GetChatResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetChatResponse>()
            return mapper.mapRemoteModelToUiModel(data)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    data class Param(
        @SerializedName(PARAM_SHOP_ID)
        val shopId: String
    )

    companion object {
        private const val KEY_PARAM_PAGE = "page"
        private const val KEY_PARAM_FILTER = "filter"
        private const val KEY_PARAM_TAB = "tab"
        private const val VALUE_PARAM_PAGE = 1
        private const val VALUE_PARAM_FILTER = "unread"
        private const val VALUE_PARAM_TAB = "tab-seller"

        private const val PARAM_INPUT = "input"
        private const val PARAM_SHOP_ID = "shop_id"

        fun creteParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putInt(KEY_PARAM_PAGE, VALUE_PARAM_PAGE)
                putString(KEY_PARAM_FILTER, VALUE_PARAM_FILTER)
                putString(KEY_PARAM_TAB, VALUE_PARAM_TAB)
                putObject(PARAM_INPUT, Param(shopId))
            }
        }

        private val QUERY = """
            query getUserChatListMessage(${'$'}page: Int!, ${'$'}filter: String!, ${'$'}tab: String!, $$PARAM_INPUT: NotificationRequest) {
              chatListMessage(page: ${'$'}page, filter:${'$'}filter, tab:${'$'}tab) {
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
              notifications(input: $$PARAM_INPUT) {
                chat {
                  unreadsSeller
                }
              }
            }
        """.trimIndent()
    }
}