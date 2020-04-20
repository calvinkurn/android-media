package com.tokopedia.play.view.type


/**
 * Created by mzennis on 15/04/20.
 */
enum class VideoOrientation(val value: String) {
    Portrait("vertical"),
    Landscape("horizontal"),
    Unknown("unknown");

    val isLandscape: Boolean
        get() = this == Landscape

    companion object {
        private val values = values()

        fun getByValue(value: String): VideoOrientation {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return Unknown
        }
    }
}