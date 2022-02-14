package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseItemBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by devarafikry on 08/02/22.
 */
class SpecialReleaseItemViewHolder(
    private val view: View,
    private val channels: ChannelModel
) : AbstractViewHolder<CarouselSpecialReleaseDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.home_component_special_release_item
    }
    private var binding: HomeComponentSpecialReleaseItemBinding? by viewBinding()

    override fun bind(element: CarouselSpecialReleaseDataModel?) {
        if (element != null) {
            view.addOnImpressionListener(element) {
                element.listener.onProductCardImpressed(
                    channel = channels,
                    channelGrid = element.grid,
                    position = adapterPosition
                )
            }

            binding?.specialReleasePrice?.text = element.grid.benefit.value
            binding?.specialReleaseShopName?.text = element.grid.shop.shopName
            binding?.specialReleaseTag?.text = element.grid.label

            binding?.specialReleaseTimer?.setTimer(
                channels.channelConfig.serverTimeOffset,
                element.grid.expiredTime ?: ""
            )

            binding?.specialReleaseShopImage?.loadImage(
                url = element.grid.shop.shopProfileUrl
            )

            binding?.specialReleaseBrandCard?.loadImage(
                url = element.grid.imageUrl
            )

            binding?.root?.setOnClickListener {
                element.listener.onProductCardClicked(
                    channel = channels,
                    channelGrid = element.grid,
                    position = adapterPosition,
                    applink = element.grid.shop.shopApplink
                )
            }
        }
    }
}