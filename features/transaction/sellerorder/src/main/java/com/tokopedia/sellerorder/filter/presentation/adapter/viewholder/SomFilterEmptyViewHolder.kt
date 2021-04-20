package com.tokopedia.sellerorder.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterEmptyUiModel

class SomFilterEmptyViewHolder(view: View): AbstractViewHolder<SomFilterEmptyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_state_som_filter
    }

    override fun bind(element: SomFilterEmptyUiModel?) {}
}