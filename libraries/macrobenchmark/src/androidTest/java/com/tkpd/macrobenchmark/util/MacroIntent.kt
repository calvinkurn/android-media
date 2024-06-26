package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.tkpd.macrobenchmark.test.R
import com.tokopedia.macrobenchmark_util.env.mock.InstrumentationMockHelper.getRawString
import com.tokopedia.macrobenchmark_util.env.mock.config.CatalogMockResponseConfig
import com.tokopedia.macrobenchmark_util.env.mock.config.HomeMockResponseConfig
import com.tokopedia.macrobenchmark_util.env.mock.config.ProductDetailMockResponseConfig
import com.tokopedia.macrobenchmark_util.env.mock.config.SearchMockResponseConfig

fun Intent.putMockData(key: String, res: Int) {
    this.putExtra(
        key,
        getRawString(
            InstrumentationRegistry.getInstrumentation().context,
            res
        )
    )
}

object MacroIntent {
    /**
     * Target test package
     * In this test class, the target is :testapp with package com.tokopedia.tkpd
     * Target package for dynamic feature module is com.tokopedia.tkpd.df_${module_name}
     */
    const val TKPD_PACKAGE_NAME = "com.tokopedia.tkpd"
    const val TKPD_PACKAGE_SELLER_APP = "com.tokopedia.sellerapp"

    /**
     * Please create intent with deeplink URI that DIRECTLY open your page
     * Don't pass intent that need to open DeeplinkActivity (which started with tokopedia://),
     * it can make macrobenchmark misinterpret DeeplinkActivity becomes the target benchmark
     */

    object Mock {
        object Config {
            const val Home = "home"
            const val Search = "search"
            const val ProductDetail = "product_detail"
            const val CatalogProductList = "catalog_product_list"
        }

        fun getMockSetupIntent(configName: String): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.setPackage(TKPD_PACKAGE_NAME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("tokopedia-android-internal://mock-setting/opt/macrobenchmark-mock/$configName")
            return intent
        }

        fun getHomeMockIntent(): Intent {
            val intent = getMockSetupIntent(Config.Home)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_POSITION, R.raw.response_mock_data_dynamic_position)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1, R.raw.response_mock_data_dynamic_home_channel_atf_1)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2, R.raw.response_mock_data_dynamic_home_channel_atf_2)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_POSITION_ICON, R.raw.response_mock_data_dynamic_position_icon)
            intent.putMockData(HomeMockResponseConfig.KEY_QUERY_DYNAMIC_POSITION_TICKER, R.raw.response_mock_data_dynamic_position_ticker)
            return intent
        }

        fun getSearchMockIntent(): Intent {
            val intent = getMockSetupIntent(Mock.Config.Search)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_SEARCH_PRODUCT, R.raw.search_common_response)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_KERO, R.raw.response_mock_choose_get_state_chosen_address)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_TOPADS_BANNER, R.raw.response_mock_search_topads_tdn)
            intent.putMockData(SearchMockResponseConfig.KEY_QUERY_HEADLINE_ADS, R.raw.response_mock_search_topads_tdn_2)
            return intent
        }

        fun getProductDetailMockIntent() = getMockSetupIntent(Mock.Config.ProductDetail).apply {
            putMockData(ProductDetailMockResponseConfig.KEY_PDP_LAYOUT, R.raw.response_pdp_layout)
            putMockData(ProductDetailMockResponseConfig.KEY_PDP_DATA, R.raw.response_pdp_data)
            putMockData(ProductDetailMockResponseConfig.KEY_PDP_RECOMM, R.raw.response_pdp_recom)
            putMockData(ProductDetailMockResponseConfig.KEY_PDP_PLAY, R.raw.response_pdp_play)
        }

        fun getCatalogProductListMockIntent() = getMockSetupIntent(Mock.Config.CatalogProductList).apply {
            putMockData(CatalogMockResponseConfig.KEY_CATALOG_PRODUCT_LIST, R.raw.search_common_response)
        }
    }

    object Session {
        fun getSessionMacroSetupIntent(packageName: String = TKPD_PACKAGE_NAME): Intent {
            val isSellerApp = packageName == TKPD_PACKAGE_SELLER_APP
            val intent = Intent("android.intent.action.VIEW")
            intent.setPackage(packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (isSellerApp) {
                intent.data = Uri.parse("sellerapp://login")
            } else {
                intent.data = Uri.parse("tokopedia://login")
            }
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
    }

    object App {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "home_fragment_recycler_view"

        fun getAppLauncherIntent(): Intent {
            val intent = Mock.getMockSetupIntent(Mock.Config.Home)
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

    object TokoFood {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "rv_home"

        private const val DF_MODULE_NAME = "df_tokofood"
        const val PACKAGE_NAME = "$TKPD_PACKAGE_NAME.$DF_MODULE_NAME"

        fun getHomeIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://food/home")
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
            intent.data = Uri.parse("tokopedia-android-internal://discovery/search-result?q=Samsung")
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

        fun getPlayVODIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://play/186068")
            return intent
        }

        fun getPlayLiveIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://play/206778")
            return intent
        }
    }

    object ProductDetail {

        private const val DF_MODULE_NAME = "df_base"
        const val PACKAGE_NAME = "$TKPD_PACKAGE_NAME.$DF_MODULE_NAME"

        const val RECYCLER_VIEW_ID = "rv_pdp"
        const val TRACE = "perf_trace_pdp"
        private const val internalDeeplink = "tokopedia-android-internal://marketplace/product-detail"

        fun getIntent() = Intent("com.tokopedia.internal.VIEW").apply {
            val parameter = MacroArgs.getParameter(InstrumentationRegistry.getArguments())

            data = if (parameter.startsWith(internalDeeplink)) {
                Uri.parse(parameter)
            } else {
                Uri.parse("$internalDeeplink/6225772444/?layoutID=4")
            }
        }
    }

    object ProductReport {

        const val COLUMN_TAG = "product_report_column"

        fun getIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://marketplace/product-report/5468977597/?dffallbackurl=https%3A%2F%2Fm.tokopedia.com%2F11530573-tammamstore%2Fkemeja-pria-pendek%2Freport%2F")
            return intent
        }
    }

    object ReschedulePickup {

        const val RECYCLER_VIEW_ID = "invoice_order_detail"

        // todo for testapp only
//        const val PACKAGE_NAME = TKPD_PACKAGE_NAME

        const val PACKAGE_NAME = TKPD_PACKAGE_SELLER_APP
        const val TRACE_NAME = "mp_reschedule_pickup"
        const val CONTENT_DESCRIPTION = "reschedule_pickup_layout"

        fun getIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://logistic/reschedulepickup?order_id=1111111")
            return intent
        }
    }

    object Catalog {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID_CATALOG_PRODUCT_LIST = "product_recyclerview"

        private const val DF_MODULE_NAME = "df_base"
        const val PACKAGE_NAME = "$TKPD_PACKAGE_NAME.$DF_MODULE_NAME"

        fun getCatalogProductListIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://catalog-product-list/Samsung?catalog_id=42417&product_sorting_status=1")
            return intent
        }
    }

    object DigitalProductDetail {

        const val RV_DENOM_GRID = "rv_denom_grid_card"
        const val RV_DENOM_FULL = "rv_denom_full_card"

        fun getPulsaPdpIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://digital/pdp_pulsa_macrobenchmark")
            return intent
        }

        fun getDataPlanPdpIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://digital/pdp_data_plan_macrobenchmark")
            return intent
        }
    }

    object RechargeHomepage {
        const val RV_RECHARGE_HOMEPAGE = "recycler_view"

        fun getRechargeHomepageIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://recharge/home/dynamic_macrobenchmark")
            return intent
        }
    }

    object SellerHome {
        /**
         * Target recyclerview
         * Capture view by resource id
         */
        const val RV_RESOURCE_ID = "recycler_view"

        fun getIntent(): Intent {
            val intent = Intent("com.tokopedia.internal.VIEW")
            intent.data = Uri.parse("tokopedia-android-internal://sellerapp/sellerhome")
            intent.`package` = TKPD_PACKAGE_SELLER_APP
            return intent
        }
    }
}
