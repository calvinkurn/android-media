package com.tokopedia.pms.paymentlist.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.BankTransferPaymentModel
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel

class BankTransferViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bind(element: BankTransferPaymentModel) {

    }

    companion object {
        private val LAYOUT_ID = R.layout.bank_transfer_payment_list_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = BankTransferViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}