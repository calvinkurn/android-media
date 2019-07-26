package com.tokopedia.logisticcart.shippingduration.view

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.domain.shipping.LogisticPromoViewModel
import kotlinx.android.synthetic.main.item_logistic_voucher_stacking.view.*

/**
 * Created by fajarnuha on 29/03/19.
 */
class LogisticPromoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic val LAYOUT = R.layout.item_logistic_voucher_stacking
    }

    fun bindData(data: LogisticPromoViewModel, listener: ShippingDurationAdapterListener) {
        itemView.view_logistic_voucher.setData(data.title, data.shipperDesc, data.description)
        itemView.view_logistic_voucher.setUseButtonClickListener(object : View.OnClickListener{
            override fun onClick(view: View?) {
                listener.onLogisticPromoClicked(data)
            }
        })
    }

}