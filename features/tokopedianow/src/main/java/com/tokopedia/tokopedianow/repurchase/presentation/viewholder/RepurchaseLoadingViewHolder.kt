package com.tokopedia.tokopedianow.repurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLoadingUiModel

class RepurchaseLoadingViewHolder(
    itemView: View
): AbstractViewHolder<RepurchaseLoadingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_loading
    }

    override fun bind(data: RepurchaseLoadingUiModel) {}
}
