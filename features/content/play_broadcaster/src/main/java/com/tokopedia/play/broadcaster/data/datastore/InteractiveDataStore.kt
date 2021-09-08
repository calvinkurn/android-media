package com.tokopedia.play.broadcaster.data.datastore


/**
 * Created by mzennis on 21/07/21.
 */
interface InteractiveDataStore {

    fun getInteractiveId(): String

    fun getInteractiveTitle(): String

    fun getSelectedInteractiveDuration(): Long

    fun getInteractiveDurations(): List<Long>

    fun setInteractiveId(id: String)

    fun setInteractiveTitle(title: String)

    fun setSelectedInteractiveDuration(durationInMs: Long)

    fun setRemainingLiveDuration(durationInMs: Long)

    fun setInteractiveDurations(durations: List<Long>)
}