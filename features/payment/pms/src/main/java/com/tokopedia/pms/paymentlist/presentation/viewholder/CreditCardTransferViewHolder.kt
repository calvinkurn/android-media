package com.tokopedia.pms.paymentlist.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.CreditCardPaymentModel

class CreditCardTransferViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(element: CreditCardPaymentModel) {

    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_payment_list_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            CreditCardTransferViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
            )
    }
}