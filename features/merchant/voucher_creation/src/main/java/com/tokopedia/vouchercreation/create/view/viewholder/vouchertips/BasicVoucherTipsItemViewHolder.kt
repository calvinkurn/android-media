package com.tokopedia.vouchercreation.create.view.viewholder.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.BasicVoucherTipsItemUiModel

class BasicVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<BasicVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_basic_info
    }

    override fun bind(element: BasicVoucherTipsItemUiModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}