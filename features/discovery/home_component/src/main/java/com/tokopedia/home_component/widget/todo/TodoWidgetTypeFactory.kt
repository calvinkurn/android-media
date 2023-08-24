package com.tokopedia.home_component.widget.todo

import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

interface TodoWidgetTypeFactory: CommonCarouselProductCardTypeFactory {
   fun type(dataModel: CarouselTodoWidgetDataModel): Int = 0
   fun type(dataModel: TodoErrorDataModel): Int = 0
   fun type(dataModel: TodoShimmerDataModel): Int = 0
}
