package com.tokopedia.play.broadcaster.analytic.setup.cover.picker

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.play.broadcaster.shorts.analytic.helper.PlayShortsAnalyticHelper
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 30, 2022
 */

/**
 * Source : https://docs.google.com/spreadsheets/d/1i9Y8hLT97dLx6c3399f9ajQBORfQguzwBYtTHPDqQFI/edit#gid=0
 */
class PlayBroCoverPickerAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroCoverPickerAnalytic {

    override fun viewAddCoverTitleBottomSheet(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        viewGeneralEvent(
            action = "add cover and title",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickContinueOnCroppingPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "lanjutkan on cropping page",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickChangeCoverOnCroppingPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "ganti",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickAddCover(account: ContentAccountUiModel, source: PlayBroPageSource) {
        clickGeneralEvent(
            action = "add cover",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickContinueOnAddCoverAndTitlePage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "lanjutkan on add cover",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun viewCroppingPage(account: ContentAccountUiModel, source: PlayBroPageSource) {
        viewGeneralEvent(
            action = "cropping page",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun viewAddCoverSourceBottomSheet(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        viewGeneralEvent(
            action = "add cover source bottom sheet",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickAddCoverFromPdpSource(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "pdp photo source",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickAddCoverFromCameraSource(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "camera source",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickAddCoverFromGallerySource(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "internal gallery source",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun openCameraScreenToAddCover(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        sendScreen("/play broadcast - camera - ${getCoverPickerBasicLabel(account, source)}")
    }

    override fun clickCancelOnCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "cancel",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickCaptureFromCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "capture",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickSwitchCameraOnCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource
    ) {
        clickGeneralEvent(
            action = "switch camera on add cover",
            label = getCoverPickerBasicLabel(account, source)
        )
    }

    override fun clickTimerCameraOnCameraPage(
        account: ContentAccountUiModel,
        source: PlayBroPageSource,
        seconds: Int
    ) {
        clickGeneralEvent(
            action = "timer",
            label = "${getCoverPickerBasicLabel(account, source)} - $seconds"
        )
    }

    private fun viewGeneralEvent(action: String, label: String) {
        val eventAction = StringBuilder()
        eventAction.append(KEY_TRACK_VIEW)
        if (action.isNotBlank()) {
            eventAction.append(" ")
            eventAction.append(action)
        }

        viewCustomGeneralEvent(
            action = eventAction.toString(),
            label = label
        )
    }

    private fun viewCustomGeneralEvent(action: String, label: String) {
        sendGeneralEvent(
            event = KEY_TRACK_VIEW_EVENT,
            action = action,
            label = label
        )
    }

    private fun clickGeneralEvent(action: String, label: String) {
        val eventAction = StringBuilder()
        eventAction.append(KEY_TRACK_CLICK)
        if (action.isNotBlank()) {
            eventAction.append(" ")
            eventAction.append(action)
        }

        sendGeneralEvent(
            event = KEY_TRACK_CLICK_EVENT,
            action = eventAction.toString(),
            label = label
        )
    }

    private fun sendGeneralEvent(event: String, action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to event,
                Key.eventCategory to KEY_TRACK_CATEGORY_PLAY,
                Key.eventAction to action,
                Key.eventLabel to label,
                Key.currentSite to currentSite,
                Key.shopId to userSession.shopId,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    private fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            hashMapOf(
                Key.currentSite to currentSite,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    private fun getCoverPickerBasicLabel(account: ContentAccountUiModel, pageSource: PlayBroPageSource): String {
        return "${PlayShortsAnalyticHelper.getEventLabelByAccount(account)} - ${getPageSourceAnalytic(pageSource)}"
    }

    private fun getPageSourceAnalytic(pageSource: PlayBroPageSource): String {
        return when (pageSource) {
            PlayBroPageSource.Live -> LIVE
            PlayBroPageSource.Shorts -> SHORTS
            else -> ""
        }
    }

    companion object {
        const val LIVE = "live"
        const val SHORTS = "short"
    }
}
