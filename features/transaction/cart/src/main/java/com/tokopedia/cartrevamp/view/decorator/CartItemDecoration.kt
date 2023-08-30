package com.tokopedia.cartrevamp.view.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cartrevamp.view.viewholder.CartSectionHeaderViewHolder
import com.tokopedia.cartrevamp.view.viewholder.DisabledAccordionViewHolder
import com.tokopedia.cartrevamp.view.viewholder.DisabledItemHeaderViewHolder
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 06/02/18.
 */

class CartItemDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var verticalSpaceHeight: Int = 0
    private var context: Context? = null

    companion object {
        private const val DEFAULT_DIVIDER_HEIGHT = 1
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (context == null) {
            context = parent.context
            verticalSpaceHeight =
                context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt()
                    ?: 0
        }

        when (val viewHolder = parent.getChildViewHolder(view)) {
            is TickerAnnouncementViewHolder ->
                outRect.top =
                    context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_6)?.toInt()
                        ?: 0

//            is CartGroupViewHolder -> {
//                try {
//                    if (parent.adapter?.getItemViewType(viewHolder.absoluteAdapterPosition - 1) == DisabledReasonViewHolder.LAYOUT) {
//                        outRect.top = verticalSpaceHeight
//                    } else if (parent.adapter?.getItemViewType(viewHolder.absoluteAdapterPosition - 1) == CartItemViewHolder.TYPE_VIEW_ITEM_CART) {
//                        outRect.top = (context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_6)?.toInt() ?: 0) + verticalSpaceHeight
//                    } else {
//                        outRect.top =
//                            context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_6)
//                                ?.toInt() ?: 0
//                    }
// //                    outRect.bottom = verticalSpaceHeight
//                } catch (e: Exception) {
//                    // No-op
//                }
//            }

            is CartSectionHeaderViewHolder ->
                outRect.top =
                    context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)?.toInt()
                        ?: 0

            is ShipmentSellerCashbackViewHolder ->
                outRect.top =
                    context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)?.toInt()
                        ?: 0

//            is DisabledReasonViewHolder -> {
//                try {
//                    if (parent.adapter?.getItemViewType(viewHolder.absoluteAdapterPosition - 1) == DisabledItemHeaderViewHolder.LAYOUT) {
//                        outRect.top = verticalSpaceHeight
//                    } else {
//                        outRect.top =
//                            context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_6)
//                                ?.toInt() ?: 0
//                    }
//                    outRect.bottom = verticalSpaceHeight
//                } catch (e: Exception) {
//                    // No-op
//                }
//            }

            is DisabledItemHeaderViewHolder -> {
                outRect.top =
                    context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)
                        ?.toInt() ?: 0
                outRect.bottom = verticalSpaceHeight
            }

            is DisabledAccordionViewHolder -> {
                outRect.top = verticalSpaceHeight
            }

            else -> outRect.bottom = verticalSpaceHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }
}
