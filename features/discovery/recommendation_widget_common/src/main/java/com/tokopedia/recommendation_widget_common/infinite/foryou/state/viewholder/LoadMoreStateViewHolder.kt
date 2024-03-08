package com.tokopedia.recommendation_widget_common.infinite.foryou.state.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.LoadMoreStateModel

class LoadMoreStateViewHolder(view: View) : AbstractViewHolder<LoadMoreStateModel>(view) {

    override fun bind(element: LoadMoreStateModel?) = Unit

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_load_more
    }
}

