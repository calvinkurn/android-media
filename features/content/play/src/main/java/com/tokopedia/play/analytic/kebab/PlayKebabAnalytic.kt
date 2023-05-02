package com.tokopedia.play.analytic.kebab

import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel

/**
 * @author by astidhiyaa on 03/01/23
 */
interface PlayKebabAnalytic {
    interface Factory {
        fun create(
            channelInfo: PlayChannelInfoUiModel
        ): PlayKebabAnalytic
    }

    fun impressKebab()
    fun impressPiP()
    fun impressChromecast()
    fun impressWatchMode()
    fun impressUserReport()
    fun clickPiP()
    fun clickChromecast()
    fun clickWatchMode()
}
