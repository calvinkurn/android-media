package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.net.Uri

object MacroIntent {
    /**
     * Target test package
     * In this test class, the target is :testapp with package com.tokopedia.tkpd
     * Target package for dynamic feature module is com.tokopedia.tkpd.df_${module_name}
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
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://home/navigation")
            return intent
        }

        fun getHomeMacroSetupIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("tokopedia-android-internal://home-setting/opt/macrobenchmark")
            return intent
        }
    }

    object App {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "home_fragment_recycler_view"

        fun getAppLauncherIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://home-setting/opt/macrobenchmark")
            return intent
        }

        fun getAppMacroSetupIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
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
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://home/navigation?TAB_POSITION=2")
            return intent
        }

        fun getOsMacroSetupIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("tokopedia-android-internal://home-setting/opt/macrobenchmark")
            return intent
        }
    }

    object ShopPage {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_HOME_TAB_RESOURCE_ID = "recycler_view"

        private const val DF_MODULE_NAME = "df_base"
        const val PACKAGE_NAME = "$TKPD_PACKAGE_NAME.$DF_MODULE_NAME"

        private const val SAMPLE_SHOP_ID = "3418893"

        fun getShopPageHomeTabIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://marketplace/shop-page/$SAMPLE_SHOP_ID/home")
            return intent
        }
    }

    object TokopediaNow {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "rv_home"

        private const val DF_MODULE_NAME = "df_tokopedianow"
        const val PACKAGE_NAME = "$TKPD_PACKAGE_NAME.$DF_MODULE_NAME"

        fun getHomeIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://now/home")
            return intent
        }
    }

    object SearchResult {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "recyclerview"

        private const val DF_MODULE_NAME = "df_base"
        const val PACKAGE_NAME = "$TKPD_PACKAGE_NAME.$DF_MODULE_NAME"

        fun getSearchResultIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://discovery/search-result?q=samsung")
            return intent
        }
    }

    object Play {
        /**
         * Target viewpager
         * Capture view by resource id
         */
        const val VIEW_PAGER_RESOURCE_ID = "vp_fragment"

        private const val DF_MODULE_NAME = "df_base"
        const val PACKAGE_NAME = "$TKPD_PACKAGE_NAME.$DF_MODULE_NAME"

        fun getPlayIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://play/186068")
            return intent
        }
    }
}
