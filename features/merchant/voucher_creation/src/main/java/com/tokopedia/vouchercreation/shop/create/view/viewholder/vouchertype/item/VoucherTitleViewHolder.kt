package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.VoucherTitleUiModel
import kotlinx.android.synthetic.main.mvc_create_widget_title.view.*

class VoucherTitleViewHolder(itemView: View): AbstractViewHolder<VoucherTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_create_widget_title
    }

    override fun bind(element: VoucherTitleUiModel) {
        itemView.widgetTitle?.text = element.title
    }
}