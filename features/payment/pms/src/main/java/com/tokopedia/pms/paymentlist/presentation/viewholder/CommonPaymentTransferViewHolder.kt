package com.tokopedia.pms.paymentlist.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.KlicBCAPaymentModel
import com.tokopedia.pms.paymentlist.domain.data.StorePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.VirtualAccountPaymentModel

class CommonPaymentTransferViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private fun bindVA(item: VirtualAccountPaymentModel) {
    }

    private fun bindKlicBCA(item: KlicBCAPaymentModel) {

    }

    fun bindStore(item: StorePaymentModel) {

    }

    fun bind(item: BasePaymentModel) {
        when(item) {
            is VirtualAccountPaymentModel -> bindVA(item)
            is KlicBCAPaymentModel -> bindKlicBCA(item)
            is StorePaymentModel -> bindStore(item)
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.common_transfer_payment_list_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            CommonPaymentTransferViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
            )
    }
}