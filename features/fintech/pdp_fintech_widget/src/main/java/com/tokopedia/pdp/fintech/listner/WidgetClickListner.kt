package com.tokopedia.pdp.fintech.listner

import com.tokopedia.pdp.fintech.domain.datamodel.WidgetBottomsheet

interface WidgetClickListner {

    fun clickedWidget(
        cta: Int,
        url: String,
        tenure: Int,
        gatewayBrand: String,
        widgetBottomsheet: WidgetBottomsheet,
        gateWayID: Int,
        userStatus: String,
        gatewayPartnerName: String
    )


}