package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean

class EmptyDataStateViewHolder(view: View): AbstractViewHolder<NotificationEmptyStateViewBean>(view) {

    private val imgState = view.findViewById<ImageView>(R.id.img_state)
    private val txtMessage = view.findViewById<TextView>(R.id.txt_message)

    override fun bind(element: NotificationEmptyStateViewBean) {
        txtMessage.text = getString(element.title)
        imgState.setImageResource(element.icon)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_empty_state
    }

}