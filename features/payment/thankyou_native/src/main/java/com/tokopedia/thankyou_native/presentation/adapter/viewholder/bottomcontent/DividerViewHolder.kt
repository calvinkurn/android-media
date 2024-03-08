package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.DividerUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.WaitingHeaderUiModel

class DividerViewHolder(
    val view: View
): AbstractViewHolder<DividerUiModel>(view) {

    companion object {
        val LAYOUT_ID = R.layout.thank_divider
    }

    override fun bind(element: DividerUiModel?) {

    }
}
