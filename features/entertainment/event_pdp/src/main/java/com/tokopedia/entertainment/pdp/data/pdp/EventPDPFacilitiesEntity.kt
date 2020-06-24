package com.tokopedia.entertainment.pdp.data.pdp

import com.tokopedia.entertainment.pdp.adapter.factory.EventPDPFactory
import com.tokopedia.entertainment.pdp.data.Facilities

data class EventPDPFacilitiesEntity(
        val list : List<Facilities> = emptyList()
): EventPDPModel() {
    override fun type(typeFactory: EventPDPFactory): Int {
        return typeFactory.type(this)
    }
}
