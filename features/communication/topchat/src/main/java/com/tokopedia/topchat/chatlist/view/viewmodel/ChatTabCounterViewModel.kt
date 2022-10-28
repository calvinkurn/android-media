package com.tokopedia.topchat.chatlist.view.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

class ChatTabCounterViewModel @Inject constructor(
    private val getChatNotificationUseCase: GetChatNotificationUseCase,
    private val sharedPref: SharedPreferences,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _mutateChatNotification = MutableLiveData<Result<NotificationsPojo>>()
    val chatNotifCounter: LiveData<Result<NotificationsPojo>>
        get() = _mutateChatNotification


    fun queryGetNotifCounter(shopId: String) {
        getChatNotificationUseCase.getChatNotification(
                shopId,
                {
                    _mutateChatNotification.value = Success(it)
                },
                {
                    _mutateChatNotification.value = Fail(it)
                }
        )
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

    fun isSearchOnBoardingTooltipHasShown(): Boolean {
        return sharedPref.getBoolean(SEARCH_TOOLTIP_ONBOARDING, false)
    }

    fun toolTipOnBoardingShown() {
        sharedPref.edit().putBoolean(SEARCH_TOOLTIP_ONBOARDING, true).apply()
    }

    companion object {
        private const val PREF_CHAT_LIST_TAB = "chatlist_tab_activity.pref"
        private const val KEY_LAST_POSITION = "key_last_seen_tab_position"
        const val SEARCH_TOOLTIP_ONBOARDING = "search_tooltip_onboarding"
    }
}
