package com.tokopedia.troubleshooter.notification.ui.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterItemFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState

internal open class TroubleshooterAdapter(
        factory: TroubleshooterItemFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(factory) {

    fun updateStatus(state: ConfigState, isSuccess: Boolean) {
        val status = if (isSuccess) {
            StatusState.Success
        } else {
            StatusState.Error
        }

        visitables?.filterIsInstance(ConfigUIView::class.java)
                ?.forEach {
                    if (it.state == state) {
                        it.status = status
                    }
                }

        notifyDataSetChanged()
    }

}