package com.tokopedia.stories.widget.tracking

import com.tokopedia.stories.widget.domain.StoriesWidgetState

/**
 * Created by kenny.hadisaputra on 16/08/23
 */
interface TrackingManager {

    fun impressEntryPoints(state: StoriesWidgetState)

    fun clickEntryPoints(state: StoriesWidgetState)
}
