package com.tokopedia.purchase_platform.checkout.subfeature.promo_benefit

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.purchase_platform.checkout.R

/**
 * Created by fwidjaja on 22/03/19.
 */

class TotalBenefitBottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var totalBenefitLabel: TextView = itemView.findViewById(com.tokopedia.purchase_platform.checkout.R.id.tv_total_benefit_label)
    private var totalBenefitAmount: TextView = itemView.findViewById(com.tokopedia.purchase_platform.checkout.R.id.tv_total_benefit_amount)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_total_benefit
    }

}



