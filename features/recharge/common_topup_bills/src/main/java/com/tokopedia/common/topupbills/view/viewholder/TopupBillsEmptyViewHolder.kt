package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ViewDigitalEmptyProductListBinding

/**
 * @author by resakemal on 22/08/19
 */

class TopupBillsEmptyViewHolder(
    private val binding: ViewDigitalEmptyProductListBinding
) : AbstractViewHolder<EmptyModel>(binding.root) {

    override fun bind(element: EmptyModel) {
        with(binding) {
            topupbillsTitleEmptyProduct.text = element.title
            topupbillsDescEmptyProduct.text = element.description
        }
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.view_digital_empty_product_list
    }
}
