package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerImageBinding
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding


class BannerViewHolder(itemView: View) : AbstractViewHolder<BannerCatalogUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_banner_image
    }

    private val binding by viewBinding<WidgetItemBannerImageBinding>()

    override fun bind(element: BannerCatalogUiModel) {
        binding?.content?.apply {
            loadImage(element.imageUrl)
            setBackgroundColor(element.widgetBackgroundColor ?: return)
            (layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = element.ratio.ratioName
        }
    }
}
