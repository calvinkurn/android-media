package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.view.NoAddressEmptyStateView
import com.tokopedia.tokomart.home.presentation.uimodel.HomeEmptyStateUiModel

class HomeEmptyStateViewHolder(
        itemView: View
) : AbstractViewHolder<HomeEmptyStateUiModel>(itemView), NoAddressEmptyStateView.ActionListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokonow_empty_state_no_address
    }

    override fun bind(element: HomeEmptyStateUiModel?) {
        val emptyState = itemView.findViewById<NoAddressEmptyStateView>(R.id.empty_state_no_address)
        emptyState?.setDescriptionCityName("Jakarta Utara")
        emptyState?.actionListener = this
    }

    override fun onChangeAddressClicked() {
        //to do -- ?
    }

    override fun onReturnClick() {
        //to do -- ?
    }
}