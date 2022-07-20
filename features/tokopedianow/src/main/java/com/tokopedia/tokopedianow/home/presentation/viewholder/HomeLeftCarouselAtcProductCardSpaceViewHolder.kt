package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcProductCardSpaceBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSpaceUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselAtcProductCardSpaceViewHolder(
    itemView: View,
    private val listener: HomeLeftCarouselAtcProductCardSpaceListener? = null
): AbstractViewHolder<HomeLeftCarouselAtcProductCardSpaceUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc_product_card_space
    }

    private val binding: ItemTokopedianowHomeLeftCarouselAtcProductCardSpaceBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselAtcProductCardSpaceUiModel) {
        binding?.root?.setOnClickListener {
            listener?.onProductCardSpaceClicked(
                appLink = element.appLink,
                channelId = element.channelId,
                headerName = element.channelHeaderName
            )
        }
    }

    interface HomeLeftCarouselAtcProductCardSpaceListener {
        fun onProductCardSpaceClicked(appLink: String, channelId: String, headerName: String)
    }
}