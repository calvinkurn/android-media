package com.tokopedia.play.view.type


/**
 * Created by mzennis on 15/04/20.
 */
enum class VideoOrientation(val value: String) {
    Portrait("vertical"),
    Landscape("horizontal"),
    Unknown("");

    val isLandscape: Boolean
        get() = this == Landscape
}