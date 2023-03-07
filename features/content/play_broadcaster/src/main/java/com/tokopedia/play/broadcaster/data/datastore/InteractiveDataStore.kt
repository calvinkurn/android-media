package com.tokopedia.play.broadcaster.data.datastore


/**
 * Created by mzennis on 21/07/21.
 */
interface InteractiveDataStore {

    fun getInteractiveId(): String

    fun getActiveInteractiveTitle(): String

    fun setInteractiveId(id: String)

    fun setActiveInteractiveTitle(title: String)
}