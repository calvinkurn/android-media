package com.tokopedia.play.data.storage

import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 22, 2023
 */
@PlayScope
class PlayPageSourceStorage @Inject constructor() {

    var pageSelected: Int = 0

    var pageSource: String = ""
        private set
        get() {
            return if (pageSelected > 0) SWIPE
            else field
        }

    fun setPageSource(sourceType: String, isChannelRecom: Boolean) {
        pageSource = if (isChannelRecom) HOME_DYNAMIC_ICON else sourceType
    }

    companion object {
        private const val HOME_DYNAMIC_ICON = "HOME_DYNAMIC_ICON"
        private const val SWIPE = "SWIPE"
    }
}
