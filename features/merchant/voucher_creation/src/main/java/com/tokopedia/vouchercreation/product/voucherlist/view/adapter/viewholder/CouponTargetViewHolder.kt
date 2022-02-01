package com.tokopedia.vouchercreation.product.voucherlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.databinding.ItemMvcCouponListBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import timber.log.Timber

class CouponListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var binding: ItemMvcCouponListBinding? by viewBinding()
    val moreButton by lazy { binding?.iuMoreButton }
    val iconCopyCode by lazy { binding?.iconCopyCode }

    fun bindData(coupon: VoucherUiModel) {
        setCouponTitleAndDescription(coupon)
        setCouponStatus(coupon)
        setCouponDate(coupon)
        setCouponQuota(coupon)
        setImageCoupon(coupon.isPublic, coupon.type)
        showPromoCode(coupon.isPublic, coupon.code)
    }

    private fun setCouponTitleAndDescription(coupon: VoucherUiModel) {
        binding?.tvMvcVoucherName?.text = coupon.name
        val descriptionFirstLine = "${coupon.typeFormatted} ${coupon.discountAmtFormatted}"
        val descriptionSecondLine = itemView.context.getString(R.string.mvc_coupon_product_min_payment,
            CurrencyFormatHelper.convertToRupiah(coupon.minimumAmt.toString()))
        val maxBenefit = if (coupon.type == VoucherTypeConst.DISCOUNT) {
            itemView.context.getString(R.string.mvc_coupon_product_max_benefit,
                CurrencyFormatHelper.convertToRupiah(coupon.discountAmtMax.toString()))
        } else ""
        binding?.tvMvcVoucherDescription?.text =
            "$descriptionFirstLine $maxBenefit\n$descriptionSecondLine"
    }

    private fun setImageCoupon(isPublic: Boolean, @VoucherTypeConst voucherType: Int) {
        try {
            binding?.imgMvcVoucherType?.run {
                val drawableRes = when {
                    isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_publik
                    !isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_khusus
                    isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_publik
                    !isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_khusus
                    else -> R.drawable.ic_mvc_cashback_publik
                }
                loadImageDrawable(drawableRes)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun setCouponStatus(element: VoucherUiModel) {
        binding?.run {
            val statusStr: String
            val colorRes: Int
            val statusIndicatorBg: Int
            val context = root.context
            when (element.status) {
                VoucherStatusConst.ONGOING -> {
                    statusStr = context.getString(R.string.mvc_ongoing)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Green_G500
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_green
                }
                VoucherStatusConst.NOT_STARTED -> {
                    statusStr = context.getString(R.string.mvc_future)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_grey
                }
                VoucherStatusConst.STOPPED -> {
                    statusStr = context.getString(R.string.mvc_stopped)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Red_R500
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_red
                }
                VoucherStatusConst.DELETED -> {
                    statusStr = context.getString(R.string.mvc_deleted)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Red_R500
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_red
                }
                else -> {
                    statusStr = context.getString(R.string.mvc_ended)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_grey
                }
            }

            tvMvcVoucherStatus.text = statusStr
            tvMvcVoucherStatus.setTextColor(context.getResColor(colorRes))
            viewMvcVoucherStatus.background = context.getResDrawable(statusIndicatorBg)
        }
    }

    private fun setCouponQuota(coupon: VoucherUiModel) {
        val remainingAmount = coupon.quota - coupon.confirmedQuota
        val remainingVoucherText = "<b>${remainingAmount}</b>/${coupon.quota}"
        binding?.tvMvcRemainingVoucher?.text = remainingVoucherText.parseAsHtml()
        binding?.tvCouponProductSold?.text = coupon.confirmedQuota.toString()
    }

    private fun showPromoCode(isPublicVoucher: Boolean, voucherCode: String) {
        binding?.run {
            val isVisible = !isPublicVoucher
            viewMvcVoucherCodeBg.isVisible = isVisible
            tvLblCode.isVisible = isVisible
            tvVoucherCode.isVisible = isVisible
            iconCopyCode.isVisible = isVisible
            tvVoucherCode.text = String.format(" %s", voucherCode)
        }
    }

    private fun setCouponDate(coupon: VoucherUiModel) {
        val isActiveVoucher = coupon.status == VoucherStatusConst.ONGOING ||
                coupon.status == VoucherStatusConst.NOT_STARTED
        val newFormat = "dd MMM yyyy"

        if (isActiveVoucher) {
            val startTime = DateTimeUtils.reformatUnsafeDateTime(coupon.startTime, newFormat)
            val finishTime = DateTimeUtils.reformatUnsafeDateTime(coupon.finishTime, newFormat)
            binding?.tvMvcVoucherDuration?.text = String.format("%s - %s", startTime, finishTime)
        } else {
            val createdTime = DateTimeUtils.reformatUnsafeDateTime(coupon.createdTime, newFormat)
            binding?.tvMvcVoucherDuration?.text = createdTime
        }
    }
}
