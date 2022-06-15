package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcCreateWidgetTitleBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.VoucherTitleUiModel

class VoucherTitleViewHolder(itemView: View): AbstractViewHolder<VoucherTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_create_widget_title
    }

    private var binding: MvcCreateWidgetTitleBinding? by viewBinding()

    override fun bind(element: VoucherTitleUiModel) {
        binding?.widgetTitle?.text = element.title
    }
}