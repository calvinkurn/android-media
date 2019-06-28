package com.tokopedia.navigation.presentation.view.listener

/**
 * @author : Steven 12/04/19
 */
interface NotificationUpdateItemListener {

    fun itemClicked(notifId: String, adapterPosition: Int, needToResetCounter: Boolean, templateKey: String)
}