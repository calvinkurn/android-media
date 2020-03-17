package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.MixTopTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.FlashSaleCardViewTypeFactoryImpl
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class MixTopBannerViewHolder(
        itemView: View, val homeCategoryListener: HomeCategoryListener,
        countDownListener: CountDownView.CountDownListener,
        private val parentRecycledViewPool: RecyclerView.RecycledViewPool
) : DynamicChannelViewHolder(itemView, homeCategoryListener, countDownListener), FlashSaleCardListener{
    private val bannerTitle = itemView.findViewById<Typography>(R.id.banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.banner_description)
    private val bannerUnifyButton = itemView.findViewById<UnifyButton>(R.id.banner_button)
    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.dc_banner_rv)
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }
    private val background = itemView.findViewById<View>(R.id.background)
    private var adapter: MixTopAdapter? = null
    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_mix_top_banner
        private const val CTA_MODE_MAIN = "main"
        private const val CTA_MODE_TRANSACTION = "transaction"
        private const val CTA_MODE_INVERTED = "inverted"
        private const val CTA_MODE_DISABLED = "disabled"
        private const val CTA_MODE_ALTERNATE = "alternate"

        private const val CTA_TYPE_FILLED = "filled"
        private const val CTA_TYPE_GHOST = "ghost"
        private const val CTA_TYPE_TEXT = "text_only"
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        mappingView(channel)
    }

    override fun bind(element: DynamicChannelViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        val channel = element?.channel
        element?.let {
            channel?.let {channel->
                val visitables = mappingVisitablesFromChannel(channel)
                mappingHeader(channel)
                mappingItem(channel, visitables)
            }
        }
    }

    override fun getViewHolderClassName(): String {
        return MixTopBannerViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        homeCategoryListener.sendEETracking(MixTopTracking.getMixTopSeeAllClick(channel.header.name) as HashMap<String, Any>)
    }

    override fun onBannerSeeMoreClicked(applink: String, channel: DynamicHomeChannel.Channels) {
        homeCategoryListener.sendEETracking(MixTopTracking.getMixTopSeeAllClick(channel.header.name) as HashMap<String, Any>)
    }

    override fun onFlashSaleCardImpressed(position: Int, channel: DynamicHomeChannel.Channels) {
        homeCategoryListener.putEEToTrackingQueue(MixTopTracking.getMixTopView(
                MixTopTracking.mapChannelToProductTracker(channel),
                channel.header.name,
                adapterPosition.toString()
        ) as HashMap<String, Any>)
    }

    override fun onFlashSaleCardClicked(position: Int, channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, applink: String) {
        homeCategoryListener.sendEETracking(MixTopTracking.getMixTopClick(
                MixTopTracking.mapChannelToProductTracker(channel),
                channel.header.name,
                channel.id,
                adapterPosition.toString()
        ) as HashMap<String, Any>)
    }

    private fun mappingView(channel: DynamicHomeChannel.Channels) {
        val visitables = mappingVisitablesFromChannel(channel)

        recyclerView.setRecycledViewPool(parentRecycledViewPool)
        recyclerView.setHasFixedSize(true)

        valuateRecyclerViewDecoration()

        mappingHeader(channel)
        mappingCtaButton(channel.banner.cta)
        mappingItem(channel, visitables)

    }

    private fun mappingHeader(channel: DynamicHomeChannel.Channels){
        val bannerItem = channel.banner
        val ctaData = channel.banner.cta
        var textColor = ContextCompat.getColor(bannerTitle.context, R.color.Neutral_N50)
        var backColor: Int = ContextCompat.getColor(bannerTitle.context, R.color.Neutral_N50)
        if(bannerItem.textColor.isNotEmpty()){
            try {
                textColor = Color.parseColor(bannerItem.textColor)
            } catch (e: IllegalArgumentException) { }
        }

        if(bannerItem.backColor.isNotEmpty()) {
            try {
                backColor = Color.parseColor(bannerItem.backColor)
            } catch (e: IllegalArgumentException) { }
        }

        bannerTitle.text = bannerItem.title
        bannerTitle.visibility = if(bannerItem.title.isEmpty()) View.GONE else View.VISIBLE
        bannerDescription.text = bannerItem.description
        bannerDescription.visibility = if(bannerItem.description.isEmpty()) View.GONE else View.VISIBLE

        background.setBackgroundColor(backColor)
        bannerTitle.setTextColor(textColor)
        bannerDescription.setTextColor(textColor)

        bannerUnifyButton.setOnClickListener {
            homeCategoryListener.sendEETracking(MixTopTracking.getMixTopButtonClick(channel.header.name, ctaData.text) as HashMap<String, Any>)
            if (ctaData.couponCode.isEmpty()) {
                homeCategoryListener.onSectionItemClicked(channel.banner.applink)
            } else {
                copyCoupon(itemView, ctaData)
            }
        }

        itemView.setOnClickListener {
            homeCategoryListener.onSectionItemClicked(channel.banner.applink)
        }
    }

    private fun mappingItem(channel: DynamicHomeChannel.Channels, visitables: MutableList<Visitable<*>>) {
        startSnapHelper.attachToRecyclerView(recyclerView)
        val typeFactoryImpl = FlashSaleCardViewTypeFactoryImpl(this, channel)
        adapter = MixTopAdapter(visitables, typeFactoryImpl)
        recyclerView.adapter = adapter
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

    private fun copyCoupon(view: View, cta: DynamicHomeChannel.CtaData) {
        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Coupon Code", cta.couponCode)
        clipboard.primaryClip = clipData

        Toaster.make(view.parent as ViewGroup,
                getString(R.string.discovery_home_toaster_coupon_copied),
                Snackbar.LENGTH_LONG)
    }

    private fun valuateRecyclerViewDecoration() {
        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(SimpleHorizontalLinearLayoutDecoration())
        recyclerView.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        /**
         * Attach startSnapHelper to recyclerView
         */
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    private fun mappingVisitablesFromChannel(channel: DynamicHomeChannel.Channels): MutableList<Visitable<*>> {
        val visitables: MutableList<Visitable<*>> = convertDataToProductData(channel)
        if (isHasSeeMoreApplink(channel) && getLayoutType(channel) == TYPE_BANNER_CAROUSEL) {
            visitables.add(SeeMorePdpDataModel(
                    applink = channel.header.applink
            ))
        }
        return visitables
    }

    private fun convertDataToProductData(channel: DynamicHomeChannel.Channels): MutableList<Visitable<*>> {
        val list :MutableList<Visitable<*>> = mutableListOf()
        for (element in channel.grids) {
            list.add(FlashSaleDataModel(
                    ProductCardFlashSaleModel(
                            slashedPrice = element.slashedPrice,
                            productName = element.name,
                            formattedPrice = element.price,
                            productImageUrl = element.imageUrl,
                            discountPercentage = element.discount,
                            pdpViewCount = element.productViewCountFormatted,
                            stockBarLabel = element.label,
                            stockBarPercentage = element.soldPercentage
                    ),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = element,
                    applink = element.applink
            ))
        }
        return list
    }
}