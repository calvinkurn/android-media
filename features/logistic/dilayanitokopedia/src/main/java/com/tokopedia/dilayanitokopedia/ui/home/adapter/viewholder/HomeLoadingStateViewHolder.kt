package com.tokopedia.dilayanitokopedia.ui.home.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLoadingStateUiModel

class HomeLoadingStateViewHolder(
    itemView: View
) : AbstractViewHolder<HomeLoadingStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dt_home_loading_state
    }

    override fun bind(element: HomeLoadingStateUiModel?) {
        // no-op
    }
}
