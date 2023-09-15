package com.tokopedia.stories.analytic

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesAnalytics @AssistedInject constructor(
    @Assisted authorId: String,
    storiesRoomAnalytic: StoriesRoomAnalytic.Factory,
) : StoriesRoomAnalytic by storiesRoomAnalytic.create(authorId) {

    @AssistedFactory
    interface Factory {
        fun create(authorId: String): StoriesAnalytics
    }

}
