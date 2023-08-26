package com.tokopedia.home_component.widget.mission

import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

interface MissionWidgetTypeFactory: CommonCarouselProductCardTypeFactory {
   fun type(dataModel: CarouselMissionWidgetDataModel): Int = 0
}
