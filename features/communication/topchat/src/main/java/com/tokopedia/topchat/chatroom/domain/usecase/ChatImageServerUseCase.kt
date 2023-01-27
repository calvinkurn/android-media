package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.imageserver.ChatImageServerResponse
import javax.inject.Inject

class ChatImageServerUseCase @Inject constructor(
    private val gqlUseCase: GraphqlUseCase<ChatImageServerResponse>
) {

    private var response: ChatImageServerResponse? = null

    fun getSourceId(
        onSuccess: (String, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (sourceIdAlreadyRetrieved(onSuccess)) return
        gqlUseCase.apply {
            setTypeClass(ChatImageServerResponse::class.java)
            setRequestParams(emptyMap())
            setGraphqlQuery(query)
            execute({ result ->
                response = result
                onSuccess(result.sourceId, result.sourceIdSecure)
            }, { error ->
                onError(error)
            })
        }
    }

    private fun sourceIdAlreadyRetrieved(onSuccess: (String, String) -> Unit): Boolean {
        response?.let {
            if (it.sourceId.isNotEmpty() && it.sourceIdSecure.isNotEmpty()) {
                onSuccess(it.sourceId, it.sourceIdSecure)
                return true
            }
        }
        return false
    }

    private val query = """
        query chatImageServer{
          chatImageServer {
            sourceID,
            sourceIDSecure
          }
        }
    """.trimIndent()
}
