package com.tokopedia.play.ui.promosheet.viewholder

import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemShopCouponBinding
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter.getDayDiffFromToday
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
                unifyR.color.Unify_NN600
            )
        )
    }

    fun bind(item: PlayVoucherUiModel.Merchant) {
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
            }
        }

        binding.viewVoucherCopyable.root.showWithCondition(item.copyable)
        binding.viewVoucherCopyable.tvPlayVoucherCode.text = item.code

        binding.root.setOnClickListener {
            if(item.isPrivate) listener.onCopyItemVoucherClicked(item)
            else listener.onVoucherItemClicked(item)
        }
    }

    private fun countDays(expiredDate: String): Long {
        val diff = PlayDateTimeFormatter.convertToCalendar(
            raw = expiredDate,
            pattern = DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z
        )?.time?.getDayDiffFromToday() ?: 0
        return if (diff > 0) diff else 1
    }

    companion object {
        private const val VOUCHER_THRESHOLD = 20
    }

    interface Listener {
        fun onCopyItemVoucherClicked(voucher: PlayVoucherUiModel.Merchant)
        fun onVoucherItemClicked(voucher: PlayVoucherUiModel.Merchant)
    }
}
