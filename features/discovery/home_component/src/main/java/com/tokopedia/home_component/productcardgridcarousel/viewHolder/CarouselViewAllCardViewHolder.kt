package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.util.getGradientBackgroundViewAllWhite
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.viewallcard.ViewAllCard
import com.tokopedia.viewallcard.ViewAllCard.Companion.MODE_COLOR
import com.tokopedia.viewallcard.ViewAllCard.Companion.MODE_NORMAL

/**
 * created by Dhaba
 */
class CarouselViewAllCardViewHolder(
    view: View,
    private val channels: ChannelModel
) : AbstractViewHolder<CarouselViewAllCardDataModel>(view) {

    private val card: ViewAllCard by lazy { view.findViewById(R.id.card_view_all_banner) }

    override fun bind(element: CarouselViewAllCardDataModel) {
        val isGradientWhite = getGradientBackgroundViewAllWhite(element.channelViewAllCard.gradientColor)
        when(element.channelViewAllCard.contentType) {
            CONTENT_TITLE_AS_STRING -> renderTypeTitleAsString(element, isGradientWhite)
            CONTENT_TITLE_AS_INTEGER -> renderTypeTitleAsInteger(element, isGradientWhite)
            CONTENT_SINGLE_IMAGE -> renderTypeSingleImage(element, isGradientWhite)
            else -> renderTypeTitleAsString(element, isGradientWhite)
        }
        //set foreground to selectableItemBackground
        val outValue = TypedValue()
        itemView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        card.cardView.foreground = itemView.context.getDrawable(outValue.resourceId)

        card.setOnClickListener {
            element.listener.onSeeMoreCardClicked(applink = element.applink, channel = channels)
        }
    }

    private fun renderTypeSingleImage(element: CarouselViewAllCardDataModel, isGradientWhite: Boolean) {
        card.imageDrawable = itemView.context.getDrawable(R.drawable.ic_graphic_element_green)
        card.imageView.loadImage(element.channelViewAllCard.imageUrl)
        card.description = element.channelViewAllCard.description
        card.setCta(itemView.context.getString(R.string.lihat_semua))
        if(isGradientWhite) {
            card.mode = MODE_NORMAL
            card.backgroundView.setImageResource(R.drawable.ic_graphic_element_green)
        }
        else {
            card.mode = MODE_COLOR
            card.containerView.setGradientBackground(element.channelViewAllCard.gradientColor)
            card.backgroundView.setImageResource(R.drawable.ic_graphic_element_white)
        }
    }

    private fun renderTypeTitleAsString(element: CarouselViewAllCardDataModel, isGradientWhite: Boolean) {
        card.title = element.channelViewAllCard.title
        card.description = element.channelViewAllCard.description
        card.setCta(itemView.context.getString(R.string.lihat_semua))
        if(isGradientWhite) {
            card.mode = MODE_NORMAL
            card.backgroundView.setImageResource(R.drawable.ic_graphic_element_green)
        }
        else {
            card.mode = MODE_COLOR
            card.containerView.setGradientBackground(element.channelViewAllCard.gradientColor)
            card.backgroundView.setImageResource(R.drawable.ic_graphic_element_white)
        }
    }

    private fun renderTypeTitleAsInteger(element: CarouselViewAllCardDataModel, isGradientWhite: Boolean) {
        card.title = element.channelViewAllCard.title
        card.description = element.channelViewAllCard.description
        card.isTitleNumberStyle = true
        card.setCta(itemView.context.getString(R.string.lihat_semua))
        if(isGradientWhite) {
            card.mode = MODE_NORMAL
            card.backgroundView.setImageResource(R.drawable.ic_graphic_element_green)
        }
        else {
            card.mode = MODE_COLOR
            card.containerView.setGradientBackground(element.channelViewAllCard.gradientColor)
            card.backgroundView.setImageResource(R.drawable.ic_graphic_element_white)
        }
    }

    companion object{
        val LAYOUT = R.layout.home_banner_item_carousel_view_all
        private const val CONTENT_TITLE_AS_STRING = "title-as-string"
        private const val CONTENT_TITLE_AS_INTEGER = "title-as-integer"
        private const val CONTENT_SINGLE_IMAGE = "single-image"
        const val CONTENT_DEFAULT = "default"
        const val DEFAULT_VIEW_ALL_ID = "0"
    }
}