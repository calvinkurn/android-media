package com.tokopedia.play.broadcaster.error

import com.tokopedia.play_common.util.event.Event

/**
 * Created by jegul on 30/06/20
 */
open class EventException(errMessage: String) : Exception(errMessage) {

    constructor(err: Throwable) : this(err.localizedMessage)

    private val event = Event(Unit)

    override fun getLocalizedMessage(): String? {
        return if (event.hasBeenHandled) null
        else {
            event.getContentIfNotHandled()
            super.getLocalizedMessage()
        }
    }
}