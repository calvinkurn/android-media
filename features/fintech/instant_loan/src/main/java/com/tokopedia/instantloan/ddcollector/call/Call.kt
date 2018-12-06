//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.call

import android.content.ContentResolver
import android.net.Uri
import android.provider.CallLog.Calls

import com.tokopedia.instantloan.ddcollector.BaseContentCollector

import java.util.ArrayList

class Call(contentResolver: ContentResolver) : BaseContentCollector(contentResolver) {

    override fun getType(): String {
        return DD_CALL
    }

    override fun getParameters(): List<String> {
        val params = ArrayList<String>()
        params.add(Calls.TYPE)
        params.add(Calls.NUMBER)
        params.add(Calls.DURATION)
        params.add(Calls.DATE)
        return params
    }

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
