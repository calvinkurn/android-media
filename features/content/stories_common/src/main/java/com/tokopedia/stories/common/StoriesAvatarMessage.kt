package com.tokopedia.stories.common

import com.tokopedia.content.common.util.UiEvent
import java.util.UUID

/**
 * Created by kenny.hadisaputra on 26/07/23
 */
sealed class StoriesAvatarMessage : UiEvent {

    override val id: Long = UUID.randomUUID().mostSignificantBits
    abstract val shopId: String

    data class ShowCoachMark(override val shopId: String) : StoriesAvatarMessage()
}
