package com.tokopedia.checkout.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.R
import com.tokopedia.checkout.view.viewholder.*
import com.tokopedia.purchase_platform.features.checkout.view.viewholder.ShippingCompletionTickerViewHolder

class ShipmentItemDecoration : RecyclerView.ItemDecoration() {
    private var verticalSpaceHeight = 0
    private var context: Context? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (context == null) {
            context = parent.context
            verticalSpaceHeight = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
        }
        val viewHolder = parent.getChildViewHolder(view)

        context?.let {
            when {
                viewHolder is ShipmentDonationViewHolder -> {
                    outRect.bottom = it.resources.getDimension(R.dimen.dp_8).toInt()
                }
                viewHolder is PromoCheckoutViewHolder -> {
                    outRect.bottom = it.resources.getDimension(R.dimen.dp_8).toInt()
                }
                viewHolder is ShipmentEmasViewHolder -> {
                    outRect.bottom = it.resources.getDimension(R.dimen.dp_8).toInt()
                }
                viewHolder is ShipmentButtonPaymentViewHolder -> {
                    outRect.bottom = it.resources.getDimension(R.dimen.dp_0).toInt()
                }
                viewHolder.adapterPosition == (parent.adapter?.itemCount ?: 0) - LAST_POSITION -> {
                    outRect.bottom = it.resources.getDimension(R.dimen.dp_14).toInt()
                }
                viewHolder is ShipmentCostViewHolder -> {
                    if (viewHolder.adapterPosition == (parent.adapter?.itemCount ?: 0) - SECOND_LAST_POSITION) {
                        outRect.bottom = verticalSpaceHeight
                    } else {
                        outRect.bottom = it.resources.getDimension(R.dimen.dp_0).toInt()
                    }
                }
                viewHolder is ShipmentInsuranceTncViewHolder -> {
                    if (viewHolder.adapterPosition == (parent.adapter?.itemCount ?: 0) - SECOND_LAST_POSITION) {
                        outRect.top = verticalSpaceHeight
                    } else {
                        outRect.top = it.resources.getDimension(R.dimen.dp_0).toInt()
                    }
                    outRect.bottom = it.resources.getDimension(R.dimen.dp_0).toInt()
                }
                viewHolder is ShippingCompletionTickerViewHolder -> {
                    outRect.top = verticalSpaceHeight
                    outRect.bottom = it.resources.getDimension(R.dimen.dp_0).toInt()
                }
                viewHolder is ShipmentTickerAnnouncementViewHolder -> {
                    outRect.bottom = if (viewHolder.isEmptyTicker()) it.resources.getDimension(R.dimen.dp_0).toInt() else verticalSpaceHeight
                }
                else -> {
                    outRect.bottom = verticalSpaceHeight
                }
            }
        }
    }

    companion object {
        const val LAST_POSITION = 1
        const val SECOND_LAST_POSITION = 2
    }
}