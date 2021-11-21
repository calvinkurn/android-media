package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetCard
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCardListener
import timber.log.Timber

class CMHomeWidgetCardViewHolder(
    private val binding: LayoutCmHomeWidgetCardBinding,
    private val listener: CMHomeWidgetCardListener
) : AbstractViewHolder<CMHomeWidgetCard>(binding.root) {
    override fun bind(item: CMHomeWidgetCard) {
        binding.tvCmHomeWidgetCardDesc.text = item.description
        binding.tvCmHomeWidgetCardLabel.text = item.label
        binding.root.setOnClickListener {
            listener.onCardClick(item)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_card
    }

}