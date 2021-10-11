package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLoadingStateUiModel

class HomeLoadingStateViewHolder(
        itemView: View,
) : AbstractViewHolder<HomeLoadingStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_loading_state
    }

    override fun bind(element: HomeLoadingStateUiModel?) { /* to do : nothing */ }
}