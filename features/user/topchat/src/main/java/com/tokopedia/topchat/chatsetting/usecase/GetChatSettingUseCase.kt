package com.tokopedia.topchat.chatsetting.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatsetting.data.GetChatSettingResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

class GetChatSettingUseCase @Inject constructor(
        @Named(ChatListQueriesConstant.QUERY_GET_CHAT_SETTING)
        private val gqlQuery: String,
        private val gqlUseCase: GraphqlUseCase<GetChatSettingResponse>
) {

    private val params = RequestParams.EMPTY

    fun get(onSuccess: (GetChatSettingResponse) -> Unit, onError: (Throwable) -> Unit) {
        gqlUseCase.apply {
            setTypeClass(GetChatSettingResponse::class.java)
            setRequestParams(params.parameters)
            setGraphqlQuery(gqlQuery)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

}