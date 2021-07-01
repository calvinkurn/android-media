package com.tokopedia.play.view.storage.interactive

/**
 * Created by jegul on 01/07/21
 */
interface PlayInteractiveStorage {

    fun setActive(interactiveId: String)

    fun setInactive(interactiveId: String)

    fun setJoined(interactiveId: String)

    fun getActiveInteractiveId(): String?

    fun hasJoined(interactiveId: String): Boolean
}