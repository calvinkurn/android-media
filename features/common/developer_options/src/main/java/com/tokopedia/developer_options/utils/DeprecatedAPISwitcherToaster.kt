package com.tokopedia.developer_options.utils

import android.content.Context
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity

fun Context.getIsEnableDeprecatedAPISwitcherToaster(): Boolean {
    val sharePref = getSharedPreferences(
        DeveloperOptionActivity.DEPRECATED_API_SWITCHER_TOASTER_SP_NAME,
        BaseActivity.MODE_PRIVATE
    )

    return sharePref.getBoolean(
        DeveloperOptionActivity.DEPRECATED_API_SWITCHER_TOASTER_KEY,
        false
    )
}

fun Context.setIsEnableDeprecatedAPISwitcherToaster(isChecked: Boolean) {
    val sharedPref = getSharedPreferences(
        DeveloperOptionActivity.DEPRECATED_API_SWITCHER_TOASTER_SP_NAME,
        BaseActivity.MODE_PRIVATE
    )

    val editor = sharedPref.edit().putBoolean(
        DeveloperOptionActivity.DEPRECATED_API_SWITCHER_TOASTER_KEY,
        isChecked
    )
    editor.apply()
}
