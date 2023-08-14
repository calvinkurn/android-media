package com.tokopedia.shop.home.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.ShopHomeDisplayAdvanceCarouselBannerItemBinding
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify


class ShopHomeDisplayAdvanceCarouselBannerItemViewHolder(
    viewBinding: ShopHomeDisplayAdvanceCarouselBannerItemBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    private val imageBanner: ImageUnify = viewBinding.imageBanner

    fun bind(uiModel: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        imageBanner.loadImage(uiModel.imageUrl)
    }

}
