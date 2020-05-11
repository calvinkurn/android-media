package com.tokopedia.kotlin.util

/**
 * Created by Ade Fulki on 2020-01-23.
 * ade.hadian@tokopedia.com
 */

object LetUtil {
    inline fun <T: Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
        return if (elements.all { it != null }) {
            elements.filterNotNull()
        } else {
            closure()
        }
    }

    inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
        if (elements.all { it != null }) {
            closure(elements.filterNotNull())
        }
    }
}