package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodOutOfCoverageUiModel

class TokoFoodOutOfCoverageViewHolder (
    itemView: View
): AbstractViewHolder<TokoFoodOutOfCoverageUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_out_of_coverage
    }

    override fun bind(element: TokoFoodOutOfCoverageUiModel) {

    }
}