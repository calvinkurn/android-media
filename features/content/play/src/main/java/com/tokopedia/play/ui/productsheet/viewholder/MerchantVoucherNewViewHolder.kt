package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.utils.date.DateUtil

/**
 * @author by astidhiyaa on 30/11/21
 */
class MerchantVoucherNewViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvVoucherTitle: TextView = itemView.findViewById(R.id.tvCouponTitle)
    private val tvVoucherDescription: TextView = itemView.findViewById(R.id.tvMinTrax)
    private val tvVoucherStock: TextView = itemView.findViewById(R.id.tvCouponStock)
    private val tvVoucherExpiredDate: TextView = itemView.findViewById(R.id.tvExpiredDate)
    private val ivCopyVoucher: IconUnify = itemView.findViewById(R.id.ivCopyVoucher)

    fun bind(item: MerchantVoucherUiModel) {
        tvVoucherTitle.text = item.title
        tvVoucherDescription.text = item.description
        tvVoucherStock.shouldShowWithAction(item.voucherStock.isMoreThanZero()){
            tvVoucherStock.text = getString(R.string.play_voucher_sheet_coupon_stock, item.voucherStock.toString())
        }
        tvVoucherExpiredDate.shouldShowWithAction(item.expiredDate.isNotEmpty()){
            tvVoucherExpiredDate.text = getString(R.string.play_voucher_sheet_coupon_expired, countDays(item.expiredDate).toString())
        }
        ivCopyVoucher.shouldShowWithAction(item.copyable){
            ivCopyVoucher.setOnClickListener {
                listener.onCopyItemVoucherClicked(item)
            }
        }
    }

    private fun countDays(expiredDate: String): Long =
        DateUtil.getDayDiffFromToday(expiredDate)

    interface Listener{
        fun onCopyItemVoucherClicked(voucher: MerchantVoucherUiModel)
    }
}