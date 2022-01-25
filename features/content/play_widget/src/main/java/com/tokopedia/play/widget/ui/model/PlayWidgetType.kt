package com.tokopedia.play.widget.ui.model

import com.tokopedia.play.widget.ui.type.PlayWidgetSize

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
enum class PlayWidgetType(val typeString: String) {
    Small("SMALL"),
    Medium("MEDIUM"),
    Large("LARGE"),
    Jumbo("JUMBO"),
    Unknown("");

    companion object {
        private val values = values()

        fun getByTypeString(typeString: String): PlayWidgetType {
            values.forEach {
                if (it.typeString.equals(typeString, ignoreCase = true)) return it
            }
            throw IllegalArgumentException("Type $typeString is not supported")
        }
    }
}