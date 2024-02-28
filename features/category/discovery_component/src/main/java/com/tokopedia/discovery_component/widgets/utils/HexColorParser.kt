package com.tokopedia.discovery_component.widgets.utils

import android.graphics.Color
import java.util.regex.Pattern

object HexColorParser {
    fun parse(colorHex: String, action: (Int) -> Unit) {
        val regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3}|[A-Fa-f0-9]{8})$"
        val pattern: Pattern = Pattern.compile(regex)

        if (pattern.matcher(colorHex).matches()) {
            action.invoke(Color.parseColor(colorHex))
        }
    }
}
