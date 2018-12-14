package com.tokopedia.instantloan.ddcollector.call

import android.content.ContentResolver
import android.net.Uri
import android.provider.CallLog.Calls
import com.tokopedia.instantloan.ddcollector.BaseContentCollector

class Call(contentResolver: ContentResolver) : BaseContentCollector(contentResolver) {

    override fun getType(): String {
        return DD_CALL
    }

    override fun getParameters(): List<String> = listOf(Calls.TYPE, Calls.NUMBER, Calls.DURATION, Calls.DATE)

    override fun buildUri(): Uri {
        return Calls.CONTENT_URI
    }

    override fun getLimit(): Int {
        return -1
    }

    companion object {

        val DD_CALL = "call"
    }
}
