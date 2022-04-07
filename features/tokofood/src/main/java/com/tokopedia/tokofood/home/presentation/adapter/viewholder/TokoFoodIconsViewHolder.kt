package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodFakeTabUiModel

class TokoFoodIconsViewHolder(
    itemView: View
): AbstractViewHolder<TokoFoodFakeTabUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_icons
    }

    override fun bind(element: TokoFoodFakeTabUiModel) {

    }
}