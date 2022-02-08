package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.common.customview.DynamicHeaderCustomView
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ThematicWidgetViewHolder (
    itemView: View
) : AbstractViewHolder<ThematicWidgetUiModel>(itemView), CoroutineScope {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_thematic_widget
    }

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: ThematicWidgetUiModel) {
        val headerView = itemView.findViewById<DynamicHeaderCustomView>(R.id.home_component_header_view)
        headerView.setModel(element.header, null)
    }

}
