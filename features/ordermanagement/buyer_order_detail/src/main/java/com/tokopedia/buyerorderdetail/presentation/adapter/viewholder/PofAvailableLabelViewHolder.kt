package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PofAvailableLabelUiModel

class PofAvailableLabelViewHolder(
    private val view: View
): AbstractViewHolder<PofAvailableLabelUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_available_label
    }

    override fun bind(element: PofAvailableLabelUiModel?) {}

}
