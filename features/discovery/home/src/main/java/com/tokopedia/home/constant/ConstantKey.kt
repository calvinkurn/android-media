package com.tokopedia.home.constant

/**
 * Created by Lukas on 2/3/21.
 */
object ConstantKey {
    object RemoteConfigKey {
        const val APP_SHOW_RECOMENDATION_BUTTON = "mainapp_show_recomendation_button"
        const val APP_SHOW_TOKOPOINT_NATIVE = "app_enable_tokopoint_native"
        const val MAINAPP_NATIVE_PROMO_LIST = "mainapp_native_promo_list"
        const val REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH = "android_user_first_install_search"
        const val AB_TEST_REVIEW_KEY = "InboxUlasanRevamp2"
        const val HOME_SHOW_NEW_BALANCE_WIDGET = "android_customer_home_show_balance_widget"
    }

    object TkpdCache {
        const val HOME_DATA_CACHE = "HOME_DATA_CACHE"
        const val CACHE_RECHARGE_WIDGET_TAB_SELECTION = "CACHE_RECHARGE_WIDGET_TAB_SELECTION"
        const val WIDGET_RECHARGE_TAB_LAST_SELECTED = "WIDGET_RECHARGE_TAB_LAST_SELECTED"
        const val EXPLORE_DATA_CACHE = "EXPLORE_DATA_CACHE"
    }

    object RequestKey {
        const val USER_ID = "userId"
        const val DEFAULT_USER_ID = ""
    }

    object Analytics {
        object AppScreen {
            object UnifyTracking {
                const val SCREEN_UNIFY_HOME_BERANDA = "/"
            }
        }
    }

    object LocationCache {
        const val KEY_LOCATION = "KEY_FP_LOCATION"
        const val KEY_LOCATION_LAT = "KEY_FP_LOCATION_LAT"
        const val KEY_LOCATION_LONG = "KEY_FP_LOCATION_LONG"
    }

    object FirstInstallCache {
        const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
        const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"
    }

    object HomeTimber {
        const val MAX_LIMIT = 1000
    }

    object ResetPassword {
        const val KEY_MANAGE_PASSWORD = "KEY_MANAGE_PASSWORD"
        const val IS_SUCCESS_RESET = "IS_SUCCESS_RESET"
    }

    object BeautyFest {
        const val KEY_BEAUTY_FEST = "KEY_BEAUTY_FEST"
        const val KEY_IS_BEAUTY_FEST ="KEY_IS_BEAUTY_FEST"
    }
}