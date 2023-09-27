package com.tokopedia.stories.analytics

import com.tokopedia.stories.view.model.StoriesArgsModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 14/09/23
 */
class StoriesAnalytics @AssistedInject constructor(
    @Assisted args: StoriesArgsModel,
    storiesRoomAnalytic: StoriesRoomAnalytic.Factory,
    sharingAnalytics: StoriesSharingAnalytics.Factory
) : StoriesSharingAnalytics by sharingAnalytics.create(args.authorId),
    StoriesRoomAnalytic by storiesRoomAnalytic.create(args) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted args: StoriesArgsModel,
        ): StoriesAnalytics
    }
}
