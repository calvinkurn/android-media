package com.tokopedia.notifcenter.view.customview

import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

object NotifCenterBadgeCounterUtil {

    private const val MAX_BADGE_STRING = "99+"

    fun setBadgeCounter(badge: TextView?, count: Int?) {
        if (count == null || count <= 0) {
            badge?.hide()
        } else {
            badge?.show()
            badge?.text = getFormattedBadgeCounter(count)
        }
    }

    private fun getFormattedBadgeCounter(badgeCount: Int): CharSequence {
        return if (badgeCount > 99) {
            MAX_BADGE_STRING
        } else {
            badgeCount.toString()
        }
    }

}
