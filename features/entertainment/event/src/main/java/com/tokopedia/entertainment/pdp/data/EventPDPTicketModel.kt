package com.tokopedia.entertainment.pdp.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory

abstract class EventPDPTicketModel(
        var isEmpty: String = "HABIS"
)

abstract class EventPDPTicket: Visitable<PackageTypeFactory>

class EventPDPTicketGroup(
        var ticketModels: List<PackageV3>
): EventPDPTicket() {
    override fun type(typeFactory: PackageTypeFactory): Int {
        return typeFactory.type(this)
    }
}

class EventPDPTicketBanner: EventPDPTicket() {
    override fun type(typeFactory: PackageTypeFactory): Int {
        return typeFactory.type(this)
    }
}