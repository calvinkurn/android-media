package com.tokopedia.recommendation_widget_common.widget.comparison

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleView
import kotlinx.android.synthetic.main.view_holder_comparison_widget.view.*

//class ComparisonWidgetViewHolder(val view: View, val listener: ComparisonWidgetViewHolderInterface): AbstractViewHolder<ComparisonWidgetDataModel>(view) {
//    val context = view.context
//    override fun bind(element: ComparisonWidgetDataModel) {
//        view.comparison_widget.setComparisonWidgetData(element.comparisonListModel, listener.getStickTitleView())
//    }
//
//    companion object{
//        val LAYOUT = R.layout.view_holder_comparison_widget
//    }
//}
//
//interface ComparisonWidgetViewHolderInterface {
//    fun getStickTitleView(): StickyTitleView
//}