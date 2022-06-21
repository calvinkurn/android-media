package com.tokopedia.topads.sdk.shopwidgetthreeproducts.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.model.EmptyShopCardModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

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
