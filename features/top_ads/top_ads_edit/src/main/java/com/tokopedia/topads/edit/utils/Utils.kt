package com.tokopedia.topads.edit.utils

import com.tokopedia.topads.common.data.internal.ParamObject.AUTO_BID_STATE

object Utils {

    fun getGroupStrategy(isAutoBid: Boolean): String {
        return if (isAutoBid) AUTO_BID_STATE else ""
    }

    val String.isAutoBid: Boolean
        get() {
            return this.isNotEmpty()
        }
}
