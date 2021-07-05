package com.tokopedia.play.widget.ui.type

/**
 * Created by jegul on 06/10/20
 */
enum class PlayWidgetSize(val typeString: String) {

    Small("SMALL"),
    Medium("MEDIUM"),
    Large("LARGE");

    companion object {
        private val values = values()

        fun getByTypeString(typeString: String): PlayWidgetSize {
            values.forEach {
                if (it.typeString.equals(typeString, ignoreCase = true)) return it
            }
            throw IllegalArgumentException("Type $typeString is not supported")
        }
    }
}