package com.tokopedia.stories.analytic

import com.tokopedia.stories.view.model.StoriesArgsModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesAnalytics @AssistedInject constructor(
    @Assisted args: StoriesArgsModel,
    storiesRoomAnalytic: StoriesRoomAnalytic.Factory,
) : StoriesRoomAnalytic by storiesRoomAnalytic.create(args) {

    @AssistedFactory
    interface Factory {
        fun create(args: StoriesArgsModel): StoriesAnalytics
    }

}
