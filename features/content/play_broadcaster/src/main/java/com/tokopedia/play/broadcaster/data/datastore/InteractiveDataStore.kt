package com.tokopedia.play.broadcaster.data.datastore


/**
 * Created by mzennis on 21/07/21.
 */
interface InteractiveDataStore {

    fun getInteractiveId(): String

    fun getSetupInteractiveTitle(): String

    fun getActiveInteractiveTitle(): String

    fun getSelectedInteractiveDuration(): Long

    fun getInteractiveDurations(): List<Long>

    fun setInteractiveId(id: String)

    fun setSetupInteractiveTitle(title: String)

    fun setActiveInteractiveTitle(title: String)

    fun setSelectedInteractiveDuration(durationInMs: Long)

    fun setInteractiveDurations(durations: List<Long>)
}