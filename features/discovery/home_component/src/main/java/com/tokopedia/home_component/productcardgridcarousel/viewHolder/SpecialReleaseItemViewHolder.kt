package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseItemBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel.Companion.CAROUEL_ITEM_SPECIAL_RELEASE_TIMER_BIND
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
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

            if (element.grid.benefit.value.isNotEmpty()) {
                binding?.specialReleasePrice?.text = element.grid.benefit.value
                binding?.specialReleasePrice?.visible()
            } else {
                binding?.specialReleasePrice?.gone()
            }

            if (element.grid.shop.shopName.isNotEmpty()) {
                binding?.specialReleaseShopName?.text = element.grid.shop.shopName
                binding?.specialReleaseShopName?.visible()
            } else {
                binding?.specialReleaseShopName?.gone()
            }

            if (element.grid.label.isNotEmpty()) {
                binding?.specialReleaseTag?.text = element.grid.label
                binding?.specialReleaseTag?.visible()
            } else {
                binding?.specialReleaseTag?.gone()
            }

            binding?.specialReleaseTimer?.setTimer(
                channels.channelConfig.serverTimeOffset,
                element.grid.expiredTime ?: ""
            )

            binding?.specialReleaseShopImage?.loadImage(
                url = element.grid.shop.shopProfileUrl
            )

            element.grid.badges.firstOrNull()?.let {
                binding?.specialReleaseShopBadge?.loadImage(
                    url = it.imageUrl
                )
            }

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

    override fun bind(element: CarouselSpecialReleaseDataModel?, payloads: MutableList<Any>) {
        if (payloads.size > 0) {
            val payload = payloads[0]
            if (payload is Bundle) {
                val bundle = payload as Bundle
                if (bundle.getBoolean(CAROUEL_ITEM_SPECIAL_RELEASE_TIMER_BIND, false)) {
                    adjustItemGridTimer(element)
                    return
                }
            }
        }
    }

    private fun adjustItemGridTimer(element: CarouselSpecialReleaseDataModel?) {
        element?.let {
            binding?.specialReleaseTimer?.setTimer(
                channels.channelConfig.serverTimeOffset,
                element.grid.expiredTime ?: ""
            )
        }
    }
}