package com.tokopedia.navigation.util

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.boolean
import com.tokopedia.navigation.R
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey.FEED_NEW_ICON

class FeedCoachMark(private val context: Context) {

    private var feedCoachMarkToggle by
        context
            .getSharedPreferences(PREF_KEY_FEED_COACHMARK, Context.MODE_PRIVATE)
            .boolean(false, PREF_KEY_FEED_COACHMARK_SHOWN)

    fun show(feedIconView: View) {
        val willShowCoachMark = !feedCoachMarkToggle && isABTestRemoteConfigEnabled()

        if (!willShowCoachMark) return

        val coachMark = CoachMark2(context)
        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    feedIconView,
                    context.getString(R.string.feed_icon_coachmark_title),
                    context.getString(R.string.feed_icon_coachmark_content),
                )
            ),
        )

        feedCoachMarkToggle = true
    }

    private fun isABTestRemoteConfigEnabled() =
        RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(FEED_NEW_ICON) == FEED_NEW_ICON

    companion object {
        private const val PREF_KEY_FEED_COACHMARK = "PREF_KEY_FEED_COACHMARK"
        private const val PREF_KEY_FEED_COACHMARK_SHOWN = "PREF_KEY_FEED_COACHMARK_SHOWN"
    }
}
