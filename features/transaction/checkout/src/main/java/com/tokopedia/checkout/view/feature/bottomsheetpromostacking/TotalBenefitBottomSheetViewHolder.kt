package com.tokopedia.checkout.view.feature.bottomsheetpromostacking

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.checkout.R

/**
 * Created by fwidjaja on 22/03/19.
 */

class TotalBenefitBottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var totalBenefitLabel: TextView = itemView.findViewById(com.tokopedia.checkout.R.id.tv_total_benefit_label)
    private var totalBenefitAmount: TextView = itemView.findViewById(com.tokopedia.checkout.R.id.tv_total_benefit_amount)

    /*fun bind(model: SummariesUiModel) {
        totalBenefitLabel.text = model.description
        totalBenefitAmount.text = model.amountStr
    }*/

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_total_benefit
    }

}



