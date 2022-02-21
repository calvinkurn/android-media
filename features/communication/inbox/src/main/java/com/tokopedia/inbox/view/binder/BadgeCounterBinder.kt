package com.tokopedia.inbox.view.binder

import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

object BadgeCounterBinder {

    fun bindBadgeCounter(badge: TextView?, count: Int?) {
        if (count == null || count <= 0) {
            badge?.hide()
        } else {
            badge?.show()
            badge?.text = getFormattedBadgeCounter(count)
        }
    }

    private fun getFormattedBadgeCounter(badgeCount: Int): CharSequence {
        return if (badgeCount > 99) {
            "99+"
        } else {
            badgeCount.toString()
        }
    }

}