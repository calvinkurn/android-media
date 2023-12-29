package com.tokopedia.home_component.widget.mission

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.MissionWidgetCardItemViewHolder
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.MissionWidgetClearItemViewHolder
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel

class MissionWidgetTypeFactoryImpl(
    private val missionWidgetComponentListener: MissionWidgetComponentListener,
    private val commonCarouselTypeFactory: CommonCarouselProductCardTypeFactory = CommonCarouselProductCardTypeFactoryImpl()
) : MissionWidgetTypeFactory,
    CommonCarouselProductCardTypeFactory by commonCarouselTypeFactory{
    override fun type(dataModel: CarouselMissionWidgetDataModel): Int {
        return if(dataModel.type == MissionWidgetListDataModel.Type.CLEAR)
            MissionWidgetClearItemViewHolder.LAYOUT
        else MissionWidgetCardItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            MissionWidgetCardItemViewHolder.LAYOUT -> {
                MissionWidgetCardItemViewHolder(view, missionWidgetComponentListener)
            }
            MissionWidgetClearItemViewHolder.LAYOUT -> {
                MissionWidgetClearItemViewHolder(view, missionWidgetComponentListener)
            }
            else -> commonCarouselTypeFactory.createViewHolder(view, viewType)
        }
    }
}
