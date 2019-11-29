package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.EmptyState

class EmptyDataViewHolder(view: View): AbstractViewHolder<EmptyState>(view) {

    private val imgState = view.findViewById<ImageView>(R.id.img_state)
    private val txtMessage = view.findViewById<TextView>(R.id.txt_message)

    override fun bind(element: EmptyState) {
        imgState.setImageResource(element.icon)
        txtMessage.text = element.title
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_empty_state
    }

}