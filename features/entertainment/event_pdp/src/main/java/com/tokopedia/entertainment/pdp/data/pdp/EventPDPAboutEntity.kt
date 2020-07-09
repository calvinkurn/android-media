package com.tokopedia.entertainment.pdp.data.pdp

import com.tokopedia.entertainment.pdp.adapter.factory.EventPDPFactory
import com.tokopedia.entertainment.pdp.data.SectionData

class EventPDPAboutEntity(
    val longDesc : String = "",
    val sectionData: SectionData = SectionData()
): EventPDPModel() {
    override fun type(typeFactory: EventPDPFactory): Int {
        return typeFactory.type(this)
    }
}

