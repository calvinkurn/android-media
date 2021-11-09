package com.tokopedia.play.broadcaster.data.datastore

import javax.inject.Inject


/**
 * Created by mzennis on 21/07/21.
 */
class InteractiveDataStoreImpl @Inject constructor() : InteractiveDataStore {

    private var interactiveId: String = ""
    private var mTitle = DEFAULT_INTERACTIVE_TITLE
    private var mActiveInteractiveTitle = mTitle
    private var mDurationInMs = DEFAULT_INTERACTIVE_DURATION
    private var mRemainingLiveDuration = 0L

    private val mAvailableDurations = mutableListOf<Long>()

    override fun getInteractiveId(): String {
        return interactiveId
    }

    override fun getSetupInteractiveTitle(): String {
        return mTitle
    }

    override fun getActiveInteractiveTitle(): String {
        return mActiveInteractiveTitle
    }

    override fun getSelectedInteractiveDuration(): Long {
        return if (mAvailableDurations.contains(mDurationInMs)) mDurationInMs else mAvailableDurations.first()
    }

    override fun setSetupInteractiveTitle(title: String) {
        this.mTitle = title
    }

    override fun setActiveInteractiveTitle(title: String) {
        this.mActiveInteractiveTitle = title
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

    override fun setInteractiveId(id: String) {
        interactiveId = id
    }

    companion object {
        const val DEFAULT_INTERACTIVE_TITLE = "Giveaway "
        const val DEFAULT_INTERACTIVE_DURATION = 180000L
    }

}