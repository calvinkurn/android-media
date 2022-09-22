package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel

class HomeProgressBarViewHolder(
    itemView: View
): AbstractViewHolder<HomeProgressBarUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_loading_more
    }

    override fun bind(element: HomeProgressBarUiModel) {
    }
}