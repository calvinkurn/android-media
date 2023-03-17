package com.tokopedia.feedplus.presentation.model

import com.tokopedia.content.common.util.UiEvent
import java.util.*

/**
 * Created by kenny.hadisaputra on 17/03/23
 */
sealed class FeedMainEvent : UiEvent {

    override val id: Long = UUID.randomUUID().mostSignificantBits

    data class HasJustLoggedIn(val userName: String) : FeedMainEvent()
}
