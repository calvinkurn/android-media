package com.tokopedia.pdpsimulation.common.utils

object Util {

    fun getText(old: String, new: String, shouldUseNew: Boolean = false): String {
        return if (shouldUseNew) new else old
    }
}
