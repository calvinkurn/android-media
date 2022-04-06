package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselCampaignCardDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by yfsx on 11/10/21.
 */
class CarouselCampaignCardViewHolder(
    private val view: View,
    private val channels: ChannelModel
) : AbstractViewHolder<CarouselCampaignCardDataModel>(view) {
    private lateinit var cardContainer: CardUnify2
    private lateinit var cardImage: ImageView

    override fun bind(element: CarouselCampaignCardDataModel?) {
        initView()
        bindView(element)
    }

    private fun initView() {
        cardContainer = itemView.findViewById<CardUnify2?>(R.id.card_campaign).apply {
            cardType = CardUnify2.TYPE_CLEAR
            animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE
        }
        cardImage = itemView.findViewById(R.id.card_campaign_image)
    }

    private fun bindView(element: CarouselCampaignCardDataModel?) {
        element?.grid?.let { grid ->
            cardImage.loadImage(grid.imageUrl)
            element.listener.onProductCardImpressed(
                channel = channels,
                channelGrid = grid,
                position = adapterPosition
            )
            cardContainer.setOnClickListener {
                element.listener.onProductCardClicked(
                    channel = channels,
                    channelGrid = grid,
                    position = adapterPosition,
                    applink = grid.applink
                )
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.home_banner_item_campaign_carousel
    }
}