package com.tokopedia.pdpsimulation.creditcard.presentation.simulation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.creditcard.domain.model.SimulationBank
import kotlinx.android.synthetic.main.credit_card_available_bank_item.view.*

class CreditCardBankInfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(bankData: SimulationBank) {
        val duration = bankData.availableDurationList?.joinToString(separator = ", ")
        view.apply {
            ivBank.maxWidth = 40.dpToPx(context.resources.displayMetrics)
            ivBank.loadImage(bankData.bankImageUrl ?: "")
            setLabelData(this, bankData.transactionBenefits)
            tvInstallments.text = context.getString(R.string.credit_card_simulation_card_header, duration)
        }
    }

    private fun setLabelData(itemView: View, transactionBenefits: String?) {
        val labelBenefitList = transactionBenefits?.split(";")?.toList()
        itemView.apply {
            val benefitItemFirst = labelBenefitList?.getOrNull(0)
            val benefitItemSecond = labelBenefitList?.getOrNull(1)
            if (benefitItemFirst.isNullOrEmpty()) benefitLabel1.gone()
            else {
                benefitLabel1.visible()
                benefitLabel1.text = benefitItemFirst
            }
            if (benefitItemSecond.isNullOrEmpty()) benefitLabel2.gone()
            else {
                benefitLabel2.visible()
                benefitLabel2.text = benefitItemSecond
            }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_available_bank_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardBankInfoViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}