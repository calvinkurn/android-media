package com.tokopedia.stories.analytics

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 14/09/23
 */
class StoriesAnalytics @AssistedInject constructor(
    @Assisted shopId: String,
    storiesRoomAnalytic: StoriesRoomAnalytic.Factory,
    sharingAnalytics: StoriesSharingAnalytics.Factory
) : StoriesSharingAnalytics by sharingAnalytics.create(shopId),
    StoriesRoomAnalytic by storiesRoomAnalytic.create(shopId) {

    @AssistedFactory
    interface Factory {
        fun create(
            shopId: String
        ): StoriesAnalytics
    }
}
