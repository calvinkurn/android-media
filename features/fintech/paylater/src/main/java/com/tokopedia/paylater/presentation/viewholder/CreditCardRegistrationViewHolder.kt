package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardBank
import kotlinx.android.synthetic.main.base_payment_register_item.view.*
import kotlinx.android.synthetic.main.credit_card_available_bank_item.view.*

class CreditCardRegistrationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(bankData: CreditCardBank) {
        view.apply {
            ivPartnerLogo.layoutParams.height = context.dpToPx(28).toInt()
            ivPartnerLogo.layoutParams.width = context.dpToPx(56).toInt()
            ImageHandler.loadImage(context,
                    ivPartnerLogo,
                    bankData.bankLogo,
                    R.drawable.ic_loading_image)
            tvTitlePaymentPartner.text = bankData.bankName
            tvDescription.text = "Dapatkan Gift Card Rp500 Ribu"
            paymenyOfferLabel.text = "PROMO"
            paymenyOfferLabel.visible()
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.base_payment_register_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardRegistrationViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}