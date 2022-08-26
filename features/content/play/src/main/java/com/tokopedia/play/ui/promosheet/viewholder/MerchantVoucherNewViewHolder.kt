package com.tokopedia.play.ui.promosheet.viewholder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.utils.date.DateUtil

/**
 * @author by astidhiyaa on 30/11/21
 */
class MerchantVoucherNewViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvVoucherTitle: TextView = itemView.findViewById(R.id.tv_coupon_title)
    private val tvVoucherDescription: TextView = itemView.findViewById(R.id.tv_min_transaction)
    private val tvVoucherExpiredDate: TextView = itemView.findViewById(R.id.tv_expired_date)
    private val viewCopyable: ConstraintLayout = itemView.findViewById(R.id.view_voucher_copyable)
    private val ivCopyVoucher: IconUnify = itemView.findViewById(R.id.iv_play_voucher_copy)
    private val tvVoucherCode: TextView = itemView.findViewById(R.id.tv_play_voucher_code)

    fun bind(item: PlayVoucherUiModel.MerchantVoucherUiModel) {
        tvVoucherTitle.text = item.title
        tvVoucherDescription.text = item.description

        tvVoucherExpiredDate.shouldShowWithAction(item.expiredDate.isNotEmpty()) {
            tvVoucherExpiredDate.text = getString(
                R.string.play_voucher_sheet_coupon_expired,
                countDays(item.expiredDate).toString()
            )
        }

        viewCopyable.showWithCondition(item.copyable)
        tvVoucherCode.text = item.code

        ivCopyVoucher.setOnClickListener {
            listener.onCopyItemVoucherClicked(item)
        }
    }

    private fun countDays(expiredDate: String): Long {
        val diff = DateUtil.getDayDiffFromToday(expiredDate)
        return if (diff > 0) diff else 1
    }

    interface Listener {
        fun onCopyItemVoucherClicked(voucher: PlayVoucherUiModel.MerchantVoucherUiModel)
    }
}