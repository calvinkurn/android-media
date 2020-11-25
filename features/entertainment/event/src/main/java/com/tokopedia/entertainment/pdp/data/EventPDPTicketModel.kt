package com.tokopedia.entertainment.pdp.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory

abstract class EventPDPTicketModel(var isChoosen: Boolean = false,
                                   var isEmpty: String = "HABIS"): Visitable<PackageTypeFactory>