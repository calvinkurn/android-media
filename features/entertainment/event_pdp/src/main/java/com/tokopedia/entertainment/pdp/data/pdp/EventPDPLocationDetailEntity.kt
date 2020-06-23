package com.tokopedia.entertainment.pdp.data.pdp

import com.tokopedia.entertainment.pdp.adapter.factory.EventPDPFactory
import com.tokopedia.entertainment.pdp.data.Outlet
import com.tokopedia.entertainment.pdp.data.SectionData

data class EventPDPLocationDetailEntity(
        val outlet : Outlet = Outlet(),
        val sectionData: SectionData = SectionData()
): EventPDPModel() {
    override fun type(typeFactory: EventPDPFactory): Int {
        return typeFactory.type(this)
    }
}
