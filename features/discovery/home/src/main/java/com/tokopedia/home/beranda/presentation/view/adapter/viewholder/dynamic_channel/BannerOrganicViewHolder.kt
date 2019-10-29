package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.BannerOrganicDecoration
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
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

    var bannerTitle = itemView.findViewById<Typography>(R.id.banner_title)
    var bannerDescription = itemView.findViewById<Typography>(R.id.banner_description)
    var bannerUnifyButton = itemView.findViewById<UnifyButton>(R.id.banner_button)
    var bannerImage = itemView.findViewById<ImageView>(R.id.banner_image)

    companion object {
        val TYPE_CAROUSEL = "carousel"
        val TYPE_NON_CAROUSEL = "non carousel"
        @LayoutRes
        val LAYOUT = R.layout.home_dc_banner_recyclerview
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.dc_banner_rv)

        val bannerItem = channel.banner
        clearItemRecyclerViewDecoration(recyclerView)

        recyclerView.adapter = BannerItemAdapter(
                channel.layout,
                channel.grids,
                channel,
                homeCategoryListener
        )

        mappingCtaButton(channel.banner.cta)

        bannerUnifyButton.setOnClickListener {
            HomePageTracking.eventClickBannerButtonChannelMix(itemView.context, channel)

            if (bannerItem.cta.couponCode.isEmpty()) {
                homeCategoryListener.onSectionItemClicked(channel.banner.applink)
            } else {
                copyCoupon(itemView, bannerItem.cta)
            }
        }

        itemView.setOnClickListener {
            HomePageTracking.eventClickBannerChannelMix(itemView.context, channel)
            homeCategoryListener.onSectionItemClicked(channel.banner.applink)
        }

        bannerTitle.text = bannerItem.title
        bannerDescription.text = bannerItem.description

        bannerTitle.setTextColor(Color.parseColor(bannerItem.textColor))
        bannerDescription.setTextColor(Color.parseColor(bannerItem.textColor))

        Glide.with(itemView.context)
                .asBitmap()
                .load(bannerItem.imageUrl)
                .centerCrop()
                .dontAnimate()
                .into(getRoundedImageViewTarget(bannerImage, 16f))
        /**
         * Set button
         */
        bannerUnifyButton.text = bannerItem.cta.text


        when(channel.layout) {
            DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> {
                recyclerView.layoutManager = GridLayoutManager(
                        itemView.context,
                        3
                )
                /**
                 * Add margin for recyclerview on for non-carousel banner
                 */
                val param = recyclerView.layoutParams as ConstraintLayout.LayoutParams
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
                val param = recyclerView.layoutParams as ConstraintLayout.LayoutParams
                param.setMargins( 0,
                        param.topMargin,
                        0,
                        param.bottomMargin
                )
                recyclerView.layoutParams = param
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
    }

    private fun clearItemRecyclerViewDecoration(itemRecyclerView: RecyclerView) {
        while (itemRecyclerView.itemDecorationCount > 0) {
            itemRecyclerView.removeItemDecorationAt(0)
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
            productCardView.setProductModel(
                    ProductCardModel(
                            productImageUrl = gridItem.imageUrl,
                            isWishlistVisible = false,
                            productName = gridItem.name,
                            slashedPrice = gridItem.slashedPrice,
                            discountPercentage = gridItem.discount,
                            formattedPrice = gridItem.price
                    ),
                    BlankSpaceConfig(
                            price = true,
                            productName = true,
                            discountPercentage = true
                    )
            )
            productCardView.setOnClickListener {
                HomePageTracking.eventClickProductChannelMix(productCardView.context, channel, position)
                homeCategoryListener.onSectionItemClicked(gridItem.applink)
            }
        }
    }

    class BannerItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val productCard: ProductCardViewSmallGrid by lazy { view.findViewById<ProductCardViewSmallGrid>(R.id.banner_item) }
    }

    private fun getRoundedImageViewTarget(imageView: ImageView, radius: Float): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                circularBitmapDrawable.cornerRadius = radius
                imageView.setImageDrawable(circularBitmapDrawable)
            }
        }
    }

}