package com.tokopedia.cartrevamp.view.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cartrevamp.view.viewholder.CartSectionHeaderViewHolder
import com.tokopedia.cartrevamp.view.viewholder.DisabledAccordionViewHolder
import com.tokopedia.cartrevamp.view.viewholder.DisabledItemHeaderViewHolder
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR

/**
 * @author anggaprasetiyo on 06/02/18.
 */

class CartItemDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var verticalSpaceHeight: Int = 0
    private var context: Context? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (context == null) {
            context = parent.context
            verticalSpaceHeight =
                context?.resources?.getDimension(abstractionR.dimen.dp_0)?.toInt()
                    ?: 0
        }

        when (parent.getChildViewHolder(view)) {
            is TickerAnnouncementViewHolder ->
                outRect.top =
                    context?.resources?.getDimension(abstractionR.dimen.dp_6)?.toInt()
                        ?: 0

            is CartSectionHeaderViewHolder ->
                outRect.top =
                    context?.resources?.getDimension(abstractionR.dimen.dp_8)?.toInt()
                        ?: 0

            is ShipmentSellerCashbackViewHolder ->
                outRect.top =
                    context?.resources?.getDimension(abstractionR.dimen.dp_8)?.toInt()
                        ?: 0

            is DisabledItemHeaderViewHolder -> {
                outRect.top =
                    context?.resources?.getDimension(abstractionR.dimen.dp_8)
                        ?.toInt() ?: 0
                outRect.bottom = verticalSpaceHeight
            }

            is DisabledAccordionViewHolder -> {
                outRect.top = verticalSpaceHeight
            }

            else -> outRect.bottom = verticalSpaceHeight
        }
    }
}
