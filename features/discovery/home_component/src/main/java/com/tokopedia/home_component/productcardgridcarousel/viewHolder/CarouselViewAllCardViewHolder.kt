package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.widget.FrameLayout
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
class CarouselViewAllCardViewHolder(
    view: View,
    private val channels: ChannelModel
) : AbstractViewHolder<CarouselViewAllCardDataModel>(view) {

    private val container: View by lazy { view.findViewById<ConstraintLayout>(R.id.container_banner_view_all) }
    private val imageHeading: ImageView by lazy { view.findViewById<ImageView>(R.id.iv_heading_view_all) }
    private val tvDcMix: TextView by lazy { view.findViewById<TextView>(R.id.tv_dc_mix_view_all) }
    private val tvTitle: TextView by lazy { view.findViewById<TextView>(R.id.tv_title_view_all) }
    private val tvTitleNumber: TextView by lazy { view.findViewById<TextView>(R.id.tv_title_number_view_all) }
    private val tvDescription: TextView by lazy { view.findViewById<TextView>(R.id.tv_description_view_all) }
    private val tvDescriptionNumber: TextView by lazy { view.findViewById<TextView>(R.id.tv_description_number_view_all) }
    private val imageChevronRight: IconUnify by lazy { view.findViewById<IconUnify>(R.id.iv_chevron_right_view_all) }
    private val tvDescriptionImage: TextView by lazy { view.findViewById<TextView>(R.id.tv_description_image_view_all) }
    private val imageGimmick: ImageView by lazy { view.findViewById<ImageView>(R.id.iv_gimmick_view_all) }
    private val containerCardImage: FrameLayout by lazy { view.findViewById<FrameLayout>(R.id.container_card_image) }

    override fun bind(element: CarouselViewAllCardDataModel) {
        val isGradientWhite = getGradientBackgroundViewAllWhite(element.channelViewAllCard.gradientColor)
        when(element.channelViewAllCard.contentType) {
            CONTENT_TITLE_AS_STRING -> renderTypeTitleAsString(element, isGradientWhite)
            CONTENT_TITLE_AS_INTEGER -> renderTypeTitleAsInteger(element, isGradientWhite)
            CONTENT_SINGLE_IMAGE -> renderTypeSingleImage(element, isGradientWhite)
        }
        container.setOnClickListener {
            element.listener.onSeeMoreCardClicked(applink = element.applink, channel = channels)
        }
    }

    private fun renderTypeSingleImage(element: CarouselViewAllCardDataModel, isGradientWhite: Boolean) {
        imageGimmick.loadImage(element.channelViewAllCard.imageUrl)
        tvDescriptionImage.text = element.channelViewAllCard.description
        tvTitle.gone()
        tvDescription.gone()
        containerCardImage.visible()
        tvDescriptionImage.visible()
        tvTitleNumber.gone()
        tvDescriptionNumber.gone()

        if(isGradientWhite) {
            container.setGradientBackground(arrayListOf())
            imageHeading.setImageResource(R.drawable.ic_graphic_element_green)
            tvDescriptionImage.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_G500))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_GN500))
        }
        else {
            container.setGradientBackground(element.channelViewAllCard.gradientColor)
            imageHeading.setImageResource(R.drawable.ic_graphic_element_white)
            tvDescriptionImage.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_N0))
        }
    }

    private fun renderTypeTitleAsString(element: CarouselViewAllCardDataModel, isGradientWhite: Boolean) {
        tvTitle.text = element.channelViewAllCard.title
        tvDescription.text = element.channelViewAllCard.description
        tvTitle.visible()
        tvDescription.visible()
        containerCardImage.gone()
        tvDescriptionImage.gone()
        tvTitleNumber.gone()
        tvDescriptionNumber.gone()

        if(isGradientWhite) {
            container.setGradientBackground(arrayListOf())
            imageHeading.setImageResource(R.drawable.ic_graphic_element_green)
            tvTitle.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDescription.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_G500))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_GN500))
        }
        else {
            container.setGradientBackground(element.channelViewAllCard.gradientColor)
            imageHeading.setImageResource(R.drawable.ic_graphic_element_white)
            tvTitle.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDescription.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_N0))
        }
    }

    private fun renderTypeTitleAsInteger(element: CarouselViewAllCardDataModel, isGradientWhite: Boolean) {
        tvTitleNumber.text = element.channelViewAllCard.title
        tvDescriptionNumber.text = element.channelViewAllCard.description
        tvTitleNumber.visible()
        tvDescriptionNumber.visible()
        containerCardImage.gone()
        tvDescriptionImage.gone()
        tvTitle.gone()
        tvDescription.gone()

        if(isGradientWhite) {
            container.setGradientBackground(arrayListOf())
            imageHeading.setImageResource(R.drawable.ic_graphic_element_green)
            tvTitleNumber.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDescriptionNumber.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N700))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_G500))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_GN500))
        }
        else {
            container.setGradientBackground(element.channelViewAllCard.gradientColor)
            imageHeading.setImageResource(R.drawable.ic_graphic_element_white)
            tvTitleNumber.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDescriptionNumber.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            tvDcMix.setTextColor(ContextCompat.getColor(container.context, R.color.Unify_N0))
            imageChevronRight.setImage(IconUnify.CHEVRON_RIGHT, ContextCompat.getColor(container.context, R.color.Unify_N0))
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