package com.tokopedia.entertainment.pdp.data.checkout

class EventCheckoutAdditionalList (
        val eventCheckoutAdditionalList : List<EventCheckoutAdditionalData> = emptyList()
)

class EventCheckoutAdditionalData(
    val additionalType: AdditionalType
)

enum class AdditionalType(val type:Int){
    PACKAGE_UNFILL(1),
    PACKAGE_FILLED(2),
    ITEM_UNFILL(3),
    ITEM_FILLED(4)
}

