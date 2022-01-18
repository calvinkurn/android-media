package com.tokopedia.vouchercreation.product.create.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.highlightView
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetUiModel

class CouponTargetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // TODO: Convert to ViewBinding
    var voucherTargetLayout: ViewGroup? = itemView.findViewById(R.id.voucherTargetItem)
    var voucherTargetItemRadioButton: RadioButtonUnify? = itemView.findViewById(R.id.voucherTargetItemRadioButton)
    private var voucherTargetItemTitle: Typography? = itemView.findViewById(R.id.voucherTargetItemTitle)
    private var voucherTargetDivider: View? = itemView.findViewById(R.id.voucherTargetDivider)
    private var voucherTargetPromoCodeInfo: View? = itemView.findViewById(R.id.voucherTargetPromoCodeInfo)
    private var voucherTargetDisplayVoucherText: Typography? = itemView.findViewById(R.id.voucherTargetDisplayVoucherText)
    private var voucherTargetItemDescription: TextView? = itemView.findViewById(R.id.voucherTargetItemDescription)
    private var voucherTargetItemIcon: ImageView? = itemView.findViewById(R.id.voucherTargetItemIcon)

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
