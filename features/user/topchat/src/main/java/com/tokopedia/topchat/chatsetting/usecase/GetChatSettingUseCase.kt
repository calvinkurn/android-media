package com.tokopedia.topchat.chatsetting.usecase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.chatsetting.data.ChatGearChatListResponse
import com.tokopedia.topchat.chatsetting.data.mapper.ChatSettingMapper
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetChatSettingUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatGearChatListResponse>,
        private val chatSettingMapper: ChatSettingMapper,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {


    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun get(onSuccess: (List<Visitable<ChatSettingTypeFactory>>) -> Unit, onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.IO,
                {
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatGearChatListResponse::class.java)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val mappedData = chatSettingMapper.mapGearChatResponse(response)
                    withContext(dispatchers.Main) {
                        onSuccess(mappedData)
                    }
                },
                { exception ->
                    withContext(dispatchers.Main) {
                        onError(exception)
                    }
                }
        )
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