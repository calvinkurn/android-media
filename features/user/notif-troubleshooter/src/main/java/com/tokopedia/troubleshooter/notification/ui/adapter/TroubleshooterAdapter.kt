package com.tokopedia.troubleshooter.notification.ui.adapter

import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.*
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.Ringtone
import com.tokopedia.troubleshooter.notification.ui.state.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.state.StatusState
import com.tokopedia.troubleshooter.notification.util.getWithIndex
import com.tokopedia.troubleshooter.notification.util.isNotNull

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

    fun addWarningTicker(ticker: TickerUIView) {
        visitables.removeAll { it is TickerUIView }
        visitables.add(visitables.size - 1, ticker)
        notifyDataSetChanged()
    }

    fun removeTickers() {
        visitables.removeAll { it is TickerUIView }
        notifyDataSetChanged()
    }

    fun status(state: StatusState) {
        if (visitables.isNotEmpty() && visitables.first() is StatusUIView) {
            visitables.removeAt(INDEX_STATUS)
        }
        visitables.add(INDEX_STATUS, StatusUIView(state))
        notifyItemInserted(INDEX_STATUS)
    }

    fun footerMessage(isDndEnabled: Boolean) {
        if (visitables.last() is FooterUIView) {
            visitables.removeAt(lastIndex)
        }

        visitables.add(FooterUIView(isDndEnabled))
        notifyDataSetChanged()
    }

    companion object {
        private const val INDEX_STATUS = 0
    }

}