package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSpaceUiModel

class HomeLeftCarouselProductCardSpaceViewHolder(
    itemView: View
): AbstractViewHolder<HomeLeftCarouselProductCardSpaceUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_product_card_space
    }

    override fun bind(element: HomeLeftCarouselProductCardSpaceUiModel) {
        /* nothing to do */
    }
}