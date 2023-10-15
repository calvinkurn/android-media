package com.tokopedia.stories.widget

import com.tokopedia.stories.widget.domain.StoriesWidgetState
import com.tokopedia.stories.widget.domain.TimeMillis

/**
 * Created by kenny.hadisaputra on 24/08/23
 */
class StoriesWidgetModelBuilder {

    fun buildStoriesWidgetState(
        shopId: String = "0",
        status: StoriesStatus = StoriesStatus.NoStories,
        appLink: String = "",
        updatedAt: TimeMillis = TimeMillis.now()
    ) = StoriesWidgetState(
        shopId = shopId,
        status = status,
        appLink = appLink,
        updatedAt = updatedAt
    )
}
