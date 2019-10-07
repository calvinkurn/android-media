package com.tokopedia.topchat.chatlist.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.QUERY_CHAT_NOTIFICATION
import com.tokopedia.topchat.chatlist.pojo.NotificationsPojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

class ChatTabCounterViewModel @Inject constructor(
        private val chatNotificationUseCase: GraphqlUseCase<NotificationsPojo>,
        private val queries: Map<String, String>,
        private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher){

    private val _mutateChatNotification = MutableLiveData<Result<NotificationsPojo>>()
    val chatNotifCounter: LiveData<Result<NotificationsPojo>>
        get() = _mutateChatNotification


    fun queryGetNotifCounter() {
        queries[QUERY_CHAT_NOTIFICATION]?.let { query ->

            chatNotificationUseCase.apply {
                setTypeClass(NotificationsPojo::class.java)
                setGraphqlQuery(query)
                execute({ result ->
                    _mutateChatNotification.value = Success(result)
                }, { error ->
                    error.printStackTrace()
                    _mutateChatNotification.value = Fail(error)
                })
            }
        }
    }

}
