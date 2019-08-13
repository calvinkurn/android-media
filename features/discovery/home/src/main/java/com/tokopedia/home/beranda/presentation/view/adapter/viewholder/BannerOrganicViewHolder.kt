package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.BannerOrganicDecoration
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class BannerOrganicViewHolder(itemView: View, val homeCategoryListener: HomeCategoryListener)
    : AbstractViewHolder<DynamicChannelViewModel>(itemView) {
    val CTA_MODE_MAIN = "main"
    val CTA_MODE_TRANSACTION = "transaction"
    val CTA_MODE_INVERTED = "inverted"
    val CTA_MODE_DISABLED = "disabled"
    val CTA_MODE_ALTERNATE = "alternate"

    val CTA_TYPE_FILLED = "filled"
    val CTA_TYPE_GHOST = "ghost"
    val CTA_TYPE_TEXT = "text_only"

    var itemRecyclerView = itemView.findViewById<RecyclerView>(R.id.dc_banner_rv)
    var headerTitle = itemView.findViewById<Typography>(R.id.title_dc)
    var seeAllText = itemView.findViewById<Typography>(R.id.see_all_dc)
    var bannerTitle = itemView.findViewById<Typography>(R.id.banner_title)
    var bannerDescription = itemView.findViewById<Typography>(R.id.banner_description)
    var bannerUnifyButton = itemView.findViewById<UnifyButton>(R.id.banner_button)
    var bannerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_banner_card)
    var titleDc = itemView.findViewById<Typography>(R.id.title_dc)
    var bannerImage = itemView.findViewById<ImageView>(R.id.banner_image)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_banner_organic
    }

    override fun bind(element: DynamicChannelViewModel) {
        val bannerItem = element.channel.banner
        clearItemRecyclerViewDecoration()

        mappingCtaButton(element.channel.banner.cta)

        bannerUnifyButton.setOnClickListener {
            HomePageTracking.eventClickBannerButtonChannelMix(itemView.context, element.channel)

            if (bannerItem.cta.couponCode.isEmpty()) {
                homeCategoryListener.onSectionItemClicked(element.channel.banner.applink)
            } else {
                copyCoupon(itemView, bannerItem.cta)
            }
        }

        itemView.setOnClickListener {
            HomePageTracking.eventClickBannerChannelMix(itemView.context, element.channel)
            homeCategoryListener.onSectionItemClicked(element.channel.banner.applink)
        }

        titleDc.text = element.channel.header.name
        if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(element.channel.header))) {
            seeAllText.visibility = View.VISIBLE
        } else {
            seeAllText.visibility = View.GONE
        }
        seeAllText.setOnClickListener {
            HomePageTracking.eventClickSeeAllBannerMixChannel(itemView.context, element.channel.id, element.channel.header.name)
            homeCategoryListener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(
                    element.channel.header
            ), element.channel.homeAttribution)
        }
        bannerTitle.text = bannerItem.title
        bannerDescription.text = bannerItem.description

        bannerTitle.setTextColor(Color.parseColor(bannerItem.textColor))
        bannerDescription.setTextColor(Color.parseColor(bannerItem.textColor))

        Glide.with(itemView.context)
                .load(bannerItem.imageUrl)
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .into(getRoundedImageViewTarget(bannerImage, 16f))
        /**
         * Set button
         */
        bannerUnifyButton.text = bannerItem.cta.text

        val itemAdapter = BannerItemAdapter(
                element.channel.layout,
                element.channel.grids,
                element.channel,
                homeCategoryListener
        )
        itemRecyclerView.adapter = itemAdapter
        when(element.channel.layout) {
            DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> {
                itemRecyclerView.layoutManager = GridLayoutManager(
                        itemView.context,
                        3
                )

                /**
                 * Add margin for recyclerview on for non-carousel banner
                 */
                val param = itemRecyclerView.layoutParams as ConstraintLayout.LayoutParams
                param.setMargins( itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_16),
                        param.topMargin,
                        itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_16),
                        param.bottomMargin
                )
                itemRecyclerView.layoutParams = param
            }
            DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL -> {
                itemRecyclerView.layoutManager = LinearLayoutManager(
                        itemView.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
                if (itemRecyclerView.itemDecorationCount == 0) itemRecyclerView.addItemDecoration(BannerOrganicDecoration())

                /**
                 * Make recyclerview to fill viewport width
                 */
                val param = itemRecyclerView.layoutParams as ConstraintLayout.LayoutParams
                param.setMargins( 0,
                        param.topMargin,
                        0,
                        param.bottomMargin
                )
                itemRecyclerView.layoutParams = param
            }
        }
    }

    private fun copyCoupon(view: View, cta: DynamicHomeChannel.CtaData) {
        val clipboard = view.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Coupon Code", cta.couponCode)
        clipboard.primaryClip = clipData

        Toaster.showNormal(view,
                getString(R.string.discovery_home_toaster_coupon_copied),
                Snackbar.LENGTH_LONG)
    }

    private fun mappingCtaButton(cta: DynamicHomeChannel.CtaData) {
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
    }

    private fun clearItemRecyclerViewDecoration() {
        while (itemRecyclerView.itemDecorationCount > 0) {
            itemRecyclerView.removeItemDecorationAt(0);
        }
    }

    class BannerItemAdapter(val bannerType: String,
                            val grids: Array<DynamicHomeChannel.Grid>,
                            val channel: DynamicHomeChannel.Channels,
                            val homeCategoryListener: HomeCategoryListener): RecyclerView.Adapter<BannerItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BannerItemViewHolder {
            val resource = when(bannerType) {
                DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> R.layout.home_banner_item
                else -> R.layout.home_banner_item_carousel
            }
            val v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false)
            return BannerItemViewHolder(v)
        }

        override fun getItemCount(): Int {
            return grids.size
        }

        override fun onBindViewHolder(viewholder: BannerItemViewHolder, position: Int) {
            val productCardView = viewholder.productCard
            productCardView.setLinesProductTitle(2)
            val gridItem = grids[position]
            productCardView.run {
                removeAllShopBadges()
                setProductNameVisible(true)
                setPriceVisible(true)
                setImageProductVisible(true)
                setButtonWishlistVisible(true)
                setSlashedPriceVisible(gridItem.slashedPrice != "")
                setLabelDiscountVisible(gridItem.discount != "")
                setImageRatingVisible(false)
                setReviewCountVisible(false)
                setButtonWishlistVisible(false)
                setProductNameText(gridItem.name)
                setPriceText(gridItem.price)
                setImageProductUrl(gridItem.imageUrl)
                setSlashedPriceText(gridItem.slashedPrice)
                setLabelDiscountText(gridItem.discount)
                realignLayout()

                setOnClickListener {
                    HomePageTracking.eventClickProductChannelMix(context, channel, position)

                    homeCategoryListener.onSectionItemClicked(gridItem.applink)
                }
            }
        }
    }

    class BannerItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val productCard: ProductCardViewSmallGrid by lazy { view.findViewById<ProductCardViewSmallGrid>(R.id.banner_item) }
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