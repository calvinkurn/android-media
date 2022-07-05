package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel

class TokoFoodHomeLoadingViewHolder (
    itemView: View
): AbstractViewHolder<TokoFoodHomeLoadingStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_loading_state
    }

    override fun bind(element: TokoFoodHomeLoadingStateUiModel) {}
}