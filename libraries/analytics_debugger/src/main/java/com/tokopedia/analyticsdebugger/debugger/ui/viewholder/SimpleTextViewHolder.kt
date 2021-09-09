package com.tokopedia.analyticsdebugger.debugger.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.IdViewModel

class SimpleTextViewHolder(itemView: View) : AbstractViewHolder<IdViewModel>(itemView) {

    private val tvName: TextView

    init {
        tvName = itemView.findViewById(R.id.name)
    }

    override fun bind(element: IdViewModel) {
        tvName.text = element.id
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_simple_text
    }
}
