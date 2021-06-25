package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.net.Uri

object MacroIntent {
    /**
     * Target test package
     * In this test class, the target is :testapp with package com.tokopedia.tkpd
     */
    const val TKPD_PACKAGE_NAME = "com.tokopedia.tkpd"

    object Home {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "home_fragment_recycler_view"

        fun getHomeIntent(): Intent {
            val intent = Intent()
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
}