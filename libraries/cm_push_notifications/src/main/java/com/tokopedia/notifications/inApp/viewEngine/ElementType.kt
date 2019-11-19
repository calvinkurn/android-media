package com.tokopedia.notifications.inApp.viewEngine

import androidx.annotation.IntDef
data class ElementType(@get:ViewType
                       @setparam:ViewType
                       var viewType: Int) {
    var elementId: String? = ""
    companion object {
        // Declare the constants
        const val MAIN = 0
        const val BUTTON = 1
        const val RADIO_BUTTON = 2
        const val CHECKBOX = 3
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(ElementType.MAIN, ElementType.BUTTON, ElementType.RADIO_BUTTON, ElementType.CHECKBOX)
annotation class ViewType

