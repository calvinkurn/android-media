package com.tokopedia.play.ui.promosheet.viewholder

import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemShopCouponBinding
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 30/11/21
 */
class MerchantVoucherNewViewHolder(
    private val binding: ItemShopCouponBinding,
    private val listener: Listener
) : BaseViewHolder(binding.root) {

    private val baseColor: ForegroundColorSpan by lazy(LazyThreadSafetyMode.NONE) {
        ForegroundColorSpan(MethodChecker.getColor(itemView.context, unifyR.color.Unify_RN500))
    }

    private val fgColor: ForegroundColorSpan by lazy(LazyThreadSafetyMode.NONE) {
        ForegroundColorSpan(
            MethodChecker.getColor(
                itemView.context,
                unifyR.color.Unify_NN950
            )
        )
    }

    fun bind(item: PlayVoucherUiModel.Merchant) {
        val isPrivate = item.type == MerchantVoucherType.Private
        binding.tvCouponTitle.text = item.title
        binding.tvMinTransaction.text = item.description

        binding.tvExpiredDate.shouldShowWithAction(item.expiredDate.isNotEmpty()) {
            binding.tvExpiredDate.text = buildSpannedString {
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

        binding.viewVoucherCopyable.root.showWithCondition(item.copyable)
        binding.viewVoucherCopyable.tvPlayVoucherCode.text = item.code

        binding.viewVoucherCopyable.ivPlayVoucherCopy.setOnClickListener {
            if (!isPrivate) return@setOnClickListener
            listener.onCopyItemVoucherClicked(item)
        }
        binding.root.setOnClickListener {
            if (isPrivate) return@setOnClickListener
            listener.onVoucherItemClicked(item)
        }
    }

    private fun countDays(expiredDate: String): Long {
        val diff = DateUtil.getDayDiffFromToday(expiredDate)
        return if (diff > 0) diff else 1
    }

    interface Listener {
        fun onCopyItemVoucherClicked(voucher: PlayVoucherUiModel.Merchant)
        fun onVoucherItemClicked(voucher: PlayVoucherUiModel.Merchant)
    }
}