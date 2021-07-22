package com.tokopedia.play.broadcaster.data.datastore

import javax.inject.Inject


/**
 * Created by mzennis on 21/07/21.
 */
class InteractiveDataStoreImpl @Inject constructor() : InteractiveDataStore {

    private var mTitle = DEFAULT_INTERACTIVE_TITLE
    private var mDurationInMs = DEFAULT_INTERACTIVE_DURATION
    private var mRemainingLiveDuration = 0L

    private val mAvailableDurations = mutableListOf<Long>()

    override fun getInteractiveTitle(): String {
        return mTitle
    }

    override fun getSelectedInteractiveDuration(): Long {
        return if (mAvailableDurations.contains(mDurationInMs)) mDurationInMs else mAvailableDurations.first()
    }

    override fun setInteractiveTitle(title: String) {
        this.mTitle = title
    }

    override fun setSelectedInteractiveDuration(durationInMs: Long) {
        this.mDurationInMs = durationInMs
    }

    override fun setRemainingLiveDuration(durationInMs: Long) {
        this.mRemainingLiveDuration = durationInMs
    }

    override fun setInteractiveDurations(durations: List<Long>) {
        mAvailableDurations.clear()
        mAvailableDurations.addAll(durations)
    }

    override fun getInteractiveDurations(): List<Long> {
        return mAvailableDurations.filter { it < mRemainingLiveDuration }
    }

    companion object {
        const val DEFAULT_INTERACTIVE_TITLE = "Giveaway "
        const val DEFAULT_INTERACTIVE_DURATION = 180000L
    }

}