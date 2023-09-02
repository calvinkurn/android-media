package com.tokopedia.feedplus.browse.data.tracker

/**
 * Created by meyta.taliti on 01/09/23.
 */
interface FeedBrowseChannelTracker {

    fun sendViewChannelCardEvent()

    fun sendViewChipsWidgetEvent()

    fun sendClickChannelCardEvent()

    fun sendClickChipsWidgetEvent()

    interface Factory {
        fun create(prefix: String): FeedBrowseChannelTracker
    }
}
