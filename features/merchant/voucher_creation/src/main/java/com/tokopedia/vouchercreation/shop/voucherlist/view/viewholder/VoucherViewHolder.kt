package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.databinding.ItemMvcVoucherListBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import timber.log.Timber
import java.util.Locale
import kotlin.reflect.KFunction1

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherViewHolder(
    itemView: View?,
    private val listener: Listener
) : AbstractViewHolder<VoucherUiModel>(itemView) {

    private var moreButton: ImageUnify? = null
    private var btnMvcMore: ImageView? = null
    private var ctaButton: UnifyButton? = null

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_list
    }

    private var binding: ItemMvcVoucherListBinding? by viewBinding()

    init {
        moreButton = itemView?.findViewById(R.id.iu_more_button)
        btnMvcMore = itemView?.findViewById(R.id.btnMvcMore)
        ctaButton = itemView?.findViewById(R.id.btnMvcVoucherCta)
    }

    override fun bind(element: VoucherUiModel) {
        binding?.apply {
            tvMvcVoucherName.text = element.name

            val description = "${element.typeFormatted} ${element.discountAmtFormatted}"
            tvMvcVoucherDescription.text = description

            if (element.isVps) {
                labelVps.show()
                labelSubsidy.gone()
                tpgPackageName.text = element.packageName
                tpgPackageName.show()
            } else {
                labelVps.gone()
                tpgPackageName.gone()
            }

            if (element.isSubsidy && !element.isVps) {
                labelSubsidy.show()
                labelVps.gone()
            } else labelSubsidy.gone()

            val remainingAmount = element.quota - element.confirmedQuota
            val remainingVoucherText = "<b>${remainingAmount}</b>/${element.quota}"
            tvMvcRemainingVoucher.text = remainingVoucherText.parseAsHtml()

            setImageVoucher(element.isPublic, element.type)
            setVoucherStatus(element)
            showPromoCode(element.isPublic, element.code)

            setupVoucherCtaButton(element)
            setVoucherDate(element)

            imgMvcVoucherType.setOnClickListener {
                listener.onVoucherIconClickListener(element.status)
                listener.onVoucherClickListener(element.id)
            }
            moreButton?.setOnClickListener {
                listener.onMoreMenuClickListener(element)
            }
            btnMvcMore.setOnClickListener {
                listener.onMoreMenuClickListener(element)
            }
            root.setOnClickListener {
                listener.onVoucherClickListener(element.id)
            }
        }
    }

    private fun setVoucherDate(element: VoucherUiModel) {
        val isActiveVoucher =
            element.status == VoucherStatusConst.ONGOING || element.status == VoucherStatusConst.NOT_STARTED
        val oldFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val newFormat = "dd MMM yyyy"

        if (isActiveVoucher) {
            val startTime = DateTimeUtils.reformatUnsafeDateTime(element.startTime, newFormat)
            val finishTime = DateTimeUtils.reformatUnsafeDateTime(element.finishTime, newFormat)
            binding?.tvMvcVoucherDuration?.text = String.format(Locale.getDefault(),"%s - %s", startTime, finishTime)
        } else {
            /**
             * this is not fix, need to confirm which date(create/update/start/finish)
             * must be shown for voucher type deleted, ended, progress and stopped
             * */
            val createdTime = DateTimeUtils.reformatUnsafeDateTime(element.createdTime, newFormat)
            binding?.tvMvcVoucherDuration?.text = createdTime
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
                    showNewBroadCastVoucherExperience(element.showNewBc, VoucherStatusConst.ONGOING)
                }
                VoucherStatusConst.NOT_STARTED -> {
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    stringRes = R.string.mvc_edit_quota
                    clickAction = listener::onEditQuotaClickListener
                    showNewBroadCastVoucherExperience(element.showNewBc, VoucherStatusConst.NOT_STARTED)
                }
                else -> {
                    if (element.type != VoucherTypeConst.FREE_ONGKIR && !element.isSubsidy && !element.isVps) {
                        binding?.btnMvcVoucherCta?.visible()
                    } else {
                        binding?.btnMvcVoucherCta?.invisible()
                    }
                    buttonType = UnifyButton.Type.ALTERNATE
                    buttonVariant = UnifyButton.Variant.GHOST
                    stringRes = R.string.mvc_duplicate
                    clickAction = listener::onDuplicateClickListener
                    moreButton?.gone()
                }
            }
            binding?.btnMvcVoucherCta?.run {
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
            val drawableRes = when {
                isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_publik
                !isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_khusus
                isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_publik
                !isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_khusus
                else -> R.drawable.ic_mvc_cashback_publik
            }
            binding?.imgMvcVoucherType?.loadImageDrawable(drawableRes)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun showPromoCode(isPublicVoucher: Boolean, voucherCode: String) {
        binding?.apply {
            val isVisible = !isPublicVoucher
            if (isVisible) viewMvcVoucherCodeBg.visible() else viewMvcVoucherCodeBg.invisible()
            if (isVisible) tvLblCode.visible() else tvLblCode.invisible()
            if (isVisible) tvVoucherCode.visible() else tvVoucherCode.invisible()
            tvVoucherCode.text = String.format(Locale.getDefault(), " %s", voucherCode)
        }
    }

    private fun setVoucherStatus(element: VoucherUiModel) {
        binding?.apply {
            val statusStr: String
            val colorRes: Int
            val statusIndicatorBg: Int
            when (element.status) {
                VoucherStatusConst.ONGOING -> {
                    statusStr = root.context.getString(R.string.mvc_ongoing)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Green_G500
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_green
                }
                VoucherStatusConst.NOT_STARTED -> {
                    statusStr = root.context.getString(R.string.mvc_future)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_grey
                }
                VoucherStatusConst.STOPPED -> {
                    statusStr = root.context.getString(R.string.mvc_stopped)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Red_R500
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_red
                }
                VoucherStatusConst.DELETED -> {
                    statusStr = root.context.getString(R.string.mvc_deleted)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Red_R500
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_red
                }
                else -> {
                    statusStr = root.context.getString(R.string.mvc_ended)
                    colorRes = com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
                    statusIndicatorBg = R.drawable.mvc_view_voucher_status_grey
                }
            }

            tvMvcVoucherStatus.text = statusStr
            tvMvcVoucherStatus.setTextColor(root.context.getResColor(colorRes))
            viewMvcVoucherStatus.background = root.context.getResDrawable(statusIndicatorBg)
        }
    }

    private fun showNewBroadCastVoucherExperience(
        showNewBc: Boolean,
        @VoucherStatusConst voucherType: Int
    ) {
        if (showNewBc) {
            ctaButton?.gone()
            btnMvcMore?.gone()
            moreButton?.visible()
        } else {
            ctaButton?.visible()
            btnMvcMore?.visible()
            moreButton?.gone()
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
