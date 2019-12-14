package com.tokopedia.notifcenter.presentation.adapter.viewholder.notificationupdate

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.domain.model.EmptyUpdateState

class EmptyStateViewHolder(view: View): AbstractViewHolder<EmptyUpdateState>(view) {

    private val imgState = view.findViewById<ImageView>(R.id.img_state)
    private val txtMessage = view.findViewById<TextView>(R.id.txt_message)

    override fun bind(element: EmptyUpdateState) {
        txtMessage.text = element.title
        imgState.setImageResource(element.icon)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_empty_state
    }

}