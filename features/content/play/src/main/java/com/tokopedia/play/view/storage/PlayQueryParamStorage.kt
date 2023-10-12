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
        get() {
            return if (pageSelected > 0) SWIPE
            else field
        }

    companion object {
        private const val SWIPE = "SWIPE"
    }
}
