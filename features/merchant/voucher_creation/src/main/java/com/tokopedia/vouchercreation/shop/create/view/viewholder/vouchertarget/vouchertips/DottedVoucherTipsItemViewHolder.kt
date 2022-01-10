package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.DottedVoucherTipsItemUiModel
import kotlinx.android.synthetic.main.mvc_voucher_tips_dotted_info.view.*

class DottedVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<DottedVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_dotted_info
    }

    override fun bind(element: DottedVoucherTipsItemUiModel) {
        itemView.run {
            voucherTipsDotDesc?.text = resources?.getString(element.descRes).toBlankOrString()
        }
    }
}