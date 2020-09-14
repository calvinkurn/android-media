package com.tokopedia.topchat.chatsetting.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatsetting.data.ChatGearChatListResponse
import com.tokopedia.topchat.chatsetting.data.GetChatSettingResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetChatSettingUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatGearChatListResponse>
) {

    private val params = RequestParams.EMPTY

    fun get(onSuccess: (ChatGearChatListResponse) -> Unit, onError: (Throwable) -> Unit) {
        gqlUseCase.apply {
            setTypeClass(ChatGearChatListResponse::class.java)
            setRequestParams(params.parameters)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    private val query = """
        query chatGearChatList{
            chatGearChatList{
              isSuccess
              listBuyer{
                alias
                description
                link
                label
                typeLabel
              }
              listSeller{
                alias
                description
                link
                label
                typeLabel
              }
              listUtils{
                alias
                description
                link
                label
                typeLabel
              }
            }
        }
    """.trimIndent()
}