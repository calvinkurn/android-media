package com.tokopedia.home_component.viewholders.shorten.factory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.DealsWidgetViewHolder
import com.tokopedia.home_component.viewholders.shorten.viewholder.MissionWidgetViewHolder
import com.tokopedia.home_component.visitable.shorten.DealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

class ShortenViewFactoryImpl(
    private val pool: RecyclerView.RecycledViewPool?,
    private val listener: ContainerMultiTwoSquareListener
) : BaseAdapterTypeFactory(), ShortenViewFactory {

    override fun type(model: DealsWidgetUiModel) = DealsWidgetViewHolder.LAYOUT

    override fun type(model: MissionWidgetUiModel) = MissionWidgetViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            DealsWidgetViewHolder.LAYOUT -> DealsWidgetViewHolder(parent, pool, listener)
            MissionWidgetViewHolder.LAYOUT -> MissionWidgetViewHolder(parent, pool, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
