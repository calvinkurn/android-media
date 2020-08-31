package com.tokopedia.entertainment.pdp.data.pdp

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.adapter.factory.EventPDPFactory

abstract class EventPDPModel(var isLoaded: Boolean = false): Visitable<EventPDPFactory>