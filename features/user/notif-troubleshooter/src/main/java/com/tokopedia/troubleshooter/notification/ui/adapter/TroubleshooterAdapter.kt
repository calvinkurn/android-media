package com.tokopedia.troubleshooter.notification.ui.adapter

import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.*
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.*
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState.Error
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState.Success
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView.Companion.ticker
import com.tokopedia.troubleshooter.notification.util.dropFirst
import com.tokopedia.troubleshooter.notification.util.getWithIndex

internal open class TroubleshooterAdapter(
        factory: TroubleshooterItemFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(factory) {

    private fun configUIView(): List<ConfigUIView>? {
        return visitables?.filterIsInstance(ConfigUIView::class.java)
    }

    private fun configUIViewByState(state: ConfigState): Pair<Int, ConfigUIView?>? {
        return configUIView()?.getWithIndex { it.state == state }
    }

    private fun setStatus(state: ConfigState, status: StatusState): ConfigUIView? {
        return configUIViewByState(state)?.second?.also { it.status = status }
    }

    fun setRingtoneStatus(ringtone: Uri?, status: StatusState) {
        setStatus(Ringtone, status)?.let {
            it.ringtone = ringtone
        }
        notifyDataSetChanged()
    }

    fun updateStatus(state: ConfigState, status: StatusState) {
        setStatus(state, status)
        notifyDataSetChanged()
    }

//    fun addMessage(state: ConfigState, message: String) {
//        val viewState = configUIViewByState(state)?: return
//        val index = viewState.first
//        val view = viewState.second
//        view?.let { it.message = message }
//        notifyItemChanged(index)
//    }

//    fun hideNotificationChannel() {
//        val index = configUIViewByState(Channel)?.first?: return
//        visitables.removeAt(index)
//        notifyItemRemoved(index)
//    }

    fun addTicker(message: CharSequence) {
        addElement(WarningTitleUIVIew("Rekomendasi lebih optimal"))
        addElement(ticker(message, "Perbaiki!"))
    }

    fun removeTicker() {
//        if (visitables.size > 0 && visitables.first() is TickerUIView) {
//            visitables.dropFirst()
//            notifyItemRemoved(TICKER_INDEX)
//        }
    }

}