package com.tokopedia.shareexperience.ui.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.tokopedia.shareexperience.R
import timber.log.Timber

fun Activity.getStringExtraFromIntentOrQuery(key: String): String? {
    val intent = this.intent

    // Check if the value is available as query parameters in the data URI
    val data = intent.data
    if (data != null) {
        val queryParameter = data.getQueryParameter(key)
        if (queryParameter != null) {
            return queryParameter
        }
    }

    // If not found, check for intent extras
    val intentExtra = intent.getStringExtra(key)
    if (intentExtra != null) {
        return intentExtra
    }

    // If not found in either intent extra or query parameters, return null
    return null
}

fun List<String>.findIndexIgnoreCase(searchItem: String): Int {
    return this.indexOfFirst { it.equals(searchItem, ignoreCase = true) }
}

fun Context.copyTextToClipboard(text: String): Boolean {
    return try {
        val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.shareex_action_copy_link), text)
        clipboard?.setPrimaryClip(clip)
        true
    } catch (throwable: Throwable) {
        Timber.d(throwable)
        false
    }
}
