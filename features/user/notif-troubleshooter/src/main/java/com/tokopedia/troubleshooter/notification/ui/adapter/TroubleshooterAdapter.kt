package com.tokopedia.troubleshooter.notification.ui.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView.Companion.addTickerMessage
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Categories as Categories
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.PushNotification as PushNotification
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState.Error as Error
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState.Success as Success

internal open class TroubleshooterAdapter(
        factory: TroubleshooterItemFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(factory) {

    private fun configUIView(): List<ConfigUIView>? {
        return visitables?.filterIsInstance(ConfigUIView::class.java)
    }

    private fun configUIViewByState(state: ConfigState): ConfigUIView? {
        return configUIView()?.first { it.state == state }
    }

    fun updateStatus(state: ConfigState, isSuccess: Boolean) {
        val status = if (isSuccess) Success else Error
        configUIView()?.forEach {
            if (it.state == state) {
                it.status = status
            }
        }
        notifyDataSetChanged()
    }

    fun updateTroubleshootMessage(message: String) {
        configUIViewByState(PushNotification)?.let {
            it.message = message
        }
        notifyDataSetChanged()
    }

    fun isTroubleshootError() {
        updateStatus(PushNotification, false)
    }

    fun hideNotificationCategory() {
        configUIViewByState(Categories)?.let {
            it.visibility = false
        }
        notifyDataSetChanged()
    }

    fun addTicker(message: String) {
        visitables.removeAll { it is TickerUIView }
        addElement(0, addTickerMessage(message))
    }

    fun removeTicker() {
        visitables.removeAll { it is TickerUIView }
        notifyItemChanged(0)
    }

}