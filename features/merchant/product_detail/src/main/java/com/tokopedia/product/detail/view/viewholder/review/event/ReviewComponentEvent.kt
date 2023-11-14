package com.tokopedia.product.detail.view.viewholder.review.event

import com.tokopedia.product.detail.view.componentization.ComponentEvent

sealed interface ReviewComponentEvent : ComponentEvent

data class OnTopicClicked(val topic: String) : ReviewComponentEvent
