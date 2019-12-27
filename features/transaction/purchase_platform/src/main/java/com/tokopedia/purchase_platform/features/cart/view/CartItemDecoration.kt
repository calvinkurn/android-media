package com.tokopedia.purchase_platform.features.cart.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder
import com.tokopedia.purchase_platform.common.feature.ticker_announcement.TickerAnnouncementViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.*

/**
 * @author anggaprasetiyo on 06/02/18.
 */

class CartItemDecoration : RecyclerView.ItemDecoration() {

    private var verticalSpaceHeight: Int = 0
    private var context: Context? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (context == null) {
            context = parent.context
            verticalSpaceHeight = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
        }

        val viewHolder = parent.getChildViewHolder(view)

        when {
            viewHolder is PromoGlobalViewHolder -> outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            viewHolder is CartTickerErrorViewHolder -> outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            viewHolder is CartRecentViewViewHolder || viewHolder is CartWishlistViewHolder || viewHolder is CartRecommendationViewHolder -> outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            viewHolder is TickerAnnouncementViewHolder -> outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            viewHolder is DisabledCartItemViewHolder -> if (viewHolder.showDivider) {
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            } else {
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            }
            viewHolder is CartShopViewHolder -> outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            viewHolder is DisabledShopViewHolder -> {
                if (parent.adapter?.getItemViewType(viewHolder.getAdapterPosition() - 1) == DisabledItemHeaderViewHolder.LAYOUT) {
                    outRect.top = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                } else {
                    outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
                }
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            viewHolder is DisabledItemHeaderViewHolder -> {
                outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            viewHolder is CartSectionHeaderViewHolder -> outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            viewHolder.adapterPosition == parent.adapter?.itemCount ?: 0 - 1 -> outRect.bottom = context?.resources?.getDimension(R.dimen.dp_14)?.toInt() ?: 0
            else -> outRect.bottom = verticalSpaceHeight
        }
    }
}
