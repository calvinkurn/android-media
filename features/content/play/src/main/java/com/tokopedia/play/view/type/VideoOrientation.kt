package com.tokopedia.play.view.type


/**
 * Created by mzennis on 15/04/20.
 */
enum class VideoOrientation {
    Portrait,
    Landscape,
    Unknown;

    val isLandscape: Boolean
        get() = this == Landscape
}