package com.tokopedia.feedplus.presentation.model

import com.tokopedia.content.common.util.UiEvent
import java.util.*

/**
 * Created by kenny.hadisaputra on 17/03/23
 */
sealed class FeedMainEvent : UiEvent {

    override val id: Long = UUID.randomUUID().mostSignificantBits

    object HasJustLoggedIn : FeedMainEvent()

    object ShowSwipeOnboarding : FeedMainEvent()

    data class ScrollToTop(val tabKey: String) : FeedMainEvent()

    data class SelectTab(
        val data: FeedDataModel,
        val position: Int
    ) : FeedMainEvent()
}
