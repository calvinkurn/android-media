package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.macrobenchmark_util.env.mock.InstrumentationMockHelper.getRawString
import com.tokopedia.macrobenchmark_util.env.mock.config.HomeMockResponseConfig
import com.tokopedia.macrobenchmark_util.env.mock.config.SearchMockResponseConfig
import com.tkpd.macrobenchmark.test.R

fun Intent.putMockData(key: String, res: Int) {
    this.putExtra(key, getRawString(
        InstrumentationRegistry.getInstrumentation().context, res))
}

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

    object Mock {
        object Config {
            const val Home = "home"
            const val Search = "search"
        }

        fun getMockSetupIntent(configName: String): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.setPackage(TKPD_PACKAGE_NAME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("tokopedia-android-internal://mock-setting/opt/macrobenchmark-mock/$configName")
            return intent
        }

        fun getHomeMockIntent(): Intent {
            val intent = getMockSetupIntent(Config.Search)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_POSITION, R.raw.response_mock_data_dynamic_position)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1, R.raw.response_mock_data_dynamic_home_channel_atf_1)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2, R.raw.response_mock_data_dynamic_home_channel_atf_2)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_POSITION_ICON, R.raw.response_mock_data_dynamic_position_icon)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_POSITION_TICKER, R.raw.response_mock_data_dynamic_position_ticker)
            return intent
        }

        fun getSearchMockIntent(): Intent {
            val intent = Mock.getMockSetupIntent(Mock.Config.Search)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_SEARCH_PRODUCT, R.raw.search_common_response)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_KERO, R.raw.response_mock_choose_get_state_chosen_address)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_TOPADS_BANNER, R.raw.response_mock_search_topads_tdn)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_HEADLINE_ADS, R.raw.response_mock_search_topads_tdn_2)
            return intent
        }
    }

    object Session {
        fun getSessionMacroSetupIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.setPackage(TKPD_PACKAGE_NAME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            var loginParam = "non-login"
            if (MacroArgs.isLogin(InstrumentationRegistry.getArguments())){
                loginParam = "login"
            }
            intent.data = Uri.parse("tokopedia-android-internal://session-setting/opt/$loginParam")
            return intent
        }
    }

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
}
