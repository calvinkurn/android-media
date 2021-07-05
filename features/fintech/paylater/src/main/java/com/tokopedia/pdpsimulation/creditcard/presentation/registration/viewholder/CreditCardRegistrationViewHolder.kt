package com.tokopedia.pdpsimulation.creditcard.presentation.registration.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.creditcard.domain.model.BankCardListItem
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.base_payment_register_item.view.*

class CreditCardRegistrationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(bankData: BankCardListItem) {
        view.apply {
            ivPartnerLogo.loadImage(bankData.bankLogoUrl ?: "")
            tvTitlePaymentPartner.text = bankData.bankName
            if (bankData.bankPdpInfo.isNullOrEmpty()) {
                tvDescription.text = context.getString(R.string.credit_card_bank_sub_header)
            } else
                tvDescription.text = bankData.bankPdpInfo
            if (bankData.isPromo == true) {
                paymentOfferLabel.setLabelType(Label.GENERAL_DARK_RED)
                paymentOfferLabel.text = context.getString(R.string.credit_card_offer_label)
                paymentOfferLabel.visible()
            } else {
                paymentOfferLabel.gone()
            }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.base_payment_register_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardRegistrationViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}