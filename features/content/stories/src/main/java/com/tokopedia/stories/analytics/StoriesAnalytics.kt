package com.tokopedia.stories.analytics

import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 14/09/23
 */
class StoriesAnalytics @AssistedInject constructor(
    @Assisted trackingQueue: TrackingQueue,
    @Assisted shopId: String,
    sharingAnalytics: StoriesSharingAnalytics.Factory
) : StoriesSharingAnalytics by sharingAnalytics.create(shopId) {

    @AssistedFactory
    interface Factory {
        fun create(
            trackingQueue: TrackingQueue,
            shopId: String
        ): StoriesAnalytics
    }
}
