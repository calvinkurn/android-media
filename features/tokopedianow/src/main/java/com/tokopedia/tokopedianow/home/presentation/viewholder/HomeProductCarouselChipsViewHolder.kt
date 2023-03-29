package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductCarouselChipsBinding
import com.tokopedia.tokopedianow.home.presentation.view.HomeProductCarouselChipsView.HomeProductCarouselChipsViewListener
import com.tokopedia.utils.view.binding.viewBinding

class HomeProductCarouselChipsViewHolder(
    itemView: View,
    private val listener: HomeProductCarouselChipsViewListener?
): AbstractViewHolder<HomeProductCarouselChipsUiModel>(itemView) {

    private var binding: ItemTokopedianowProductCarouselChipsBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_carousel_chips
    }

    override fun bind(uiModel: HomeProductCarouselChipsUiModel) {
        binding?.productCarouselChipView?.setListener(listener)
        binding?.productCarouselChipView?.bind(
            channelId = uiModel.id,
            header = uiModel.header,
            chipList = uiModel.chipList,
            carouselItems = uiModel.carouselItemList,
            state = uiModel.state
        )
    }

    override fun bind(uiModel: HomeProductCarouselChipsUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && uiModel != null) {
            binding?.productCarouselChipView?.bindCarouselItemList(
                carouselItemList = uiModel.carouselItemList,
                state = uiModel.state
            )
        }
    }
}
