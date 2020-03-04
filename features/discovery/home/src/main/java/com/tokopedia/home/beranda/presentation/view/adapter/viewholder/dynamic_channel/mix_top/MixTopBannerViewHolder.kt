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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.MixTopTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
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
    private var adapter: MixTopAdapter? = null
    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_mix_top_banner
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

        mappingHeader(channel)
        mappingItem(channel, visitables)

        recyclerView.setRecycledViewPool(parentRecycledViewPool)
        recyclerView.setHasFixedSize(true)
    }

    private fun mappingHeader(channel: DynamicHomeChannel.Channels){
        val bannerItem = channel.banner
        val ctaData = channel.banner.cta
        bannerTitle.text = bannerItem.title
        bannerDescription.text = bannerItem.description
        val textColor = if (bannerItem.textColor.isEmpty()) ContextCompat.getColor(bannerTitle.context, R.color.Neutral_N50) else Color.parseColor(bannerItem.textColor)
        bannerTitle.setTextColor(textColor)
        bannerDescription.setTextColor(textColor)
        bannerUnifyButton.setOnClickListener {
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