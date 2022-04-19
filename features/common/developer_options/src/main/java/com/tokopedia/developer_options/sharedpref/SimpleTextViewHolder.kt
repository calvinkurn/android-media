package com.tokopedia.developer_options.sharedpref

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R

class SimpleTextViewHolder(itemView: View) : AbstractViewHolder<IdViewModel>(itemView) {

    private val tvName: TextView = itemView.findViewById(R.id.name)

    override fun bind(element: IdViewModel) {
        tvName.text = element.id
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_simple_text
    }
}