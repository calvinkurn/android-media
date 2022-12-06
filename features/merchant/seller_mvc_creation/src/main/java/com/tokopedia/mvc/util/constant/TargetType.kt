package com.tokopedia.mvc.util.constant

object TargetType {
    fun convertTargetType(isPublic: Boolean): Int {
        return if (isPublic) {
            1
        } else {
            0
        }
    }
}
