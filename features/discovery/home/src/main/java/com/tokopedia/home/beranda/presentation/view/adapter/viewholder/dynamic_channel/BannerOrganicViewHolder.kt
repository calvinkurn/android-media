package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.helper.glide.loadImageCenterCrop
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.BannerOrganicDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.BannerItemAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.ProductBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.SeeMoreBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactoryImpl
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder.ProductItemViewHolder
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_dc_banner_recyclerview.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BannerOrganicViewHolder(itemView: View, val homeCategoryListener: HomeCategoryListener,
                              countDownListener: CountDownView.CountDownListener,
                              private val parentRecycledViewPool: RecyclerView.RecycledViewPool)
    : DynamicChannelViewHolder(itemView, homeCategoryListener, countDownListener) {

    override fun getViewHolderClassName(): String {
        return BannerOrganicViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTracking.eventClickSeeAllBannerMixChannel(itemView.context, channel.id, channel.header.name)
    }

    private var adapter: BannerItemAdapter? = null
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
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }

    companion object {
        val TYPE_CAROUSEL = "carousel"
        val TYPE_NON_CAROUSEL = "non carousel"
        @LayoutRes
        val LAYOUT = R.layout.home_dc_banner_recyclerview
        val DEFAULT_BANNER_MIX_SPAN_COUNT = 3
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        clearItemRecyclerViewDecoration(recyclerView)
        mappingBackgroundContainerUnify(channel)
        valuateRecyclerViewDecoration(channel)
        mappingView(channel)
    }

    private fun valuateRecyclerViewDecoration(channel: DynamicHomeChannel.Channels) {
        when(channel.layout) {
            DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC -> {
                recyclerView.layoutManager = GridLayoutManager(
                        itemView.context,
                        DEFAULT_BANNER_MIX_SPAN_COUNT
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
                 * Attach startSnapHelper to recyclerView
                 */
                startSnapHelper.attachToRecyclerView(recyclerView)
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
        val blankSpaceConfig = computeBlankSpaceConfig(channel)
        val bannerItem = channel.banner
        val visitables = mappingVisitablesFromChannel(channel, blankSpaceConfig)
        val ctaData = channel.banner.cta

        mappingCtaButton(ctaData)
        mappingBanner(bannerItem, channel, ctaData)
        mappingGrid(channel, visitables)

        recyclerView.setRecycledViewPool(parentRecycledViewPool)
        recyclerView.setHasFixedSize(true)
    }

    private fun mappingGrid(channel: DynamicHomeChannel.Channels, visitables: MutableList<Visitable<BannerMixTypeFactory>>) {
        if (adapter == null) {
            adapter = BannerItemAdapter(
                    BannerMixTypeFactoryImpl(homeCategoryListener),
                    channel.layout,
                    channel.grids,
                    channel,
                    homeCategoryListener,
                    visitables)

            recyclerView.adapter = adapter
        } else {
            adapter?.setItems(visitables)
        }
    }

    private fun mappingBanner(bannerItem: DynamicHomeChannel.Banner, channel: DynamicHomeChannel.Channels, ctaData: DynamicHomeChannel.CtaData) {
        bannerImage.loadImageCenterCrop(bannerItem.imageUrl)
        bannerTitle.text = bannerItem.title
        bannerDescription.text = bannerItem.description
        val textColor = if (bannerItem.textColor.isEmpty()) ContextCompat.getColor(bannerTitle.context, R.color.Neutral_N50) else Color.parseColor(bannerItem.textColor)
        bannerTitle.setTextColor(textColor)
        bannerDescription.setTextColor(textColor)
        bannerUnifyButton.setOnClickListener {
            HomePageTracking.eventClickBannerButtonChannelMix(itemView.context, channel)
            if (ctaData.couponCode.isEmpty()) {
                homeCategoryListener.onSectionItemClicked(channel.banner.applink)
            } else {
                copyCoupon(itemView, ctaData)
            }
        }
        itemView.setOnClickListener {
            HomePageTracking.eventClickBannerChannelMix(itemView.context, channel)
            homeCategoryListener.onSectionItemClicked(channel.banner.applink)
        }
    }

    override fun bind(element: DynamicChannelViewModel?, payloads: MutableList<Any>) {
        val channel = element?.channel
        val blankSpaceConfig = computeBlankSpaceConfig(channel)

        if (payloads.isNotEmpty()) {
            payloads.forEach { payload->
                if (payload == DynamicChannelViewModel.HOME_RV_BANNER_IMAGE_URL) {
                    channel?.let {
                        mappingBanner(it.banner, it, it.banner.cta)
                        mappingCtaButton(it.banner.cta)
                    }
                }
            }
        }
        element?.let {
            channel?.let {channel->
                val visitables = mappingVisitablesFromChannel(channel, blankSpaceConfig)
                mappingGrid(channel, visitables)
            }
        }
    }

    private fun mappingVisitablesFromChannel(channel: DynamicHomeChannel.Channels,
                                             blankSpaceConfig: BlankSpaceConfig): MutableList<Visitable<BannerMixTypeFactory>> {
        val visitables: MutableList<Visitable<BannerMixTypeFactory>> = channel.grids.map {
            ProductBannerMixDataModel(it, channel, blankSpaceConfig)
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

        //set false first to prevent unexpected behavior
        bannerUnifyButton.isInverse = false

        if (cta.text.isEmpty()) {
            bannerUnifyButton.visibility = View.GONE
            return
        }
        var mode = CTA_MODE_MAIN
        var type = CTA_TYPE_FILLED

        if (cta.mode.isNotEmpty()) mode = cta.mode

        if (cta.text.isNotEmpty()) type = cta.type

        when (type) {
            CTA_TYPE_FILLED -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.FILLED
            CTA_TYPE_GHOST -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.GHOST
            CTA_TYPE_TEXT -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.TEXT_ONLY
        }

        when (mode) {
            CTA_MODE_MAIN -> bannerUnifyButton.buttonType = UnifyButton.Type.MAIN
            CTA_MODE_TRANSACTION -> bannerUnifyButton.buttonType = UnifyButton.Type.TRANSACTION
            CTA_MODE_ALTERNATE -> bannerUnifyButton.buttonType = UnifyButton.Type.ALTERNATE
            CTA_MODE_DISABLED -> bannerUnifyButton.isEnabled = false
            CTA_MODE_INVERTED -> bannerUnifyButton.isInverse = true
        }

        bannerUnifyButton.text = cta.text
    }

    private fun clearItemRecyclerViewDecoration(itemRecyclerView: RecyclerView) {
        while (itemRecyclerView.itemDecorationCount > 0) {
            itemRecyclerView.removeItemDecorationAt(0)
        }
    }

    private fun computeBlankSpaceConfig(channel: DynamicHomeChannel.Channels?): BlankSpaceConfig {
        val blankSpaceConfig = BlankSpaceConfig(
                twoLinesProductName = true
        )
        channel?.grids?.forEach {
            if (it.freeOngkir.isActive) blankSpaceConfig.freeOngkir = true
            if (it.slashedPrice.isNotEmpty()) blankSpaceConfig.slashedPrice = true
            if (it.price.isNotEmpty()) blankSpaceConfig.price = true
            if (it.discount.isNotEmpty()) blankSpaceConfig.discountPercentage = true
            if (it.name.isNotEmpty()) blankSpaceConfig.productName = true
        }
        return blankSpaceConfig
    }
}