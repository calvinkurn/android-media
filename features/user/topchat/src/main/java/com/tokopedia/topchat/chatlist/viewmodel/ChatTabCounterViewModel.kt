package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
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
) : BaseViewModel(dispatcher) {

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

    fun setLastVisitedTab(context: Context, position: Int) {
        context.getSharedPreferences(PREF_CHAT_LIST_TAB, Context.MODE_PRIVATE)
                .edit()
                .apply {
                    putInt(KEY_LAST_POSITION, position)
                    apply()
                }
    }

    fun getLastVisitedTab(context: Context): Int {
        return context.getSharedPreferences(PREF_CHAT_LIST_TAB, Context.MODE_PRIVATE)
                .getInt(KEY_LAST_POSITION, -1)
    }

    companion object {
        private const val PREF_CHAT_LIST_TAB = "chatlist_tab_activity.pref"
        private const val KEY_LAST_POSITION = "key_last_seen_tab_position"
    }
}
