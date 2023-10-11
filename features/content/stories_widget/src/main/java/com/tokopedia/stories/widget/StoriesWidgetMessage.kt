package com.tokopedia.stories.widget

import com.tokopedia.content.common.util.UiEvent
import java.util.UUID

/**
 * Created by kenny.hadisaputra on 26/07/23
 */
sealed class StoriesWidgetMessage : UiEvent {

    override val id: Long = UUID.randomUUID().mostSignificantBits

    data class ShowCoachMark(val shopId: String, val text: String) : StoriesWidgetMessage()
}
