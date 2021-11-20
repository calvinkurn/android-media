package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetCardBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetCard
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCardListener
import timber.log.Timber

class CMHomeWidgetCardViewHolder(
    binding: LayoutCmHomeWidgetCardBinding,
    listener: CMHomeWidgetCardListener
) : AbstractViewHolder<CMHomeWidgetCard>(binding.root) {
    override fun bind(item: CMHomeWidgetCard) {
        Timber.d("CMHomeWidgetCardViewHolder")
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_card
    }

}