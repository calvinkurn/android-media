package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.view.View
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentOneDataModel
import kotlinx.android.synthetic.main.component_one_layout.view.*

class ComponentOneViewHolder(itemView: View) : AbstractViewHolder<ComponentOneDataModel>(itemView) {
    val textView = itemView.text_view


    companion object {
        val LAYOUT = R.layout.component_one_layout
    }

    override fun bindView(item: ComponentOneDataModel) {
        textView.text = item.name
    }
}