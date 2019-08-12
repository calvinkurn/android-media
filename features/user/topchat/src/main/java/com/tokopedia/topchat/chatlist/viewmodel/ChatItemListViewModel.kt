package com.tokopedia.topchat.chatlist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

class ChatItemListViewModel
@Inject constructor(private val graphqlUseCase: GraphqlUseCase<ChatListPojo>,
                                                private val rawQueries: Map<String, String>,
                                                dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val mutateChatListResponse = MutableLiveData<Result<ChatListPojo>>()

    fun queryGetChatListMessage(
            page: Int,
            filter: String,
            tab: String,
            perPage: Int
            ) {

        rawQueries[ChatListQueriesConstant.QUERY_CHAT_LIST_MESSAGE]?.let { query ->
            val params = mapOf(
                    ChatListQueriesConstant.PARAM_PAGE to page,
                    ChatListQueriesConstant.PARAM_FILTER to filter,
                    ChatListQueriesConstant.PARAM_TAB to tab,
                    ChatListQueriesConstant.PARAM_PER_PAGE to perPage

            )

            graphqlUseCase.setTypeClass(ChatListPojo::class.java)
            graphqlUseCase.setRequestParams(params)
            graphqlUseCase.setGraphqlQuery(query)
            graphqlUseCase.execute(
                    onSuccessGetChatListMessage(page)
                    , onErrorGetChatListMessage()
            )
        }
    }

    private fun onErrorGetChatListMessage(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutateChatListResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetChatListMessage(page: Int): (ChatListPojo) -> Unit {
        return {
            mutateChatListResponse.value = Success(it)
        }
    }
}
