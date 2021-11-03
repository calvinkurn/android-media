package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.net.Uri

object MacroIntent {
    /**
     * Target test package
     * In this test class, the target is :testapp with package com.tokopedia.tkpd
     */
    const val TKPD_PACKAGE_NAME = "com.tokopedia.tkpd"

    /**
     * Please create intent with deeplink URI that DIRECTLY open your page
     * Don't pass intent that need to open DeeplinkActivity (which started with tokopedia://),
     * it can make macrobenchmark misinterpret DeeplinkActivity becomes the target benchmark
     */
    object Home {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "home_fragment_recycler_view"

        fun getHomeIntent(): Intent {
            val intent = Intent()
            intent.data = Uri.parse("tokopedia-android-internal://home/navigation")
            return intent
        }

        fun getHomeMacroSetupIntent(): Intent {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("tokopedia-android-internal://home-setting/opt/macrobenchmark")
            return intent
        }
    }

    object OfficialStore {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "os_child_recycler_view"

        fun getOsIntent(): Intent {
            val intent = Intent()
            intent.data = Uri.parse("tokopedia-android-internal://home/navigation?TAB_POSITION=2")
            return intent
        }

        fun getOsMacroSetupIntent(): Intent {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("tokopedia-android-internal://home-setting/opt/macrobenchmark")
            return intent
        }
    }
}