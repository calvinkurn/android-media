package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetViewAllCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener

class CMHomeWidgetViewAllCardViewHolder(
    private val binding: LayoutCmHomeWidgetViewAllCardBinding,
    private val listener: CMHomeWidgetViewAllCardListener
) : AbstractViewHolder<CMHomeWidgetViewAllCardData>(binding.root) {
    override fun bind(dataItem: CMHomeWidgetViewAllCardData) {
        with(binding)
        {
            cmHomeWidgetCardViewAll.description = dataItem.description.toString()
            cmHomeWidgetCardViewAll.setCta(dataItem.label.toString())
            root.setOnClickListener {
                listener.onViewAllCardClick(dataItem)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_view_all_card
    }

}