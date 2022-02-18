package com.tokopedia.videoTabComponent.util

import android.app.Activity
import android.content.Context
import com.tokopedia.kotlin.extensions.clear

object PlayFeedSharedPrefsUtil {

    fun Activity.clearTabMenuPosition() {
        saveTabMenuPosition(0)
    }

    fun Activity.saveTabMenuPosition(position: Int) {
        writeToPref(FEED_TAB_MENU_POSITION, position)
    }

    fun Activity.getTabMenuPosition() =
        getPreferences(Context.MODE_PRIVATE).getInt(FEED_TAB_MENU_POSITION, 0)

    private fun Activity.writeToPref(key: String, value: Any) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
            }
            commit()
        }
    }

    private const val FEED_TAB_MENU_POSITION = "slot_posi"
}