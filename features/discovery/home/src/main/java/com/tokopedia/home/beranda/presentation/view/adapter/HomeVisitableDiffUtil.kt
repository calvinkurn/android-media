package com.tokopedia.home.beranda.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.recharge_component.visitable.RechargeComponentVisitable
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationVisitable

class HomeVisitableDiffUtil : DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is HomeVisitable && newItem is HomeVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else if (oldItem is RecommendationVisitable && newItem is RecommendationVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else if (oldItem is RechargeComponentVisitable && newItem is RechargeComponentVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else false
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is CarouselPlayWidgetDataModel && newItem is CarouselPlayWidgetDataModel) {
            false
        } else if (oldItem is HomeVisitable && newItem is HomeVisitable) {
            oldItem.equalsWith(newItem)
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.equalsWith(newItem)
        }  else if (oldItem is RecommendationVisitable && newItem is RecommendationVisitable) {
            oldItem.equalsWith(newItem)
        } else if (oldItem is RechargeComponentVisitable && newItem is RechargeComponentVisitable) {
            oldItem.equalsWith(newItem)
        } else false
    }

    override fun getChangePayload(oldItem: Visitable<*>, newItem: Visitable<*>): Any? {
        return if (oldItem is CarouselPlayWidgetDataModel && newItem is CarouselPlayWidgetDataModel) {
            Unit
        } else if (oldItem is HomeVisitable && newItem is HomeVisitable) {
            oldItem.getChangePayloadFrom(newItem)
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.getChangePayloadFrom(newItem)
        } else if (oldItem is RecommendationVisitable && newItem is RecommendationVisitable) {
            oldItem.getChangePayloadFrom(newItem)
        } else if (oldItem is RechargeComponentVisitable && newItem is RechargeComponentVisitable) {
            oldItem.getChangePayloadFrom(newItem)
        } else false
    }
}
