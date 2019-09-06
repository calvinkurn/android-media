package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics

/**
 * @author : Steven 12/04/19
 */
interface NotificationUpdateItemListener {

    fun itemClicked(notifId: String, adapterPosition: Int, needToResetCounter: Boolean, templateKey: String)

    fun getAnalytic(): NotificationUpdateAnalytics
}