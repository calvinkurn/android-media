package com.tokopedia.stories.widget.tracking

import com.tokopedia.stories.widget.domain.StoriesEntryPoint

/**
 * Created by kenny.hadisaputra on 16/08/23
 */
interface TrackingManager {

    fun impressEntryPoints(key: StoriesEntryPoint)

    fun clickEntryPoints(key: StoriesEntryPoint)
}
