package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselProductCardSpaceBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSpaceUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselProductCardSpaceViewHolder(
    itemView: View,
    private val listener: HomeLeftCarouselProductCardSpaceListener? = null
): AbstractViewHolder<HomeLeftCarouselProductCardSpaceUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_product_card_space
    }

    private val binding: ItemTokopedianowHomeLeftCarouselProductCardSpaceBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselProductCardSpaceUiModel) {
        binding?.root?.setOnClickListener {
            listener?.onProductCardSpaceClicked(
                appLink = element.appLink,
                channelId = element.channelId,
                headerName = element.channelHeaderName
            )
        }
    }

    interface HomeLeftCarouselProductCardSpaceListener {
        fun onProductCardSpaceClicked(appLink: String, channelId: String, headerName: String)
    }
}