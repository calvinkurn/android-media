package com.tokopedia.play.broadcaster.data.datastore


/**
 * Created by mzennis on 21/07/21.
 */
interface InteractiveDataStore {

    fun getInteractiveTitle(): String

    fun getSelectedInteractiveDuration(): Long

    fun getInteractiveDurations(): List<Long>

    fun setInteractiveTitle(title: String)

    fun setSelectedInteractiveDuration(durationInMs: Long)

    fun setRemainingLiveDuration(durationInMs: Long)

    fun setInteractiveDurations(durations: List<Long>)
}