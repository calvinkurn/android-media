package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetMapper
import kotlinx.android.synthetic.main.item_comparison_widget_viewholder.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PdpComparisonWidgetViewHolder(
      private val view: View,
      private val listener: DynamicProductDetailListener)
: AbstractViewHolder<PdpComparisonWidgetDataModel>(view), CoroutineScope {

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main

    companion object {
        val LAYOUT = R.layout.item_comparison_widget_viewholder
    }

    override fun bind(element: PdpComparisonWidgetDataModel) {
        launch {
            itemView.comparison_widget.setComparisonWidgetData(
                    ComparisonWidgetMapper.mapToComparisonWidgetModel(element.recommendationWidget, itemView.context),
                    listener.getStickyTitleView()
            )
        }
    }

    override fun bind(element: PdpComparisonWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }
}
