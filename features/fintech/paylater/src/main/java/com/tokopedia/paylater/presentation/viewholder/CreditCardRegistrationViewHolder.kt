package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.BankCardListItem
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.base_payment_register_item.view.*

class CreditCardRegistrationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(bankData: BankCardListItem) {
        view.apply {
            ImageHandler.loadImage(context,
                    ivPartnerLogo,
                    bankData.bankLogoUrl,
                    R.drawable.ic_loading_image)
            tvTitlePaymentPartner.text = bankData.bankName
            if (bankData.bankPdpInfo.isNullOrEmpty()) {
                tvDescription.text = "Lihat pilihan kartu"
            } else tvDescription.text = bankData.bankPdpInfo
            if (bankData.isPromo == true) {
                paymentOfferLabel.setLabelType(Label.GENERAL_DARK_RED)
                paymentOfferLabel.text = "PROMO"
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