package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.CardUnify2

class CarouselSeeMorePdpViewHolder(view: View,
                                   private val channels: ChannelModel,
                                   private val cardInteraction: Boolean = false,
) : AbstractViewHolder<CarouselSeeMorePdpDataModel>(view){

    private val card: CardUnify2 by lazy { view.findViewById<CardUnify2>(R.id.card_see_more_banner_mix) }
    private val bannerBackgroundImage: ImageView by lazy { view.findViewById<ImageView>(R.id.background_banner_mix_more)}

    override fun bind(element: CarouselSeeMorePdpDataModel) {
        card.animateOnPress = if(cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
        loadImage(element)
        itemView.setOnClickListener {
            element.listener.onSeeMoreCardClicked(applink = element.applink, channel = channels)
        }
    }

    private fun loadImage(element: CarouselSeeMorePdpDataModel) {
        if(element.backgroundImage.isEmpty()) {
            bannerBackgroundImage.hide()
        } else {
            bannerBackgroundImage.show()
            bannerBackgroundImage.loadImageWithoutPlaceholder(element.backgroundImage)
        }
    }

    companion object{
        val LAYOUT = R.layout.home_banner_item_carousel_see_more
    }
}
