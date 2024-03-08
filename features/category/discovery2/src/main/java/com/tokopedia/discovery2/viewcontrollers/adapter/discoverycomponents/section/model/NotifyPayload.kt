package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section.model

import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList

data class NotifyPayload(
    val identifier: String,
    val type: ComponentsList,
    val data: Any
)
