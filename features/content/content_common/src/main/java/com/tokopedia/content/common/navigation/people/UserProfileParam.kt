package com.tokopedia.content.common.navigation.people

import android.content.Intent
import com.tokopedia.content.common.navigation.util.appendQuery

/**
 * Created By : Jonathan Darwin on February 15, 2023
 */
class UserProfileParam {

    private var selectedTab = SelectedTab.Unknown

    fun setSelectedTab(selectedTab: SelectedTab) {
        this.selectedTab = selectedTab
    }

    fun build(): String {
        return buildString {
            if(selectedTab != SelectedTab.Unknown) {
                appendQuery(KEY_SELECTED_TAB, selectedTab.key)
            }
        }
    }

    enum class SelectedTab(val key: String) {
        Feed("feed"),
        Video("video"),
        Unknown("");

        companion object {
            fun getByKey(key: String): SelectedTab {
                return values().firstOrNull { it.key == key } ?: Unknown
            }
        }
    }

    companion object {
        private const val KEY_SELECTED_TAB = "selectedTab"

        fun getSelectedTab(intent: Intent?, isRemoveAfterGet: Boolean = false): SelectedTab {
            val key = intent?.getStringExtra(KEY_SELECTED_TAB).orEmpty()
            if(isRemoveAfterGet) {
                intent?.removeExtra(KEY_SELECTED_TAB)
            }
            return SelectedTab.getByKey(key)
        }
    }
}
