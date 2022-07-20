package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHtwdViewAllCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener

class CMHomeWidgetViewAllCardViewHolder(
    private val binding: LayoutCmHtwdViewAllCardBinding,
    private val listener: CMHomeWidgetViewAllCardListener
) : AbstractViewHolder<CMHomeWidgetViewAllCardData>(binding.root) {
    override fun bind(dataItem: CMHomeWidgetViewAllCardData) {
        with(binding)
        {
            cmHomeWidgetViewAllCard.backgroundView.setImageResource(R.drawable.ic_cm_home_widget_graphic_element_white)
            cmHomeWidgetViewAllCard.description = dataItem.description.toString()
            cmHomeWidgetViewAllCard.setCta(dataItem.label.toString())
            root.setOnClickListener {
                listener.onViewAllCardClick(dataItem)
            }
            root.post {
                val height = listener.getProductCardHeight()
                if (height != 0) {
                    val layoutParams: ViewGroup.LayoutParams = root.layoutParams
                    layoutParams.height = height
                    root.layoutParams = layoutParams
                }
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_htwd_view_all_card
        const val RATIO_WIDTH = 0.367
    }

}