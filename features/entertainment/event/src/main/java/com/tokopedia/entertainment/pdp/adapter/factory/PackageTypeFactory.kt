package com.tokopedia.entertainment.pdp.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.entertainment.pdp.data.EventPDPTicketBanner
import com.tokopedia.entertainment.pdp.data.EventPDPTicketGroup
import com.tokopedia.entertainment.pdp.data.PackageV3

interface PackageTypeFactory: AdapterTypeFactory {

    fun type(dataModel: EventPDPTicketGroup): Int
    fun type(dataModel: EventPDPTicketBanner):Int
}