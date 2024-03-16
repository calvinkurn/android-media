package com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ShowMoreItemModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener

class ShowMoreItemVieHolder(
    itemView: View,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?
) : AbstractViewHolder<ShowMoreItemModel>(itemView) {

    override fun bind(item: ShowMoreItemModel) {
        itemView.setOnClickListener {
            topAdsBannerClickListener?.onBannerAdsClicked(
                adapterPosition,
                item.appLink,
                item.cpmData
            )
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                ShowMoreItemVieHolder::class.java.name,
                item.adsClickUrl,
                "",
                "",
                ""
            )
        }
    }

    companion object {
        @LayoutRes
        var LAYOUT: Int = R.layout.shop_widget_show_more_item_layout
    }
}
