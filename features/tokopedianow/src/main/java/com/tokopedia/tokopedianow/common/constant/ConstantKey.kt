package com.tokopedia.tokopedianow.common.constant

import com.tokopedia.remoteconfig.RollenceKey


object ConstantKey {
    //remote config
    const val REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH = "android_user_first_install_search"
    const val EXPERIMENT_ENABLED = "experiment_variant"
    const val EXPERIMENT_DISABLED = "control_variant"

    //shared preferences
    const val SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
    const val SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

    //ab testing
    const val AB_TEST_AUTO_TRANSITION_KEY = "auto_transition"

    //default value
    const val PARAM_APPLINK_AUTOCOMPLETE = "?navsource={source}&placeholder={placeholder}&first_install={first_install}"
    const val DEFAULT_QUANTITY = 0
}
