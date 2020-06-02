package com.tokopedia.play.broadcaster.view.uimodel


/**
 * Created by mzennis on 24/05/20.
 */
enum class PlayChannelStatus (val value: String) {
    Active("active"),
    InActive("inactive");

    val isActive: Boolean
        get() = this == Active
}