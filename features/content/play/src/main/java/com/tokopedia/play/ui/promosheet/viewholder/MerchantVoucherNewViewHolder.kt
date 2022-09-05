package com.tokopedia.play.ui.promosheet.viewholder

import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.unifyprinciples.R as unifyR

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

    private val baseColor: ForegroundColorSpan by lazy {
        ForegroundColorSpan(MethodChecker.getColor(itemView.context, unifyR.color.Unify_RN500))
    }

    private val fgColor: ForegroundColorSpan by lazy {
        ForegroundColorSpan(
            MethodChecker.getColor(
                itemView.context,
                unifyR.color.Unify_NN950
            )
        )
    }

    fun bind(item: PlayVoucherUiModel.MerchantVoucherUiModel) {
        val isPrivate = item.type == MerchantVoucherType.Private
        tvVoucherTitle.text = item.title
        tvVoucherDescription.text = item.description

        tvVoucherExpiredDate.shouldShowWithAction(item.expiredDate.isNotEmpty()) {
            tvVoucherExpiredDate.text = buildSpannedString {
                append(
                    getString(
                        R.string.play_voucher_sheet_coupon_expired,
                        countDays(item.expiredDate).toString()
                    ),
                    fgColor, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
                if (item.voucherStock <= 20) {
                    append(
                        " " + getString(R.string.play_product_pinned_info_separator) + " ", baseColor,
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                    append(
                        getString(
                            R.string.play_voucher_widget_low_quantity,
                            item.voucherStock, ""
                        ), baseColor, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }
            }
        }

        viewCopyable.showWithCondition(item.copyable)
        tvVoucherCode.text = item.code

        ivCopyVoucher.setOnClickListener {
            if (!isPrivate) return@setOnClickListener
            listener.onCopyItemVoucherClicked(item)
        }
        itemView.setOnClickListener {
            if (isPrivate) return@setOnClickListener
            listener.onVoucherItemClicked(item)
        }
    }

    private fun countDays(expiredDate: String): Long {
        val diff = DateUtil.getDayDiffFromToday(expiredDate)
        return if (diff > 0) diff else 1
    }

    interface Listener {
        fun onCopyItemVoucherClicked(voucher: PlayVoucherUiModel.MerchantVoucherUiModel)
        fun onVoucherItemClicked(voucher: PlayVoucherUiModel.MerchantVoucherUiModel)
    }
}