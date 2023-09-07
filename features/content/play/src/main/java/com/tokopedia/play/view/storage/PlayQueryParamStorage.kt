package com.tokopedia.play.view.storage

import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 22, 2023
 */
@PlayScope
class PlayQueryParamStorage @Inject constructor() {

    var pageSelected: Int = 0

    var widgetId: String = ""

    var pageSourceName: String = ""
        private set
        get() {
            return if (pageSelected > 0) SWIPE
            else field
        }

    fun setPageSourceName(pageSourceName: String, isChannelRecom: Boolean) {
        this.pageSourceName = if (pageSourceName.isEmpty() && isChannelRecom) HOME_DYNAMIC_ICON else pageSourceName
    }

    companion object {
        private const val HOME_DYNAMIC_ICON = "HOME_DYNAMIC_ICON"
        private const val SWIPE = "SWIPE"
    }
}
