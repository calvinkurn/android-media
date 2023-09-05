package com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.ShopHomeDisplayAdvanceCarouselBannerItemBinding
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify


class ShopHomeDisplayAdvanceCarouselBannerItemViewHolder(
    viewBinding: ShopHomeDisplayAdvanceCarouselBannerItemBinding,
    private val parentUiModel: ShopHomeDisplayWidgetUiModel
) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        private const val DEFAULT_RATIO = "1:1"
    }

    private val imageBanner: ImageUnify = viewBinding.imageBanner

    fun bind(uiModel: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        imageBanner.apply {
            val ratio = parentUiModel.header.ratio
            (imageBanner.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio =
                ratio.takeIf { it.isNotEmpty() } ?: DEFAULT_RATIO
            loadImage(uiModel.imageUrl)
        }
    }

}
