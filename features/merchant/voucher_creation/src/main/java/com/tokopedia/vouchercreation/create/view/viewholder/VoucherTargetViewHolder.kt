package com.tokopedia.vouchercreation.create.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetUiModel

class VoucherTargetViewHolder(itemView: View) : AbstractViewHolder<VoucherTargetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.mvc_voucher_target_widget
    }

    override fun bind(element: VoucherTargetUiModel) {

    }
}