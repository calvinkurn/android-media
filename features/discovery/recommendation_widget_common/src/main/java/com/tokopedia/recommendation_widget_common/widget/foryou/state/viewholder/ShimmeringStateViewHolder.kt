package com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ShimmeringStateModel

class ShimmeringStateViewHolder(view: View) : AbstractViewHolder<ShimmeringStateModel>(view) {

    override fun bind(element: ShimmeringStateModel?) = Unit

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_shimmering
    }
}
