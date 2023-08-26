package com.tokopedia.home_component.widget.mission

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.MissionWidgetItemViewHolder

class MissionWidgetTypeFactoryImpl(
    private val missionWidgetComponentListener: MissionWidgetComponentListener,
) : CommonCarouselProductCardTypeFactoryImpl(), MissionWidgetTypeFactory {
    override fun type(dataModel: CarouselMissionWidgetDataModel): Int {
        return MissionWidgetItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            MissionWidgetItemViewHolder.LAYOUT -> {
                MissionWidgetItemViewHolder(view, missionWidgetComponentListener)
            }
            else -> super.createViewHolder(view, viewType)
        }
    }
}
