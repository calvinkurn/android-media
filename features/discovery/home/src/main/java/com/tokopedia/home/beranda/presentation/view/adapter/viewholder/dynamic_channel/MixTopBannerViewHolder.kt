package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

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
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

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

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_mix_top_banner
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        mappingHeader(channel)
    }

    override fun getViewHolderClassName(): String {
        return MixTopBannerViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {

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
//            HomePageTracking.eventClickBannerButtonChannelMix(itemView.context, channel)
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

    private fun mappingItem(channel: DynamicHomeChannel.Channels){
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    private fun copyCoupon(view: View, cta: DynamicHomeChannel.CtaData) {
        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Coupon Code", cta.couponCode)
        clipboard.primaryClip = clipData

        Toaster.make(view.parent as ViewGroup,
                getString(R.string.discovery_home_toaster_coupon_copied),
                Snackbar.LENGTH_LONG)
    }
}