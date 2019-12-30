package com.tokopedia.purchase_platform.features.cart.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder
import com.tokopedia.purchase_platform.common.feature.seller_cashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartSectionHeaderViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartShopViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledItemHeaderViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledShopViewHolder

/**
 * @author anggaprasetiyo on 06/02/18.
 */

class CartItemDecoration : RecyclerView.ItemDecoration() {

    private var verticalSpaceHeight: Int = 0
    private var context: Context? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (context == null) {
            context = parent.context
            verticalSpaceHeight = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        }

        when (val viewHolder = parent.getChildViewHolder(view)) {
            is PromoGlobalViewHolder -> outRect.bottom = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            is CartShopViewHolder -> outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            is CartSectionHeaderViewHolder -> outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            is ShipmentSellerCashbackViewHolder -> outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            is DisabledShopViewHolder -> {
                if (parent.adapter?.getItemViewType(viewHolder.getAdapterPosition() - 1) == DisabledItemHeaderViewHolder.LAYOUT) {
                    outRect.top = verticalSpaceHeight
                } else {
                    outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
                }
                outRect.bottom = verticalSpaceHeight
            }
            is DisabledItemHeaderViewHolder -> {
                outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
                outRect.bottom = verticalSpaceHeight
            }
            else -> outRect.bottom = verticalSpaceHeight
        }
    }
}
