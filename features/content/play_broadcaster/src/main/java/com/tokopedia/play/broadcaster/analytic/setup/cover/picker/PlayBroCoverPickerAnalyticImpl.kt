package com.tokopedia.play.broadcaster.analytic.setup.cover.picker

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 30, 2022
 */

/**
 * Source : https://docs.google.com/spreadsheets/d/1i9Y8hLT97dLx6c3399f9ajQBORfQguzwBYtTHPDqQFI/edit#gid=0
 */
class PlayBroCoverPickerAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
) : PlayBroCoverPickerAnalytic {

    override fun viewAddCoverTitleBottomSheet(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickContinueOnCroppingPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickChangeCoverOnCroppingPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickAddCover(account: ContentAccountUiModel, source: PlayBroPageSource) {
        
    }

    override fun clickContinueOnAddCoverAndTitlePage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun viewCroppingPage(account: ContentAccountUiModel, source: PlayBroPageSource) {
        
    }

    override fun viewAddCoverSourceBottomSheet(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickAddCoverFromPdpSource(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickAddCoverFromCameraSource(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickAddCoverFromGallerySource(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun openCameraScreenToAddCover(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickCancelOnCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickCaptureFromCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickSwitchCameraOnCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        
    }

    override fun clickTimerCameraOnCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource,
        seconds: Int,
    ) {
        
    }

    companion object {
        const val LIVE = "live"
        const val SHORTS = "short"
    }
}
