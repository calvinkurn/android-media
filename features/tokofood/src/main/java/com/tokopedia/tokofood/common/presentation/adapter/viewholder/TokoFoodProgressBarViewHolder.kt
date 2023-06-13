package com.tokopedia.tokofood.common.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel

class TokoFoodProgressBarViewHolder(
    itemView: View
): AbstractViewHolder<TokoFoodProgressBarUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_progress_bar
    }

    override fun bind(element: TokoFoodProgressBarUiModel?) {}
}