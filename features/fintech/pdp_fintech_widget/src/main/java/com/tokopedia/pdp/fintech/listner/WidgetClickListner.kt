package com.tokopedia.pdp.fintech.listner

import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass

interface WidgetClickListner {

    fun clickedWidget(
        fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass
    )


}