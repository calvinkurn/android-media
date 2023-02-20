package com.tokopedia.developer_options.utils

import android.content.Context
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity

fun Context.getIsEnableSharedPrefLeak(keyMainApp: String, keySellerApp: String): Boolean {
    val sharedPref = getSharedPreferences(
        DeveloperOptionActivity.LEAK_CANARY_TOGGLE_SP_NAME,
        BaseActivity.MODE_PRIVATE
    )
    return if (GlobalConfig.isSellerApp()) {
        sharedPref.getBoolean(
            keySellerApp,
            DeveloperOptionActivity.LEAK_CANARY_DEFAULT_TOGGLE
        )
    } else {
        sharedPref.getBoolean(
            keyMainApp,
            DeveloperOptionActivity.LEAK_CANARY_DEFAULT_TOGGLE
        )
    }
}

fun Context.setIsEnableSharedPrefLeak(keyMainApp: String, keySellerApp: String, state: Boolean) {
    val sharedPref = getSharedPreferences(
        DeveloperOptionActivity.LEAK_CANARY_TOGGLE_SP_NAME,
        BaseActivity.MODE_PRIVATE
    )
    val editor = if (GlobalConfig.isSellerApp()) {
        sharedPref.edit().putBoolean(keySellerApp, state)
    } else {
        sharedPref.edit().putBoolean(keyMainApp, state)
    }
    editor.apply()
}
