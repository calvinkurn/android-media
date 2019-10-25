package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.design.widget.Snackbar
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.BannerOrganicDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.ProductBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.SeeMoreBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactoryImpl
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class BannerOrganicViewHolder(itemView: View, val homeCategoryListener: HomeCategoryListener, countDownListener: CountDownView.CountDownListener)
    : DynamicChannelViewHolder(itemView, homeCategoryListener, countDownListener) {

    override fun getViewHolderClassName(): String {
        return BannerOrganicViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTracking.eventClickSeeAllBannerMixChannel(itemView.context, channel.id, channel.header.name)
    }

    val CTA_MODE_MAIN = "main"
    val CTA_MODE_TRANSACTION = "transaction"
    val CTA_MODE_INVERTED = "inverted"
    val CTA_MODE_DISABLED = "disabled"
    val CTA_MODE_ALTERNATE = "alternate"

    val CTA_TYPE_FILLED = "filled"
    val CTA_TYPE_GHOST = "ghost"
    val CTA_TYPE_TEXT = "text_only"

    val BLUE = "blue"
    val YELLOW = "yellow"
    val RED = "red"
    val GREEN = "green"

    var bannerTitle = itemView.findViewById<Typography>(R.id.banner_title)
    var bannerDescription = itemView.findViewById<Typography>(R.id.banner_description)
    var bannerUnifyButton = itemView.findViewById<UnifyButton>(R.id.banner_button)
    var bannerImage = itemView.findViewById<ImageView>(R.id.banner_image)
    var backgroundBanner = itemView.findViewById<ContainerUnify>(R.id.backgroundBanner)
    val recyclerView = itemView.findViewById<RecyclerView>(R.id.dc_banner_rv)

    companion object {
        val TYPE_CAROUSEL = "carousel"
        val TYPE_NON_CAROUSEL = "non carousel"
        @LayoutRes
        val LAYOUT = R.layout.home_dc_banner_recyclerview
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        clearItemRecyclerViewDecoration(recyclerView)
        mappingBackgroundContainerUnify(channel)
        mappingView(channel)
        valuateRecyclerViewDecoration(channel)
    }

    private fun valuateRecyclerViewDecoration(channel: DynamicHomeChannel.Channels) {
        when(channel.layout) {
            DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> {
                recyclerView.layoutManager = GridLayoutManager(
                        itemView.context,
                        3
                )
                /**
                 * Add margin for recyclerview on for non-carousel banner
                 */
                val param = recyclerView.layoutParams as LinearLayout.LayoutParams
                param.setMargins( itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_16),
                        param.topMargin,
                        itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_16),
                        param.bottomMargin
                )
                recyclerView.layoutParams = param
            }
            DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL -> {
                if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(BannerOrganicDecoration())
                recyclerView.layoutManager = LinearLayoutManager(
                        itemView.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )

                /**
                 * Make recyclerview to fill viewport width
                 */
                val param = recyclerView.layoutParams as LinearLayout.LayoutParams
                param.setMargins( 0,
                        param.topMargin,
                        0,
                        param.bottomMargin
                )
                recyclerView.layoutParams = param
            }
        }
    }

    private fun mappingView(channel: DynamicHomeChannel.Channels) {
        val bannerItem = channel.banner
        val visitables = mappingVisitablesFromChannel(channel)
        val ctaData = channel.banner.cta

        mappingCtaButton(ctaData)

        bannerTitle.text = bannerItem.title
        bannerDescription.text = bannerItem.description
        val textColor = if(bannerItem.textColor == null) ContextCompat.getColor(channelTitle.context, R.color.Neutral_N50) else Color.parseColor(bannerItem.textColor)
        bannerTitle.setTextColor(textColor)
        bannerDescription.setTextColor(textColor)

        Glide.with(itemView.context)
                .load(bannerItem.imageUrl)
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .into(getRoundedImageViewTarget(bannerImage, 24f))

        recyclerView.adapter = BannerItemAdapter(
                BannerMixTypeFactoryImpl(homeCategoryListener),
                channel.layout,
                channel.grids,
                channel,
                homeCategoryListener,
                visitables
        )

        itemView.setOnClickListener {
            HomePageTracking.eventClickBannerChannelMix(itemView.context, channel)
            homeCategoryListener.onSectionItemClicked(channel.banner.applink)
        }

        bannerUnifyButton.setOnClickListener {
            HomePageTracking.eventClickBannerButtonChannelMix(itemView.context, channel)
            if (ctaData.couponCode.isEmpty()) {
                homeCategoryListener.onSectionItemClicked(channel.banner.applink)
            } else {
                copyCoupon(itemView, ctaData)
            }
        }
    }

    private fun mappingVisitablesFromChannel(channel: DynamicHomeChannel.Channels): MutableList<Visitable<BannerMixTypeFactory>> {
        val visitables: MutableList<Visitable<BannerMixTypeFactory>> = channel.grids.map {
            ProductBannerMixDataModel(it, channel)
        }.toMutableList()

        if (isHasSeeMoreApplink(channel) && getLayoutType(channel) == TYPE_BANNER_CAROUSEL) {
            visitables.add(SeeMoreBannerMixDataModel(
                    channel
            ))
        }
        return visitables
    }

    private fun mappingBackgroundContainerUnify(channel: DynamicHomeChannel.Channels) {
        backgroundBanner.setContainerColor(
                when (channel.header.backColor) {
                    RED -> ContainerUnify.RED
                    BLUE -> ContainerUnify.BLUE
                    YELLOW -> ContainerUnify.YELLOW
                    GREEN -> ContainerUnify.GREEN
                    else -> ContainerUnify.RED
                }
        )
    }

    private fun copyCoupon(view: View, cta: DynamicHomeChannel.CtaData) {
        val clipboard = view.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Coupon Code", cta.couponCode)
        clipboard.primaryClip = clipData

        Toaster.showNormal(view.parent as ViewGroup,
                getString(R.string.discovery_home_toaster_coupon_copied),
                Snackbar.LENGTH_LONG)
    }

    private fun mappingCtaButton(cta: DynamicHomeChannel.CtaData) {
        if (cta.text.isEmpty()) {
            bannerUnifyButton.visibility = View.GONE
            return
        }
        bannerUnifyButton.visibility = View.VISIBLE
        when (cta.mode) {
            CTA_MODE_MAIN -> bannerUnifyButton.buttonType = UnifyButton.Type.MAIN
            CTA_MODE_TRANSACTION -> bannerUnifyButton.buttonType = UnifyButton.Type.TRANSACTION
            CTA_MODE_INVERTED -> bannerUnifyButton.isInverse = true
            CTA_MODE_DISABLED -> bannerUnifyButton.isEnabled = false
            CTA_MODE_ALTERNATE -> bannerUnifyButton.isEnabled = false
        }

        when (cta.type) {
            CTA_TYPE_FILLED -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.FILLED
            CTA_TYPE_GHOST -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.GHOST
            CTA_TYPE_TEXT -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            else -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.FILLED
        }

        bannerUnifyButton.text = cta.text
    }

    private fun clearItemRecyclerViewDecoration(itemRecyclerView: RecyclerView) {
        while (itemRecyclerView.itemDecorationCount > 0) {
            itemRecyclerView.removeItemDecorationAt(0)
        }
    }

    class SeeMoreItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val seeMoreCard: CardView by lazy { view.findViewById<CardView>(R.id.card_see_more_banner_mix) }
    }

    private fun getRoundedImageViewTarget(imageView: ImageView, radius: Float): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                circularBitmapDrawable.cornerRadius = radius
                imageView.setImageDrawable(circularBitmapDrawable)
            }
        }
    }

}