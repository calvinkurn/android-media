package com.tokopedia.sellerhome.view.customview

import android.view.MenuItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class NotificationDotBadge {

    fun showBadge(menuItem: MenuItem) {
        val actionView = menuItem.actionView
        val badge = actionView?.findViewById<ImageUnify>(R.id.imgSahNotifBadge)
        badge?.visible()
    }

    fun removeBadge(menuItem: MenuItem) {
        val actionView = menuItem.actionView
        val badge = actionView?.findViewById<ImageUnify>(R.id.imgSahNotifBadge)
        badge?.gone()
    }
}