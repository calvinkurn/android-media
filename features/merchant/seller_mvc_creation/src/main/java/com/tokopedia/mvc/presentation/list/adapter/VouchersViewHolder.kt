package com.tokopedia.mvc.presentation.list.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.DateUtil.formatStartFinishDate
import com.tokopedia.mvc.databinding.SmvcItemVoucherBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherDataBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherHeaderBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherPeriodBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherStatsBinding
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherStatusConst.Companion.DELETED
import com.tokopedia.mvc.domain.entity.VoucherStatusConst.Companion.ENDED
import com.tokopedia.mvc.domain.entity.VoucherStatusConst.Companion.NOT_STARTED
import com.tokopedia.mvc.domain.entity.VoucherStatusConst.Companion.ONGOING
import com.tokopedia.mvc.domain.entity.VoucherStatusConst.Companion.PROCESSING
import com.tokopedia.mvc.domain.entity.VoucherStatusConst.Companion.STOPPED
import com.tokopedia.mvc.domain.entity.VoucherTargetBuyer.Companion.ALL_BUYER
import com.tokopedia.mvc.domain.entity.VoucherTargetBuyer.Companion.MEMBER
import com.tokopedia.mvc.domain.entity.VoucherTargetBuyer.Companion.NEW_BUYER
import com.tokopedia.mvc.domain.entity.VoucherTargetBuyer.Companion.NEW_FOLLOWER

class VouchersViewHolder(
    private val binding: SmvcItemVoucherBinding,
    private val listener: VoucherAdapterListener,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(voucher: Voucher) {
        binding.headerContent.setupHeader(voucher)
        binding.mainInfoContent.setupMainInfo(voucher)
        binding.periodContent.setupPeriod(voucher)
        binding.periodStatsContent.setupPeriodStats(voucher)
        binding.root.setOnClickListener {
            listener.onVoucherListClicked(voucher)
        }
    }

    private fun SmvcItemVoucherHeaderBinding.setupHeader(voucher: Voucher) {
        val context = root.context
        val processColor: Int
        when (voucher.status) {
            DELETED -> {
                processColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                tfStatusTitle.text = context.getString(R.string.smvc_voucherlist_status_deleted_text)
            }
            PROCESSING -> {
                processColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                tfStatusTitle.text = context.getString(R.string.smvc_voucherlist_status_processing_text)
            }
            NOT_STARTED -> {
                processColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                tfStatusTitle.text = context.getString(R.string.smvc_voucherlist_status_notstarted_text)
            }
            ONGOING -> {
                processColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                tfStatusTitle.text = context.getString(R.string.smvc_voucherlist_status_ongoing_text)
            }
            ENDED -> {
                processColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                tfStatusTitle.text = context.getString(R.string.smvc_voucherlist_status_ended_text)
            }
            STOPPED -> {
                processColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                tfStatusTitle.text = context.getString(R.string.smvc_voucherlist_status_stopped_text)
            }
            else -> {
                processColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                tfStatusTitle.text = ""
            }
        }
        tfVps.isVisible = voucher.isVps
        tfSubsidy.isVisible = voucher.isSubsidy
        tfStatusTitle.setTextColor(processColor)
        cardIndicator.setCardBackgroundColor(processColor)
        iconMenu.setOnClickListener {
            listener.onVoucherListMoreMenuClicked(voucher)
        }
    }

    private fun SmvcItemVoucherDataBinding.setupMainInfo(voucher: Voucher) {
        tfVoucherName.text = voucher.name
        viewVoucherType.background = if (voucher.isLockToProduct) {
            MethodChecker.getDrawable(root.context, R.drawable.ic_voucher_product)
        } else {
            MethodChecker.getDrawable(root.context, R.drawable.ic_voucher_shop)
        }
        btnCodeBackground.isVisible = !voucher.isPublic
        layoutCodeBackground.isVisible = !voucher.isPublic
        layoutCodeBackground.setOnClickListener {
            listener.onVoucherListCopyCodeClicked(voucher)
        }
        tfCodeContent.text = voucher.code
        tfVoucherDesc.text = getFormattedVoucherDesc(root.context, voucher)
    }

    private fun getFormattedVoucherDesc(context: Context, voucher: Voucher): CharSequence {
        return if (voucher.discountUsingPercent) {
            context.getString(
                R.string.smvc_voucherlist_format_percent_desc,
                voucher.typeFormatted,
                voucher.discountAmtFormatted,
                voucher.discountAmtMax.getCurrencyFormatted())
        } else {
            context.getString(
                R.string.smvc_voucherlist_format_desc,
                voucher.typeFormatted,
                voucher.discountAmtMax.getCurrencyFormatted())
        }
    }

    private fun SmvcItemVoucherPeriodBinding.setupPeriod(voucher: Voucher) {
        tfPeriodDate.text = formatStartFinishDate(voucher.startTime, voucher.finishTime)
        tfMultiPeriodMore.text = root.context.getString(R.string.smvc_voucherlist_format_multiperiod, voucher.totalChild)
        tfMultiPeriodMore.isVisible = voucher.totalChild.isMoreThanZero()
        iconMenu.isVisible = voucher.totalChild.isMoreThanZero()
        tfMultiPeriodMore.setOnClickListener {
            listener.onVoucherListMultiPeriodClicked(voucher)
        }
        iconMenu.setOnClickListener {
            listener.onVoucherListMultiPeriodClicked(voucher)
        }
    }

    private fun SmvcItemVoucherStatsBinding.setupPeriodStats(voucher: Voucher) {
        tfUsedQuota.text = listOf(voucher.confirmedQuota, voucher.quota).joinToString("/")
        tfInCartCoupon.text = voucher.bookedQuota.toString()
        tfCouponTarget.text = getTargetVoucherText(root.context, voucher)
    }

    private fun getTargetVoucherText(context: Context, voucher: Voucher): String {
        return when (voucher.targetBuyer) {
            ALL_BUYER -> context.getString(R.string.smvc_voucherlist_target_allbuyer_text)
            NEW_FOLLOWER -> context.getString(R.string.smvc_voucherlist_target_newfollower_text)
            NEW_BUYER -> context.getString(R.string.smvc_voucherlist_target_newbuyer_text)
            MEMBER -> context.getString(R.string.smvc_voucherlist_target_member_text)
            else -> ""
        }
    }
}
