package com.tokopedia.imagepicker_insta.common.trackers

object TrackerProvider {
    var tracker: TrackerContract? = null

    fun attachTracker(trackerContract: TrackerContract) {
        tracker = trackerContract
    }

    fun removeTracker() {
        tracker = null
    }
}