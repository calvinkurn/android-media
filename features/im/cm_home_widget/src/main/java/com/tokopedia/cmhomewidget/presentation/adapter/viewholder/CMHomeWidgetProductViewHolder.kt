package com.tokopedia.cmhomewidget.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetProductBinding
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProduct
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductListener
import timber.log.Timber

class CMHomeWidgetProductViewHolder(
    private val binding: LayoutCmHomeWidgetProductBinding,
    private val listener: CMHomeWidgetProductListener
) : AbstractViewHolder<CMHomeWidgetProduct>(binding.root) {
    override fun bind(item: CMHomeWidgetProduct) {
        binding.root.setOnClickListener {
            listener.onProductClick(item)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_cm_home_widget_product
    }
}