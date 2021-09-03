package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.util.getGradientBackgroundViewAllWhite
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * created by Dhaba
 */
class CarouselViewAllCardViewHolder(view: View,
                                    private val channels: ChannelModel
)
    : AbstractViewHolder<CarouselViewAllCardDataModel>(view){

    private val container: View by lazy { view.findViewById<ConstraintLayout>(R.id.container_banner_view_all) }
    private val imageHeading: ImageView by lazy { view.findViewById<ImageView>(R.id.iv_heading_view_all) }
    private val tvDcMix: TextView by lazy { view.findViewById<TextView>(R.id.tv_dc_mix_view_all) }
    private val tvTitle: TextView by lazy { view.findViewById<TextView>(R.id.tv_title_view_all) }
    private val tvDescription: TextView by lazy { view.findViewById<TextView>(R.id.tv_description_view_all) }
    private val imageChevronRight: IconUnify by lazy { view.findViewById<IconUnify>(R.id.iv_chevron_right_view_all) }
    private val cardImage: CardView by lazy { view.findViewById<CardView>(R.id.card_image_view_all) }
    private val tvDescriptionImage: TextView by lazy { view.findViewById<TextView>(R.id.tv_description_image_view_all) }
    private val imageGimmick: ImageView by lazy { view.findViewById<ImageView>(R.id.iv_gimmick_view_all) }

    override fun bind(element: CarouselViewAllCardDataModel) {
        tvTitle.text = element.channelViewAllCard.title
        tvDescription.text = element.channelViewAllCard.description
        if(element.channelViewAllCard.imageUrl.isEmpty()) {
            tvTitle.visible()
            tvDescription.visible()
            cardImage.gone()
            tvDescriptionImage.gone()
        }
        else {
            tvTitle.gone()
            tvDescription.gone()
            cardImage.visible()
            tvDescriptionImage.visible()
            imageGimmick.loadImage(element.channelViewAllCard.imageUrl)
            tvDescriptionImage.text = element.channelViewAllCard.description
        }
        val gradientWhite = getGradientBackgroundViewAllWhite(element.channelViewAllCard.gradientColor)
        if(gradientWhite) {
            imageHeading.setImageResource(R.drawable.ic_graphic_element_green)
            tvTitle.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDescription.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDescriptionImage.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_G500))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_GN500))
        }
        else {
            container.setGradientBackground(element.channelViewAllCard.gradientColor)
            imageHeading.setImageResource(R.drawable.ic_graphic_element_white)
            tvTitle.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDescription.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDescriptionImage.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_N0))
        }
        container.setOnClickListener {
            element.listener.onSeeMoreCardClicked(applink = element.applink, channel = channels)
        }
    }

    companion object{
        val LAYOUT = R.layout.home_banner_item_carousel_view_all
    }
}