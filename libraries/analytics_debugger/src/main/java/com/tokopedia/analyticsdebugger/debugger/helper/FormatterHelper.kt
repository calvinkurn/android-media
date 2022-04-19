package com.tokopedia.analyticsdebugger.debugger.helper

import android.content.Context
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.database.STATUS_DATA_NOT_FOUND
import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.STATUS_NOT_MATCH
import com.tokopedia.analyticsdebugger.database.STATUS_PENDING

fun formatDataExcerpt(raw: String?) : String {

    if (raw == null) return ""

    val dataExcerpt = raw.replace("\\s+".toRegex(), " ")
    if (dataExcerpt.length > 100) {
        return dataExcerpt.substring(0, 100) + "..."
    } else {
        return dataExcerpt
    }
}

fun getTopAdsStatusColor(context: Context?, status: String?) : Int {
    if (context == null) return 0

    return when(status) {
        STATUS_PENDING -> context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N400)
        STATUS_DATA_NOT_FOUND -> context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y300)
        STATUS_NOT_MATCH -> context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_R500)
        STATUS_MATCH -> context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500)
        else -> 0
    }
}