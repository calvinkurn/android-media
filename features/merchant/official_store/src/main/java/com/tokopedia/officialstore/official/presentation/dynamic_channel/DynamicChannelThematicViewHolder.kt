package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Cta
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Header
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class DynamicChannelThematicViewHolder(
        view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelViewModel>(view) {

    private val mainContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_thematic_main_container)
    private val headerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_header_main_container)
    private val headerTitle = itemView.findViewById<Typography>(R.id.dc_header_title)
    private val headerCountDown = itemView.findViewById<CountDownView>(R.id.dc_header_count_down)
    private val headerActionText = itemView.findViewById<Typography>(R.id.dc_header_action_text)
    private val bannerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_thematic_banner_container)
    private val bannerTitle = itemView.findViewById<Typography>(R.id.dc_thematic_banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.dc_thematic_banner_description)
    private val bannerButton = itemView.findViewById<UnifyButton>(R.id.dc_thematic_banner_button)
    private val bannerImage = itemView.findViewById<AppCompatImageView>(R.id.dc_thematic_banner_image)

    private val contentList = itemView.findViewById<RecyclerView>(R.id.dc_thematic_rv)

    override fun bind(element: DynamicChannelViewModel?) {
        element?.run {
            dcEventHandler.mixBannerImpression(dynamicChannelData)
            dcEventHandler.mixImageImpression(dynamicChannelData)
            setupHeader(dynamicChannelData.header)
            setupBanner(dynamicChannelData.banner, dynamicChannelData)
            setupContent(dynamicChannelData)
        }
    }

    private fun setupHeader(header: Header?) {
        if (header != null && header.name.isNotEmpty()) {
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
                    .load(banner.imageUrl)
                    .asBitmap()
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

            contentList.apply {
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ThematicListAdapter(channelData, dcEventHandler)
            }
        } else {
            mainContainer.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_thematic_main

        private const val bannerImageCornerRadius = 16f
    }
}
