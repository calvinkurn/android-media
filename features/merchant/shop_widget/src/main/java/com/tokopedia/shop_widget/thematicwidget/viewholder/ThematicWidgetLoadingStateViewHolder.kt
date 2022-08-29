package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel

class ThematicWidgetLoadingStateViewHolder (
    itemView: View
) : AbstractViewHolder<ThematicWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_thematic_widget_loading_state
    }

    override fun bind(element: ThematicWidgetUiModel) {
       /* nothing to do */
    }
}