package com.tokopedia.purchase_platform.features.cart.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionViewHolder
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
        if (viewHolder is CartPromoSuggestionViewHolder) {
            val cartPromoSuggestionHolderData = viewHolder.cartPromoSuggestionHolderData
            if (cartPromoSuggestionHolderData.isVisible) {
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
                outRect.left = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
                outRect.right = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
            } else {
                outRect.bottom = 0
            }
        } else if (viewHolder is PromoGlobalViewHolder) {
            outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        } else if (viewHolder is CartTickerErrorViewHolder) {
            outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        } else if (viewHolder.adapterPosition == parent.adapter?.itemCount ?: 0 - 1) {
            outRect.bottom = context?.resources?.getDimension(R.dimen.dp_14)?.toInt() ?: 0
        } else if (viewHolder is CartRecentViewViewHolder ||
                viewHolder is CartWishlistViewHolder ||
                viewHolder is CartRecommendationViewHolder) {
            outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        } else if (viewHolder is TickerAnnouncementViewHolder) {
            outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        } else if (viewHolder is DisabledCartItemViewHolder) {
            if (viewHolder.showDivider) {
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            } else {
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            }
        } else if (viewHolder is CartShopViewHolder) {
            outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
        } else if (viewHolder is DisabledShopViewHolder) {
            if (parent.adapter?.getItemViewType(viewHolder.getAdapterPosition() - 1) == DisabledItemHeaderViewHolder.LAYOUT) {
                outRect.top = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            } else {
                outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            }
            outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        } else if (viewHolder is DisabledItemHeaderViewHolder) {
            outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            outRect.bottom = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        } else if (viewHolder is CartSectionHeaderViewHolder) {
            outRect.top = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
        } else {
            outRect.bottom = verticalSpaceHeight
        }
    }
}
