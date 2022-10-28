package com.tokopedia.play.broadcaster.analytic.setup.menu

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
interface PlayBroSetupMenuAnalytic {

    fun clickSetupTitleMenu()

    fun clickSetupCoverMenu()

    fun clickSetupProductMenu()

    fun clickSwitchCameraOnPreparation()

    fun clickCloseOnPreparation()

    fun clickCancelStreaming(channelId: String, title: String)

    fun clickStartStreaming(channelId: String)
}