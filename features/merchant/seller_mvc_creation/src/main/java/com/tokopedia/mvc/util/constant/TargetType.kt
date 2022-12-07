package com.tokopedia.mvc.util.constant

object TargetType {
    fun convertTargetType(isPublic: Boolean): Int {
        return if (isPublic) {
            PUBLIC
        } else {
            PRIVATE
        }
    }

    const val PUBLIC = 1
    const val PRIVATE = 0
}
