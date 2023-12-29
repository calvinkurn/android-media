package com.tokopedia.feedplus.presentation.model

import com.tokopedia.content.common.util.UiEvent
import java.util.*

/**
 * Created by kenny.hadisaputra on 07/07/23
 */
sealed class FeedPostEvent : UiEvent {

    override val id: Long = UUID.randomUUID().mostSignificantBits

    data class PreCacheVideos(val videoUrls: List<String>) : FeedPostEvent()
}
