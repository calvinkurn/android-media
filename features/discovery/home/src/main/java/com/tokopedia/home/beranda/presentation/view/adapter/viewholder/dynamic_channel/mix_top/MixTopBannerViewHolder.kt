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
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.MixTopTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopProductDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopSeeMoreDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel.MixTopVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.typeFactory.MixTopTypeFactoryImpl
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class MixTopBannerViewHolder(
        itemView: View, val homeCategoryListener: HomeCategoryListener,
        countDownListener: CountDownView.CountDownListener,
        private val parentRecycledViewPool: RecyclerView.RecycledViewPool
) : DynamicChannelViewHolder(itemView, homeCategoryListener, countDownListener){
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
        val blankSpaceConfig = computeBlankSpaceConfig(channel)
        element?.let {
            channel?.let {channel->
                val visitables = mappingVisitablesFromChannel(channel, blankSpaceConfig)
                mappingHeader(channel)
                mappingItem(channel, visitables)
            }
        }
    }

    override fun getViewHolderClassName(): String {
        return MixTopBannerViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        homeCategoryListener.putEEToTrackingQueue(MixTopTracking.getMixTopSeeAllClick(channel.header.name) as HashMap<String, Any>)
    }

    private fun mappingView(channel: DynamicHomeChannel.Channels) {
        val blankSpaceConfig = computeBlankSpaceConfig(channel)
        val visitables = mappingVisitablesFromChannel(channel, blankSpaceConfig)

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

    private fun mappingItem(channel: DynamicHomeChannel.Channels, visitables: MutableList<MixTopVisitable>){
        if (adapter == null) {
            startSnapHelper.attachToRecyclerView(recyclerView)
            adapter = MixTopAdapter(
                    MixTopTypeFactoryImpl(homeCategoryListener),
                    channel.grids,
                    channel,
                    homeCategoryListener)
            adapter?.setItems(visitables)

            recyclerView.adapter = adapter
        } else {
            adapter?.setItems(visitables)
        }
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

    private fun mappingVisitablesFromChannel(channel: DynamicHomeChannel.Channels,
                                             blankSpaceConfig: BlankSpaceConfig): MutableList<MixTopVisitable> {
        val visitables: MutableList<MixTopVisitable> = channel.grids.map {
            MixTopProductDataModel(it, channel, blankSpaceConfig, adapterPosition.toString())
        }.toMutableList()

        if (isHasSeeMoreApplink(channel) && getLayoutType(channel) == TYPE_BANNER_CAROUSEL) {
            visitables.add(MixTopSeeMoreDataModel(
                    channel
            ))
        }
        return visitables
    }
}