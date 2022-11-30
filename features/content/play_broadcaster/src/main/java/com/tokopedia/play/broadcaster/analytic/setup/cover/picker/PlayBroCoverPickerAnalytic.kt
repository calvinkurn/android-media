package com.tokopedia.play.broadcaster.analytic.setup.cover.picker

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource

/**
 * Created By : Jonathan Darwin on November 30, 2022
 */
interface PlayBroCoverPickerAnalytic {

    fun viewAddCoverTitleBottomSheet(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickContinueOnCroppingPage(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickChangeCoverOnCroppingPage(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickAddCover(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickContinueOnAddCoverAndTitlePage(account: ContentAccountUiModel, source: PlayBroPageSource)
    
    fun viewCroppingPage(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun viewAddCoverSourceBottomSheet(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickAddCoverFromPdpSource(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickAddCoverFromCameraSource(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickAddCoverFromGallerySource(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun openCameraScreenToAddCover(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickCancelOnCameraPage(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickCaptureFromCameraPage(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickSwitchCameraOnCameraPage(account: ContentAccountUiModel, source: PlayBroPageSource)

    fun clickTimerCameraOnCameraPage(account: ContentAccountUiModel, source: PlayBroPageSource, seconds: Int)
}
