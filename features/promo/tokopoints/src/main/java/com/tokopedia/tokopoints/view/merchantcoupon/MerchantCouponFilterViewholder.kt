package com.tokopedia.tokopoints.view.merchantcoupon

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.merchantcoupon.ProductCategoriesFilterItem
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.tp_layout_filter_item.view.*

class MerchantCouponFilterViewholder(itemView: View, var filterCallback: FilterCallback) : RecyclerView.ViewHolder(itemView) {

    enum class ViewHolderState {
        SELECTED,
        NORMAL,
        DISABLED
    }

    var viewHolderState = ViewHolderState.NORMAL

    fun bind(productCategoriesFilterItem: ProductCategoriesFilterItem, state: ViewHolderState) {
        itemView.writeChipsItem.chipText = productCategoriesFilterItem.rootName
        itemView.writeChipsItem?.setOnClickListener {
            when (viewHolderState) {

                ViewHolderState.SELECTED -> {
                    val isConfirm = filterCallback.filterClickDisableListener(adapterPosition)
                    if (isConfirm) viewHolderState = ViewHolderState.NORMAL
                }
                ViewHolderState.NORMAL -> {
                    filterCallback.filterClickEnableListener(adapterPosition)
                    viewHolderState = ViewHolderState.SELECTED
                }
                ViewHolderState.DISABLED ->
                    itemView.writeChipsItem.chipType = ChipsUnify.TYPE_DISABLE
            }

            AnalyticsTrackerUtil.sendEvent(
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_TOKO,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_CHIP_FILTER, "{${productCategoriesFilterItem.rootName}}",
                    AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                    AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
            )
        }
        viewHolderState = state

        when (state) {
            ViewHolderState.SELECTED ->
                itemView.writeChipsItem.chipType = ChipsUnify.TYPE_SELECTED
            ViewHolderState.NORMAL ->
                itemView.writeChipsItem.chipType = ChipsUnify.TYPE_NORMAL
            ViewHolderState.DISABLED ->
                itemView.writeChipsItem.chipType = ChipsUnify.TYPE_DISABLE
        }
    }

    interface FilterCallback {
        fun filterClickEnableListener(position: Int)
        fun filterClickDisableListener(position: Int): Boolean

    }
}