package com.tkpd.macrobenchmark

import android.content.Intent
import android.net.Uri

object MacroIntent {
    fun getHomeIntent(): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.setData(Uri.parse("tokopedia-android-internal://home/navigation"))
        intent.putExtra("testenv", "testenv")
        return intent
    }

    fun getHomeMacroSetupIntent(): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setData(Uri.parse("tokopedia-android-internal://setting/dev-opts/home-macrobenchmark/setup"))
        return intent
    }
}