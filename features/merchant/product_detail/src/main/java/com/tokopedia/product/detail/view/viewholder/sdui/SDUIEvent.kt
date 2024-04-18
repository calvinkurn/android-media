package com.tokopedia.product.detail.view.viewholder.sdui

import com.tokopedia.product.detail.view.componentization.ComponentEvent

interface SDUIEvent : ComponentEvent {
    data class SendTracker(
        val eventMap: HashMap<String, Any>
    ) : SDUIEvent
}
