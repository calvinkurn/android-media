package com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.viewholder.ShopDirectPurchaseByEtalaseUiModel

class ShopHomeDirectPurchasedByEtalaseViewHolder(
    itemView: View,
) : AbstractViewHolder<ShopDirectPurchaseByEtalaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_direct_purchase_by_etalase

    }


    override fun bind(element: ShopDirectPurchaseByEtalaseUiModel?) {

    }

}
