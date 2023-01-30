package com.tokopedia.dilayanitokopedia.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.HomeLoadingMoreModel

class HomeLoadingMoreViewHolder(itemView: View) : AbstractViewHolder<HomeLoadingMoreModel>(itemView) {

    override fun bind(element: HomeLoadingMoreModel) {
        // no-op
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dt_home_loading_more_layout
    }
}
