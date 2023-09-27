package com.tokopedia.stories.widget.tracking

import com.tokopedia.stories.widget.domain.StoriesEntrySource

/**
 * Created by kenny.hadisaputra on 16/08/23
 */
interface TrackingManager {

    fun impressEntryPoints(key: StoriesEntrySource)

    fun clickEntryPoints(key: StoriesEntrySource)
}
