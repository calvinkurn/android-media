package com.tokopedia.topchat.chatlist.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

class ChatTabCounterViewModel @Inject constructor(
    private val getChatNotification: GetChatNotificationUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _mutateChatNotification = MutableLiveData<Result<NotificationsPojo>>()
    val chatNotifCounter: LiveData<Result<NotificationsPojo>>
        get() = _mutateChatNotification

    fun queryGetNotifCounter(shopId: String) {
        viewModelScope.launch {
            try {
                val result = getChatNotification(shopId)
                _mutateChatNotification.value = Success(result)
            } catch (throwable: Throwable) {
                _mutateChatNotification.value = Fail(throwable)
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
        const val PREF_CHAT_LIST_TAB = "chatlist_tab_activity.pref"
        const val KEY_LAST_POSITION = "key_last_seen_tab_position"
    }
}
