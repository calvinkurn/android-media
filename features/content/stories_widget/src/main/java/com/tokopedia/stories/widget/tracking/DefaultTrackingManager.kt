package com.tokopedia.stories.widget.tracking

import com.tokopedia.stories.widget.domain.StoriesEntryPoint

/**
 * Created by kenny.hadisaputra on 23/08/23
 */
class DefaultTrackingManager(private val entryPoint: StoriesEntryPoint) : TrackingManager {

    override fun impressEntryPoints(key: StoriesEntryPoint) {
    }

    override fun clickEntryPoints(key: StoriesEntryPoint) {
    }
}
