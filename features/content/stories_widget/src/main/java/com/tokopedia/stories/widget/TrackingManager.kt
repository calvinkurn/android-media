package com.tokopedia.stories.widget

import com.tokopedia.stories.widget.domain.StoriesEntryPoint

/**
 * Created by kenny.hadisaputra on 16/08/23
 */
interface TrackingManager {

    fun impressEntryPoints(key: StoriesEntryPoint)

    fun clickEntryPoints(key: StoriesEntryPoint)
}

class DefaultTrackingManager : TrackingManager {

    override fun impressEntryPoints(key: StoriesEntryPoint) {
    }

    override fun clickEntryPoints(key: StoriesEntryPoint) {
    }
}
