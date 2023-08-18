package com.tokopedia.stories.widget

import com.tokopedia.stories.widget.domain.StoriesKey

/**
 * Created by kenny.hadisaputra on 16/08/23
 */
interface TrackingManager {

    fun impressEntryPoints(key: StoriesKey)

    fun clickEntryPoints(key: StoriesKey)
}

class DefaultTrackingManager : TrackingManager {

    override fun impressEntryPoints(key: StoriesKey) {
    }

    override fun clickEntryPoints(key: StoriesKey) {
    }
}
