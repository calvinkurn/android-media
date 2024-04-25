package com.tokopedia.feedplus.presentation.model

import com.tokopedia.content.common.util.UiEvent
import java.util.*

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
sealed class FeedTooltipEvent : UiEvent {

    override val id: Long = UUID.randomUUID().mostSignificantBits
    data class ShowTooltip(
        val text: String,
    ) : FeedTooltipEvent()

    object DismissTooltip : FeedTooltipEvent()
}
