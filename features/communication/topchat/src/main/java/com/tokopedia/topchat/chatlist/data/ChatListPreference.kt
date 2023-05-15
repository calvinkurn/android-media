package com.tokopedia.topchat.chatlist.data

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.topchat.chatlist.view.fragment.ChatTabListFragment
import javax.inject.Inject

@ActivityScope
class ChatListPreference @Inject constructor(@ApplicationContext private val context: Context) {

    private val topChatPref by lazy {
        context.getSharedPreferences("topchat_prefs", Context.MODE_PRIVATE)
    }

    var coachMarkShown: Boolean
        get() {
            return CoachMarkPreference.hasShown(context, ChatTabListFragment.TAG_ONBOARDING)
        }
        set(value) {
            CoachMarkPreference.setShown(context, ChatTabListFragment.TAG_ONBOARDING, value)
        }

    var searchTooltipShown: Boolean
        get() {
            return topChatPref.getBoolean(
                SEARCH_TOOLTIP_ONBOARDING,
                false
            )
        }
        set(value) {
            topChatPref.edit()
                .putBoolean(SEARCH_TOOLTIP_ONBOARDING, value).apply()
        }

    companion object {
        const val SEARCH_TOOLTIP_ONBOARDING = "search_tooltip_onboarding"
    }
}
