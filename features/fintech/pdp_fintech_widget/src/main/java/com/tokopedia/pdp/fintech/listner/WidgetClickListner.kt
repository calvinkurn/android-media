package com.tokopedia.pdp.fintech.listner

interface WidgetClickListner {

    fun clickedWidget(cta: Int, url: String, tenure: Int, gatewayID: Int)

}