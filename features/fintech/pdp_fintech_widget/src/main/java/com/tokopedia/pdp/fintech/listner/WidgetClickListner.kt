package com.tokopedia.pdp.fintech.listner

import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetBottomsheet

interface WidgetClickListner {

    fun clickedWidget(
       fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass
    )


}