package com.tkpd.macrobenchmark

import android.content.Intent
import android.net.Uri

object MacroIntent {
    fun getHomeIntent(): Intent {
        val intent = Intent()
        intent.setData(Uri.parse("tokopedia-android-internal://home/navigation"))
        intent.putExtra("testenv", "testenv")
        return intent
    }
}