package com.tokopedia.tokomart.common.constant

import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object ConstantKey {
    //remote config
    const val REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH = "android_user_first_install_search"
    const val REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH = "android_user_duration_auto_transition_search"

    //shared preferences
    const val SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
    const val SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

    //ab testing
    const val AB_TEST_AUTO_TRANSITION_KEY = "auto_transition"
    const val AB_TEST_EXP_NAME = AbTestPlatform.NAVIGATION_EXP_TOP_NAV
    const val AB_TEST_VARIANT_OLD = AbTestPlatform.NAVIGATION_VARIANT_OLD
    const val AB_TEST_VARIANT_REVAMP = AbTestPlatform.NAVIGATION_VARIANT_REVAMP

    //applink default
    const val PARAM_APPLINK_AUTOCOMPLETE = "?navsource={source}&hint={hint}&first_install={first_install}"
}