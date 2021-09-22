package com.tokopedia.imagepicker_insta.trackers

object TrackerProvider {
    internal var tracker: TrackerContract? = null

    fun attachTracker(trackerContract: TrackerContract) {
        tracker = trackerContract
    }

    fun removeTracker() {
        tracker = null
    }
}