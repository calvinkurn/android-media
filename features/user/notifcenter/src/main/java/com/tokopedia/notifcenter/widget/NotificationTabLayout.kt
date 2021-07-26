package com.tokopedia.notifcenter.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.consts.Resources

class NotificationTabLayout(val context: Context) {

    fun init(title: String): View? {
        val customView = LayoutInflater.from(context).inflate(R.layout.item_notification_tab_title, null)
        val titleView = customView.findViewById<TextView>(R.id.title)
        titleView.text = title
        return customView
    }

    fun selected(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(context, Resources.Green_G600))
    }

    fun unselected(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(context, Resources.Neutral_N200))
    }

    fun removeDot(customView: View?) {
        val notif = customView?.findViewById<View>(R.id.circle)
        notif?.hide()
    }

}