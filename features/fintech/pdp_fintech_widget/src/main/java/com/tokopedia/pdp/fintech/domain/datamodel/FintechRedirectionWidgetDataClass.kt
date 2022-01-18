package com.tokopedia.pdp.fintech.domain.datamodel

data class FintechRedirectionWidgetDataClass(
    var cta:Int = 0,
    var redirectionUrl:String? =null ,
    var productURl:String? = null,
    var price:String? = null,
    var productId:String? = null
)
