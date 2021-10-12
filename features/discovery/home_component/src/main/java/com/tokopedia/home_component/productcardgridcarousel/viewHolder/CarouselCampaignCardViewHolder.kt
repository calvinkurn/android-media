package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselCampaignCardDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify

/**
 * Created by yfsx on 11/10/21.
 */
class CarouselCampaignCardViewHolder(
    private val view: View,
    private val channels: ChannelModel
) : AbstractViewHolder<CarouselCampaignCardDataModel>(view) {
    private lateinit var cardContainer: CardUnify
    private lateinit var cardImage: ImageView

    override fun bind(element: CarouselCampaignCardDataModel?) {
        initView()
        bindView(element)
    }

    private fun initView() {
        cardContainer = itemView.findViewById(R.id.card_campaign)
        cardImage = itemView.findViewById(R.id.card_campaign_image)
    }

    private fun bindView(element: CarouselCampaignCardDataModel?) {
        element?.grid?.let {
            cardImage.loadImage(it.imageUrl)
            element.listener.onProductCardClicked(
                channel = channels,
                channelGrid = it,
                position = adapterPosition,
                applink = it.applink
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.home_banner_item_campaign_carousel
    }
}