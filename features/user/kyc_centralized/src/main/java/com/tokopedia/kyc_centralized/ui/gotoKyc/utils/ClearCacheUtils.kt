package com.tokopedia.kyc_centralized.ui.gotoKyc.utils

import android.content.Context
import com.tokopedia.utils.file.FileUtil
import java.io.File

fun removeGotoKycPreference(context: Context, preferenceName: String, preferenceKey: String) {
    try {
        val state = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getString(preferenceKey, "").orEmpty()
        if (state.isNotEmpty()) {
            context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .remove(preferenceKey)
                .apply()
        }
    } catch (ignored: Exception) {}
}

fun removeGotoKycImage(directory: String) {
    val file = File(directory)
    if (file.isDirectory) {
        FileUtil.deleteFolder(directory)
    }
}
