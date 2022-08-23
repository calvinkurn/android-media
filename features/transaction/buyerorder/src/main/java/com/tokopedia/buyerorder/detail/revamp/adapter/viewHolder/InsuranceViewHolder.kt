package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.ItemsInsurance

/**
 * created by @bayazidnasir on 23/8/2022
 */
class InsuranceViewHolder(itemView: View): AbstractViewHolder<ItemsInsurance>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_insurance
    }

    override fun bind(element: ItemsInsurance?) {

    }
}