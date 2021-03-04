package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Cta
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Header
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class DynamicChannelThematicViewHolder(
        view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelDataModel>(view) {

    private val mainContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_thematic_main_container)
    private val headerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_header_main_container)
    private val headerTitle = itemView.findViewById<Typography>(R.id.dc_header_title)
    private val headerCountDown = itemView.findViewById<CountDownView>(R.id.dc_header_count_down)
    private val headerActionText = itemView.findViewById<AppCompatTextView>(R.id.dc_header_action_text)
    private val bannerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_thematic_banner_container)
    private val bannerTitle = itemView.findViewById<Typography>(R.id.dc_thematic_banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.dc_thematic_banner_description)
    private val bannerButton = itemView.findViewById<UnifyButton>(R.id.dc_thematic_banner_button)
    private val bannerImage = itemView.findViewById<AppCompatImageView>(R.id.dc_thematic_banner_image)

    private val contentList = itemView.findViewById<CarouselProductCardView>(R.id.dc_thematic_rv)

    override fun bind(element: DynamicChannelDataModel?) {
        element?.run {
            dcEventHandler.mixBannerImpression(dynamicChannelData.channel)
            dcEventHandler.mixImageImpression(dynamicChannelData.channel)
            setupHeader(dynamicChannelData.channel.header)
            setupBanner(dynamicChannelData.channel.banner, dynamicChannelData.channel)
            setupContent(dynamicChannelData.channel)
        }
    }

    private fun setupHeader(header: Header?) {
        if (header != null && header.name.isNotEmpty()) {
            mainContainer.setMargin(0, itemView.context.resources.getDimensionPixelSize(R.dimen.dp_20), 0, 0)
            headerContainer.visibility = View.VISIBLE
            headerTitle.text = header.name
            headerCountDown.visibility = View.GONE

            if (header.applink.isNotEmpty()) {
                headerActionText.visibility = View.VISIBLE
                headerActionText.setOnClickListener(dcEventHandler.onClickMixActionText(header.applink))
            } else {
                headerActionText.visibility = View.GONE
            }

        } else {
            mainContainer.setMargin(0, itemView.context.resources.getDimensionPixelSize(R.dimen.dp_6), 0, 0)
            headerContainer.visibility = View.GONE
        }
    }

    private fun setupBannerButton(cta: Cta, channelData: Channel) {
        if (cta.text.isNotEmpty()) {
            bannerButton.visibility = View.VISIBLE
            bannerButton.setOnClickListener(dcEventHandler.onClickMixBanner(channelData))

            when (cta.mode) {
                DynamicChannelIdentifiers.CTA_MODE_MAIN -> bannerButton.buttonType = UnifyButton.Type.MAIN
                DynamicChannelIdentifiers.CTA_MODE_TRANSACTION -> bannerButton.buttonType = UnifyButton.Type.TRANSACTION
                DynamicChannelIdentifiers.CTA_MODE_INVERTED -> bannerButton.isInverse = true
                DynamicChannelIdentifiers.CTA_MODE_DISABLED -> bannerButton.isEnabled = false
                DynamicChannelIdentifiers.CTA_MODE_ALTERNATE -> bannerButton.isEnabled = false
            }

            when (cta.type) {
                DynamicChannelIdentifiers.CTA_TYPE_FILLED -> bannerButton.buttonVariant = UnifyButton.Variant.FILLED
                DynamicChannelIdentifiers.CTA_TYPE_GHOST -> bannerButton.buttonVariant = UnifyButton.Variant.GHOST
                DynamicChannelIdentifiers.CTA_TYPE_TEXT -> bannerButton.buttonVariant = UnifyButton.Variant.TEXT_ONLY
                else -> bannerButton.buttonVariant = UnifyButton.Variant.FILLED
            }

            bannerButton.text = cta.text
        } else {
            bannerButton.visibility = View.GONE
        }
    }

    private fun setupBanner(banner: Banner?, channelData: Channel) {
        if (banner != null && banner.imageUrl.isNotEmpty()) {
            bannerContainer.visibility = View.VISIBLE

            if (banner.applink.isNotEmpty()) {
                bannerContainer.setOnClickListener(dcEventHandler.onClickMixBanner(channelData))
            }

            bannerTitle.apply {
                text = banner.title
                setTextColor(Color.parseColor(banner.textColor))
            }

            bannerDescription.apply {
                text = banner.description
                setTextColor(Color.parseColor(banner.textColor))
            }

            if (banner.cta != null) {
                setupBannerButton(banner.cta, channelData)
            }

            Glide.with(itemView.context)
                    .asBitmap()
                    .load(banner.imageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .into(OfficialStoreImageHelper.getRoundedImageViewTarget(bannerImage, bannerImageCornerRadius))
        } else {
            bannerContainer.visibility = View.GONE
        }
    }

    private fun setupContent(channelData: Channel) {
        if (!channelData.grids.isNullOrEmpty()) {
            mainContainer.visibility = View.VISIBLE

            contentList?.bindCarouselProductCardViewGrid(
                    productCardModelList = createProductCardModelList(channelData),
                    carouselProductCardOnItemClickListener = object: CarouselProductCardListener.OnItemClickListener {
                        override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            dcEventHandler.onClickMixImage(channelData, carouselProductCardPosition).onClick(contentList)
                        }
                    }
            )
        } else {
            mainContainer.visibility = View.GONE
        }
    }

    private fun createProductCardModelList(channelData: Channel): List<ProductCardModel> {
        return channelData.grids?.map {
            ProductCardModel(
                productImageUrl = it?.imageUrl ?: "",
                productName = it?.name ?: "",
                formattedPrice = it?.price ?: "",
                freeOngkir = ProductCardModel.FreeOngkir(
                        it?.freeOngkir?.isActive ?: false,
                        it?.freeOngkir?.imageUrl ?: ""
                ),
                slashedPrice = it?.slashedPrice ?: "",
                discountPercentage = it?.discount ?: ""
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_thematic_main

        private const val bannerImageCornerRadius = 16f
    }
}
