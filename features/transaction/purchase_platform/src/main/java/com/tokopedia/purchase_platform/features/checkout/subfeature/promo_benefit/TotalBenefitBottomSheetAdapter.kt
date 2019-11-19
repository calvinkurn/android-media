package com.tokopedia.purchase_platform.features.checkout.subfeature.promo_benefit

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.purchase_platform.R
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel

/**
 * Created by fwidjaja on 22/03/19.
 */

class TotalBenefitBottomSheetAdapter : RecyclerView.Adapter<TotalBenefitBottomSheetAdapter.BenefitViewHolder>() {
    private lateinit var benefitList: List<SummariesUiModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_total_benefit, parent, false)

        return BenefitViewHolder(view)
    }

    fun setList(benefitList: List<SummariesUiModel>) {
        this.benefitList = benefitList
    }

    override fun getItemCount(): Int {
        return benefitList.size
    }

    override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
        val benefit = benefitList[position]
        if (!isNullOrEmpty(benefit.description)) {
            holder.label.text = benefit.description
        }
        if (!isNullOrEmpty(benefit.amountStr)) {
            holder.amount.text = benefit.amountStr
        }
    }

    private fun isNullOrEmpty(string: String?): Boolean {
        return string == null || string.equals(other = "null", ignoreCase = true) || string.isEmpty()
    }

    inner class BenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var label: TextView = itemView.findViewById(R.id.tv_total_benefit_label)
        var amount: TextView = itemView.findViewById(R.id.tv_total_benefit_amount)
    }
}