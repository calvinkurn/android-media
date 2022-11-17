package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselBannerCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselCampaignCardDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by dhaba
 */
class CarouselBannerItemViewHolder (
    view: View,
    private val cardInteraction: Boolean = false,
) : AbstractViewHolder<CarouselBannerCardDataModel>(view) {
    private lateinit var cardContainer: CardUnify2
    private lateinit var cardImage: ImageUnify

    override fun bind(element: CarouselBannerCardDataModel) {
        initView()
        bindView(element)
    }

    private fun initView() {
        cardContainer = itemView.findViewById<CardUnify2?>(R.id.card_banner).apply {
            cardType = CardUnify2.TYPE_CLEAR
            animateOnPress = if(cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
        }
        cardImage = itemView.findViewById(R.id.image_banner)
    }

    private fun bindView(element: CarouselBannerCardDataModel) {
        cardImage.setImageUrl(element.imageUrl)
    }

    companion object {
        val LAYOUT = R.layout.home_banner_item_banner
    }
}
