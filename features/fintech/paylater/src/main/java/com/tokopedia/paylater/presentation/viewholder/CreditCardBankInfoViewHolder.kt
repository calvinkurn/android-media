package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardBank
import kotlinx.android.synthetic.main.credit_card_available_bank_item.view.*

class CreditCardBankInfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(bankData: CreditCardBank) {
        val benefitArray = bankData.bankBenefits.split(";")
        bankData.availableDuration
        view.apply {
            ivBank.maxWidth = 40.dpToPx(context.resources.displayMetrics)
            ImageHandler.loadImage(context,
                    ivBank,
                    bankData.bankLogo,
                    R.drawable.ic_loading_image)
            benefitLabel1.text = benefitArray[0].trim()
            benefitLabel2.text = benefitArray[1].trim()
            tvInstallments.text = "Cicilan ${bankData.availableDuration} bulan"
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_available_bank_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardBankInfoViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}