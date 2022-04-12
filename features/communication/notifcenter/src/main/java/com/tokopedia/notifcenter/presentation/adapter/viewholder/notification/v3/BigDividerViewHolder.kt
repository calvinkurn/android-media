package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener

class BigDividerViewHolder constructor(
        itemView: View?,
        private val adapterListener: NotificationAdapterListener?
) : AbstractViewHolder<BigDividerUiModel>(itemView) {

    private var container: FrameLayout? = itemView?.findViewById(R.id.fl_container)

    override fun bind(element: BigDividerUiModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            bindDividerMargin(element)
        }
    }

    override fun bind(element: BigDividerUiModel) {
        bindDividerMargin(element)
    }

    private fun bindDividerMargin(element: BigDividerUiModel) {
        val isPreviousItemNotification = adapterListener?.isPreviousItemNotification(adapterPosition)
        val topPadding = if (isPreviousItemNotification == true) {
            topPaddingNotification
        } else {
            0
        }
        container?.apply {
            setPadding(paddingLeft, topPadding, paddingRight, paddingBottom)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_big_divider
        private val topPaddingNotification = 8f.toPx().toInt()
    }
}