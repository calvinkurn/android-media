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
import kotlin.reflect.KFunction1

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

            val usedVoucher = "<b>${element.confirmedQuota}</b>/${element.quota}"
            tvMvcVoucherUsed.text = usedVoucher.parseAsHtml()

            setImageVoucher(element.isPublic, element.type)
            setVoucherStatus(element)
            showPromoCode(element.isPublic, element.code)

            setupVoucherCtaButton(element)
            setVoucherDate(element)

            imgMvcVoucherType?.setOnClickListener {
                listener.onVoucherIconClickListener(element.status)
                listener.onVoucherClickListener(element.id)
            }
            btnMvcMore.setOnClickListener {
                listener.onMoreMenuClickListener(element)
            }
            setOnClickListener {
                listener.onVoucherClickListener(element.id)
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
            val buttonType: Int
            val buttonVariant: Int
            val stringRes: Int
            val clickAction: KFunction1<VoucherUiModel, Unit>

            when (element.status) {
                VoucherStatusConst.ONGOING -> {
                    buttonType = UnifyButton.Type.MAIN
                    buttonVariant = UnifyButton.Variant.FILLED
                    stringRes = R.string.mvc_share
                    clickAction = listener::onShareClickListener
                    btnMvcVoucherCta?.visible()
                }
                VoucherStatusConst.NOT_STARTED -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    stringRes = R.string.mvc_edit_quota
                    clickAction = listener::onEditQuotaClickListener
                    btnMvcVoucherCta?.visible()
                }
                else -> {
                    if (element.type != VoucherTypeConst.FREE_ONGKIR) {
                        btnMvcVoucherCta?.visible()
                    } else {
                        btnMvcVoucherCta?.invisible()
                    }
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    stringRes = R.string.mvc_duplicate
                    clickAction = listener::onDuplicateClickListener
                }
            }
            btnMvcVoucherCta?.run {
                this.buttonType = buttonType
                this.buttonVariant = buttonVariant
                text = context.getString(stringRes)
                setOnClickListener {
                    clickAction(element)
                }
            }
        }
    }

    private fun setImageVoucher(isPublic: Boolean, @VoucherTypeConst voucherType: Int) {
        try {
            with(itemView.imgMvcVoucherType) {
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

    private fun showPromoCode(isPublicVoucher: Boolean, voucherCode: String) {
        with(itemView) {
            val isVisible = !isPublicVoucher
            viewMvcVoucherCodeBg.isVisible = isVisible
            tvLblCode.isVisible = isVisible
            tvVoucherCode.isVisible = isVisible
            tvVoucherCode.text = String.format(" %s", voucherCode)
        }
    }

    private fun setVoucherStatus(element: VoucherUiModel) {
        with(itemView) {
            val statusStr: String
            val colorRes: Int
            val statusIndicatorBg: Int
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

    interface Listener {

        fun onVoucherClickListener(voucherId: Int)

        fun onMoreMenuClickListener(voucher: VoucherUiModel)

        fun onShareClickListener(voucher: VoucherUiModel)

        fun onEditQuotaClickListener(voucher: VoucherUiModel)

        fun onDuplicateClickListener(voucher: VoucherUiModel)

        fun onVoucherIconClickListener(@VoucherStatusConst status: Int)

        fun onImpressionListener(dataKey: String)

        fun onErrorTryAgain()
    }
}