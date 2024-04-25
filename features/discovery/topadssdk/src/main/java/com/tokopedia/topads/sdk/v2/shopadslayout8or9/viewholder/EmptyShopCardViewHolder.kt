package com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.EmptyShopCardModel
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener

class EmptyShopCardViewHolder(
    itemView: View,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?
) : AbstractViewHolder<EmptyShopCardModel>(itemView) {

    override fun bind(item: EmptyShopCardModel) {
        itemView.setOnClickListener {
            topAdsBannerClickListener?.onBannerAdsClicked(
                adapterPosition,
                item.shopApplink,
                item.cpmData
            )
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                EmptyShopCardViewHolder::class.java.name,
                item.adsClickUrl,
                "",
                "",
                ""
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.empty_shop_card_item
    }
}
