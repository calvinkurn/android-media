package com.tokopedia.vouchercreation.create.view.viewholder.vouchertarget.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.vouchertips.BasicVoucherTipsItemUiModel
import kotlinx.android.synthetic.main.mvc_voucher_tips_basic_info.view.*

class BasicVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<BasicVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_basic_info
    }

    override fun bind(element: BasicVoucherTipsItemUiModel) {
        itemView.run {
            expensesEstimationTicker?.tickerTitle = resources?.getString(element.titleRes).toBlankOrString()
            expensesEstimationTicker?.setTextDescription(resources?.getString(element.titleRes).toBlankOrString())
        }
    }
}