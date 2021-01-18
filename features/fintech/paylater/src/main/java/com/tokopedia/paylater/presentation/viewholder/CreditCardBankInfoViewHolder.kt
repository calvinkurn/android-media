package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.SimulationBank
import kotlinx.android.synthetic.main.credit_card_available_bank_item.view.*

class CreditCardBankInfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(bankData: SimulationBank) {
        val benefitArray = bankData.transactionBenefits?.split(";")
        val duration = bankData.availableDurationList?.joinToString(separator = ",")
        view.apply {
            ivBank.maxWidth = 40.dpToPx(context.resources.displayMetrics)
            ImageHandler.loadImage(context,
                    ivBank,
                    bankData.bankImageUrl,
                    R.drawable.ic_loading_image)
            if (!benefitArray?.getOrNull(0).isNullOrEmpty()) {
                benefitLabel1.text = benefitArray?.getOrNull(0)?.trim()
            }
            if (!benefitArray?.getOrNull(1).isNullOrEmpty()) {
                benefitLabel2.text = benefitArray?.getOrNull(1)?.trim()
            }
            tvInstallments.text = "Cicilan ${duration} bulan"
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_available_bank_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardBankInfoViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}