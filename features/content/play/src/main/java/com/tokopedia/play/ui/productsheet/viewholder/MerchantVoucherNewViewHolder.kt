package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel

/**
 * @author by astidhiyaa on 30/11/21
 */
class MerchantVoucherNewViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvVoucherTitle: TextView = itemView.findViewById(R.id.tvCouponTitle)
    private val tvVoucherDescription: TextView = itemView.findViewById(R.id.tvMinTrax)

    fun bind(item: MerchantVoucherUiModel) {
        tvVoucherTitle.text = item.title
        tvVoucherDescription.text = item.description
    }

    interface Listener{
    }
}