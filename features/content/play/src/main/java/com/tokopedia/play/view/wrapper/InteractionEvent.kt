package com.tokopedia.play.view.wrapper

import java.util.*

/**
 * Created by jegul on 18/12/19
 */
enum class InteractionEvent(val needLogin: Boolean) {

    SendChat(true),
    Like(true);

    companion object {
        private val values = values()

        val needLoginEvents = EnumSet.copyOf(values.filter(InteractionEvent::needLogin))
    }
}