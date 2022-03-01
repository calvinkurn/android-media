package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficCardModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateTrafficProductItemVH(itemView: View)
    : AbstractViewHolder<AffiliateTrafficCardModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_traffic_product_cards
    }

    override fun bind(element: AffiliateTrafficCardModel?) {
        element?.product?.image?.androidURL?.let { url ->
            itemView.findViewById<ImageUnify>(R.id.product_image)?.setImageUrl(
                url
            )
        }
        itemView.findViewById<Typography>(R.id.product_name)?.apply {
            text = element?.product?.cardTitle
        }
        itemView.findViewById<Typography>(R.id.product_desc)?.apply {
            text = element?.product?.cardDescription
        }
    }
}
