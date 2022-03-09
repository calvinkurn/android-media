package com.tokopedia.vouchercreation.product.create.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.common.utils.highlightView
import com.tokopedia.vouchercreation.databinding.MvcVoucherTargetItemBinding
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetUiModel

class CouponTargetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: MvcVoucherTargetItemBinding? by viewBinding()

    var voucherTargetItemRadioButton = binding?.voucherTargetItemRadioButton
    private var voucherTargetLayout = binding?.voucherTargetItem
    private var voucherTargetItemTitle = binding?.voucherTargetItemTitle
    private var voucherTargetDivider = binding?.voucherTargetDivider
    private var voucherTargetPromoCodeInfo = binding?.voucherTargetPromoCodeInfo
    private var voucherTargetDisplayVoucherText = binding?.voucherTargetDisplayVoucherText
    private var voucherTargetItemDescription = binding?.voucherTargetItemDescription
    private var voucherTargetItemIcon = binding?.voucherTargetItemIcon

    fun bindData(couponTargetUiModel: CouponTargetUiModel) {
        voucherTargetDivider?.hide()
        voucherTargetDisplayVoucherText?.hide()
        voucherTargetPromoCodeInfo?.hide()

        voucherTargetItemTitle?.setText(couponTargetUiModel.titleStringRes)
        voucherTargetItemDescription?.setText(couponTargetUiModel.descriptionStringRes)
        voucherTargetItemIcon?.setImageResource(couponTargetUiModel.iconRes)
        voucherTargetLayout?.highlightView(couponTargetUiModel.selected)
        voucherTargetItemRadioButton?.isChecked = couponTargetUiModel.selected
    }
}
