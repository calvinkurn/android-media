package com.tokopedia.mvc.presentation.list.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemVoucherBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherDataBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherHeaderBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherPeriodBinding
import com.tokopedia.mvc.databinding.SmvcItemVoucherStatsBinding
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.PromotionStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherCreator
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_FORMAT
import com.tokopedia.utils.date.DateUtil.YYYY_MM_DD_T_HH_MM_SS

class VouchersViewHolder(
    private val binding: SmvcItemVoucherBinding,
    private val listener: VoucherAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

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
            VoucherStatus.DELETED -> {
                processColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
                tfStatusTitle.text =
                    context.getString(R.string.smvc_voucherlist_status_deleted_text)
            }

            VoucherStatus.PROCESSING -> {
                processColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
                tfStatusTitle.text =
                    context.getString(R.string.smvc_voucherlist_status_processing_text)
            }

            VoucherStatus.NOT_STARTED -> {
                processColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
                tfStatusTitle.text =
                    context.getString(R.string.smvc_voucherlist_status_notstarted_text)
            }

            VoucherStatus.ONGOING -> {
                processColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                tfStatusTitle.text =
                    context.getString(R.string.smvc_voucherlist_status_ongoing_text)
            }

            VoucherStatus.ENDED -> {
                processColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
                tfStatusTitle.text = context.getString(R.string.smvc_voucherlist_status_ended_text)
            }

            VoucherStatus.STOPPED -> {
                processColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
                tfStatusTitle.text =
                    context.getString(R.string.smvc_voucherlist_status_stopped_text)
            }
        }
        setupHeaderSubsidy(voucher)
        tfStatusTitle.setTextColor(processColor)
        cardIndicator.setCardBackgroundColor(processColor)
        iconMenu.setOnClickListener {
            listener.onVoucherListMoreMenuClicked(voucher)
        }
    }

    private fun SmvcItemVoucherHeaderBinding.setupHeaderSubsidy(voucher: Voucher) {
        when (voucher.labelVoucher.labelCreator) {
            VoucherCreator.SELLER -> {
                tfVps.gone()
                tfSubsidy.gone()
            }
            VoucherCreator.INTOOLS -> {
                tfVps.gone()
                tfSubsidy.visible()
            }
            VoucherCreator.VPS -> {
                tfVps.visible()
                tfSubsidy.gone()
            }
        }
    }

    private fun SmvcItemVoucherDataBinding.setupMainInfo(voucher: Voucher) {
        tfVoucherProgramDetail.setVoucherProgram(voucher)
        tfVoucherName.text = voucher.name
        viewVoucherType.setBackgroundResource(
            if (voucher.isLockToProduct) {
                R.drawable.ic_voucher_product
            } else {
                R.drawable.ic_voucher_shop
            }
        )
        tfVoucherSubsidyInfo.setVoucherSubsidy(voucher)
        btnCodeBackground.isVisible = !voucher.isPublic
        layoutCodeBackground.isVisible = !voucher.isPublic
        layoutCodeBackground.setOnClickListener {
            listener.onVoucherListCopyCodeClicked(voucher)
        }
        tfCodeContent.text = voucher.code
        tfVoucherDesc.text = getFormattedVoucherDesc(root.context, voucher)
    }

    private fun SmvcItemVoucherPeriodBinding.setupPeriod(voucher: Voucher) {
        val startDate =
            DateUtil.formatDate(YYYY_MM_DD_T_HH_MM_SS, DEFAULT_VIEW_FORMAT, voucher.startTime)
        val finishDate =
            DateUtil.formatDate(YYYY_MM_DD_T_HH_MM_SS, DEFAULT_VIEW_FORMAT, voucher.finishTime)
        tfPeriodDate.text = root.context.getString(
            R.string.smvc_voucherlist_voucher_date_format,
            startDate,
            finishDate
        )
        tfMultiPeriodMore.text =
            root.context.getString(R.string.smvc_voucherlist_format_multiperiod, voucher.totalChild)
        tfMultiPeriodMore.isVisible = voucher.totalChild.isMoreThanZero()
        iconMenu.isVisible = voucher.totalChild.isMoreThanZero()
        tfMultiPeriodMore.setOnClickListener {
            listener.onVoucherListMultiPeriodClicked(voucher)
        }
        iconMenu.setOnClickListener {
            listener.onVoucherListMultiPeriodClicked(voucher)
        }
    }

    private fun Typography.setVoucherProgram(voucher: Voucher) {
        this.apply {
            text = voucher.subsidyDetail.programDetail.promotionLabel
            val isPromotionRejected = voucher.subsidyDetail.programDetail.promotionStatus == PromotionStatus.REJECTED
            showWithCondition(voucher.subsidyDetail.programDetail.promotionLabel.isNotEmpty() && !isPromotionRejected)
        }
    }

    private fun Typography.setVoucherSubsidy(voucher: Voucher) {
        this.apply {
            text = voucher.labelVoucher.labelSubsidyInfoFormatted
            val isPromotionRejected = voucher.subsidyDetail.programDetail.promotionStatus == PromotionStatus.REJECTED
            showWithCondition(voucher.labelVoucher.labelSubsidyInfoFormatted.isNotEmpty() && !isPromotionRejected)
        }
    }

    private fun SmvcItemVoucherStatsBinding.setupPeriodStats(voucher: Voucher) {
        val usedQuota = voucher.confirmedQuota + voucher.subsidyDetail.quotaSubsidized.confirmedGlobalQuota
        tfUsedQuota.text = listOf(usedQuota, voucher.quota).joinToString("/")
        tfInCartCoupon.text = voucher.bookedQuota.toString()
        tfCouponTarget.text = getTargetVoucherText(root.context, voucher)
    }

    private fun getFormattedVoucherDesc(context: Context, voucher: Voucher): CharSequence {
        return if (voucher.discountUsingPercent) {
            context.getString(
                R.string.smvc_voucherlist_format_percent_desc,
                getPromoName(context, voucher.type),
                voucher.discountAmtFormatted,
                voucher.discountAmtMax.getCurrencyFormatted()
            )
        } else {
            context.getString(
                R.string.smvc_voucherlist_format_desc,
                getPromoName(context, voucher.type),
                voucher.discountAmtMax.getCurrencyFormatted()
            )
        }
    }

    private fun getPromoName(context: Context, promoType: Int): String {
        return try {
            val arrPromoTypeName =
                context.resources.getStringArray(R.array.voucher_list_promo_type_items)
            arrPromoTypeName.getOrNull(promoType.dec()).orEmpty()
        } catch (e: Exception) {
            ""
        }
    }

    private fun getTargetVoucherText(context: Context, voucher: Voucher): String {
        return when (voucher.targetBuyer) {
            VoucherTargetBuyer.ALL_BUYER -> context.getString(R.string.smvc_voucherlist_target_allbuyer_text)
            VoucherTargetBuyer.NEW_FOLLOWER -> context.getString(R.string.smvc_voucherlist_target_newfollower_text)
            VoucherTargetBuyer.NEW_BUYER -> context.getString(R.string.smvc_voucherlist_target_newbuyer_text)
            VoucherTargetBuyer.MEMBER -> context.getString(R.string.smvc_voucherlist_target_member_text)
        }
    }
}
