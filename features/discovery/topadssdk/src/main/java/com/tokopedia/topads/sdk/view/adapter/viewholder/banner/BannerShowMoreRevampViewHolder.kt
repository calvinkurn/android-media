package com.tokopedia.topads.sdk.view.adapter.viewholder.banner

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreUiModel
import com.tokopedia.viewallcard.ViewAllCard

class BannerShowMoreRevampViewHolder(
    container: View,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?
) : AbstractViewHolder<BannerShopViewMoreUiModel>(container) {

    override fun bind(element: BannerShopViewMoreUiModel?) {
        val viewAll = itemView.findViewById<ViewAllCard>(R.id.viewAllAdsBannerShop)
        viewAll.cardView.cardType = CARD_TYPE_BORDER
        viewAll.setCta("") {
            invokeClickListener(element!!)
            Unit
        }
        itemView.setOnClickListener { v: View? ->
            invokeClickListener(
                element!!
            )
        }
    }

    private fun invokeClickListener(element: BannerShopViewMoreUiModel) {
        if (topAdsBannerClickListener != null) {
            topAdsBannerClickListener.onBannerAdsClicked(
                adapterPosition,
                element.appLink,
                element.cpmData
            )
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                className,
                element.adsClickUrl,
                "",
                "",
                ""
            )
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.layout_ads_banner_shop_a_more_revamp
        private val className = BannerShowMoreRevampViewHolder::class.java.simpleName
        private const val CARD_TYPE_BORDER = 1
    }
}
