package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_list.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<VoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list
    }

    override fun bind(element: VoucherUiModel) {
        with(itemView) {
            tvMvcVoucherName.text = element.name

            val description = "${element.typeFormatted} ${element.discountAmtFormatted}"
            tvMvcVoucherDescription.text = description

            val usedVoucher = "<b>${element.bookedQuota}</b>/${element.quota}"
            tvMvcVoucherUsed.text = usedVoucher.parseAsHtml()

            setImageVoucher(element.isPublic, element.type)
            setVoucherStatus(element)
            showPromoCode(element.status, element.isPublic, element.code)
            setupVoucherCtaButton(element)
            setVoucherDate(element)

            btnMvcMore.setOnClickListener {
                listener.onMoreMenuClickListener(element)
            }
            setOnClickListener {
                listener.onVoucherClickListener(element)
            }
        }
    }

    private fun setVoucherDate(element: VoucherUiModel) {
        val isActiveVoucher = element.status == VoucherStatusConst.ONGOING || element.status == VoucherStatusConst.NOT_STARTED
        val oldFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val newFormat = "dd MMM yyyy"


        if (isActiveVoucher) {
            val startTime = DateTimeUtils.reformatUnsafeDateTime(element.startTime, newFormat)
            val finishTime = DateTimeUtils.reformatUnsafeDateTime(element.finishTime, newFormat)
            itemView.tvMvcVoucherDuration.text = String.format("%s - %s", startTime, finishTime)
        } else {
            /**
             * this is not fix, need to confirm which date(create/update/start/finish)
             * must be shown for voucher type deleted, ended, progress and stopped
             * */
            val createdTime = DateTimeUtils.reformatUnsafeDateTime(element.createdTime, newFormat)
            itemView.tvMvcVoucherDuration.text = createdTime
        }
    }

    private fun setupVoucherCtaButton(element: VoucherUiModel) {
        with(itemView) {
            var buttonType: Int = UnifyButton.Type.ALTERNATE
            var buttonVariant: Int = UnifyButton.Variant.GHOST
            var stringRes: Int = R.string.mvc_edit_quota

            when (element.status) {
                VoucherStatusConst.ONGOING -> {
                    buttonType = UnifyButton.Type.MAIN
                    buttonVariant = UnifyButton.Variant.FILLED
                    stringRes = R.string.mvc_share
                }
                VoucherStatusConst.NOT_STARTED -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    stringRes = R.string.mvc_edit_quota
                }
                VoucherStatusConst.ENDED -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    stringRes = R.string.mvc_ended
                }
            }
            btnMvcVoucherCta.buttonType = buttonType
            btnMvcVoucherCta.buttonVariant = buttonVariant
            btnMvcVoucherCta.text = context.getString(stringRes)
            btnMvcVoucherCta.setOnClickListener {
                val isOnGoingPromo = element.status == VoucherStatusConst.ONGOING
                if (isOnGoingPromo) {
                    listener.onShareClickListener(element)
                } else {
                    listener.onEditQuotaClickListener(element)
                }
            }
        }
    }

    private fun setImageVoucher(isPublic: Boolean, @VoucherTypeConst voucherType: Int) {
        try {
            with(itemView.imgMvcVoucherType) {
                /**
                 * this logic not fix, need to confirm to Product/UI.
                 * Coz voucher have 3 types but only two examples provided on Figma
                 * */
                val drawableRes = when {
                    isPublic && (voucherType == VoucherTypeConst.CASHBACK) -> R.drawable.ic_mvc_cashback_publik
                    !isPublic && (voucherType == VoucherTypeConst.CASHBACK) -> R.drawable.ic_mvc_cashback_khusus
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

    private fun showPromoCode(@VoucherStatusConst voucherStatus: Int, isPublicVoucher: Boolean, voucherCode: String) {
        with(itemView) {
            val isVisible = voucherStatus == VoucherStatusConst.ONGOING && !isPublicVoucher
            viewMvcVoucherCodeBg.isVisible = isVisible
            tvLblCode.isVisible = isVisible
            tvVoucherCode.isVisible = isVisible
            tvVoucherCode.text = String.format(" %s", voucherCode)
        }
    }

    private fun setVoucherStatus(element: VoucherUiModel) {
        with(itemView) {
            var statusStr = context.getString(R.string.mvc_not_started_yet)
            var colorRes = R.color.Neutral_N700_68
            var statusIndicatorBg = R.drawable.mvc_view_voucher_status_grey
            when (element.status) {
                VoucherStatusConst.ONGOING -> {
                    statusStr = context.getString(R.string.mvc_is_ongoing)
                    colorRes = R.color.Green_G500
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_green
                }
                VoucherStatusConst.NOT_STARTED -> {
                    statusStr = context.getString(R.string.mvc_not_started_yet)
                    colorRes = R.color.Neutral_N700_68
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_grey
                }
                VoucherStatusConst.ENDED -> {
                    statusStr = context.getString(R.string.mvc_ended)
                    colorRes = R.color.Neutral_N700_68
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_grey
                }
            }

            tvMvcVoucherStatus.text = statusStr
            tvMvcVoucherStatus.setTextColor(context.getResColor(colorRes))
            viewMvcVoucherStatus.background = context.getResDrawable(statusIndicatorBg)
        }
    }

    interface Listener {

        fun onVoucherClickListener(voucher: VoucherUiModel)

        fun onMoreMenuClickListener(voucher: VoucherUiModel)

        fun onShareClickListener(voucher: VoucherUiModel)

        fun onEditQuotaClickListener(voucher: VoucherUiModel)
    }
}