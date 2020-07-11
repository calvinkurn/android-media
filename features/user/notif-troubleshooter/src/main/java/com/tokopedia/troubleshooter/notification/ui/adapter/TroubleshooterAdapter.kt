package com.tokopedia.troubleshooter.notification.ui.adapter

import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView.Companion.addTickerMessage
import com.tokopedia.troubleshooter.notification.util.dropFirst
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Channel as Channel
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.PushNotification as PushNotification
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Ringtone as Ringtone
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

    private fun setStatus(state: ConfigState, isSuccess: Boolean): ConfigUIView? {
        val status = if (isSuccess) Success else Error
        return configUIViewByState(state)?.also { it.status = status }
    }

    fun setRingtoneStatus(ringtone: Uri?, isSuccess: Boolean) {
        setStatus(Ringtone, isSuccess)?.let {
            it.ringtone = ringtone
        }
        notifyDataSetChanged()
    }

    fun updateStatus(state: ConfigState, isSuccess: Boolean) {
        setStatus(state, isSuccess)
        notifyDataSetChanged()
    }

    fun updateTroubleshootMessage(message: String) {
        configUIViewByState(PushNotification)?.let {
            it.message = message
        }
        notifyItemChanged(0)
    }

    fun isTroubleshootError() {
        updateStatus(PushNotification, false)
    }

    fun hideNotificationChannel() {
        configUIViewByState(Channel)?.let {
            it.visibility = false
        }
        notifyDataSetChanged()
    }

    fun addTicker(message: String) {
        removeTicker()
        addElement(TICKER_INDEX, addTickerMessage(message))
    }

    fun removeTicker() {
        if (visitables.size > 0 && visitables.first() is TickerUIView) {
            visitables.dropFirst()
            notifyItemRemoved(TICKER_INDEX)
        }
    }

    companion object {
        private const val TICKER_INDEX = 0
    }

}