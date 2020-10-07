package com.tokopedia.play.widget.ui.type


/**
 * Created by mzennis on 07/10/20.
 */
enum class PlayWidgetCardType(val value: String) {
    Banner("PlayWidgetBanner"),
    Channel("PlayWidgetChannel"),
    Overlay("Overlay"),
    Unknown("");

    companion object {

        private val values = values()

        fun getByValue(value: String): PlayWidgetCardType {
            values.forEach {
                if (it.value.equals(value, true)) return it
            }
            return PlayWidgetCardType.Unknown
        }
    }
}