package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductCarouselChipsBinding
import com.tokopedia.utils.view.binding.viewBinding

class HomeProductCarouselChipsViewHolder(
    itemView: View
): AbstractViewHolder<HomeProductCarouselChipsUiModel>(itemView) {

    private var binding: ItemTokopedianowProductCarouselChipsBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_carousel_chips
    }

    override fun bind(uiModel: HomeProductCarouselChipsUiModel) {
        binding?.productCarouselChipView?.bind(
            header = uiModel.header,
            chipList = uiModel.chipList,
            carouselItems = uiModel.carouselItemList,
            state = uiModel.state
        )
    }
}
