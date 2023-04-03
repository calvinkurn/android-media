package com.tokopedia.play.broadcaster.analytic.beautification

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.helper.PlayBroadcasterAnalyticHelper
import com.tokopedia.play.broadcaster.analytic.sender.PlayBroadcasterAnalyticSender
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
class PlayBroadcastBeautificationAnalyticImpl @Inject constructor(
    private val analyticSender: PlayBroadcasterAnalyticSender
) : PlayBroadcastBeautificationAnalytic {

    override fun clickBeautificationEntryPointOnPreparationPage(account: ContentAccountUiModel) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - beautification entry point",
            account = account,
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41378",
                sellerAppTrackerId = "41355",
            )
        )
    }

    override fun viewBeautificationCoachmark(account: ContentAccountUiModel) {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - beautification coachmark",
            account = account,
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41379",
                sellerAppTrackerId = "41356",
            )
        )
    }

    override fun clickCloseBeautificationCoachmark(account: ContentAccountUiModel) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - close beautification coachmark",
            account = account,
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41380",
                sellerAppTrackerId = "41357",
            )
        )
    }

    override fun openScreenBeautificationEntryPointOnPreparationPage() {
        analyticSender.sendGeneralOpenScreen(
            screenName = "/play broadcast - beautification filter entry point",
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41381",
                sellerAppTrackerId = "41358",
            )
        )
    }

    override fun openScreenBeautificationBottomSheet() {
        analyticSender.sendGeneralOpenScreen(
            screenName = "/play broadcast - beauty filter creation bottomsheet",
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41382",
                sellerAppTrackerId = "41359",
            )
        )
    }

    override fun clickCustomFace(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        customFace: String
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - custom face shaping",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                filterName = customFace,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41383",
                sellerAppTrackerId = "41360",
            )
        )
    }

    override fun clickNoneCustomFace(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - none beauty effects",
            eventLabel = getEventLabel(
                account = account,
                page = page,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41384",
                sellerAppTrackerId = "41361",
            )
        )
    }

    override fun clickBeautificationTab(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        tab: PlayBroadcastBeautificationAnalytic.Tab
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - beauty filter tab",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                tab = tab,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41385",
                sellerAppTrackerId = "41362",
            )
        )
    }

    override fun clickBeautyFilterReset(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - beauty filter reset",
            eventLabel = getEventLabel(
                account = account,
                page = page,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41386",
                sellerAppTrackerId = "41363",
            )
        )
    }

    override fun clickSliderBeautyFilter(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        tab: PlayBroadcastBeautificationAnalytic.Tab,
        filterName: String
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - slider beauty filter",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                tab = tab,
                filterName = filterName,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41387",
                sellerAppTrackerId = "41364",
            )
        )
    }

    override fun viewResetFilterPopup(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        tab: PlayBroadcastBeautificationAnalytic.Tab
    ) {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - reset filter bottomsheet",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                tab = tab,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41388",
                sellerAppTrackerId = "41365",
            )
        )
    }

    override fun clickYesResetFilter(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - yes reset filter",
            eventLabel = getEventLabel(
                account = account,
                page = page,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41389",
                sellerAppTrackerId = "41366",
            )
        )
    }

    override fun clickPresetMakeup(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        preset: String
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - preset makeup",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                filterName = preset,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41390",
                sellerAppTrackerId = "41367",
            )
        )
    }

    override fun clickNonePreset(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - none reset preset makeup",
            eventLabel = getEventLabel(
                account = account,
                page = page,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41391",
                sellerAppTrackerId = "41368",
            )
        )
    }

    override fun clickDownloadPreset(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        preset: String
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - download asset preset makeup",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                filterName = preset,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41392",
                sellerAppTrackerId = "41369",
            )
        )
    }

    override fun clickRetryDownloadPreset(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        preset: String
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - retry download preset makeup",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                filterName = preset,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41393",
                sellerAppTrackerId = "41370",
            )
        )
    }

    override fun viewFailDownloadPreset(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        preset: String
    ) {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - failed download preset makeup",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                filterName = preset,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41394",
                sellerAppTrackerId = "41371",
            )
        )
    }

    override fun viewFailApplyBeautyFilter(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        customFace: String
    ) {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - failed apply beauty filter",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                filterName = customFace,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41395",
                sellerAppTrackerId = "41372",
            )
        )
    }

    override fun clickRetryApplyBeautyFilter(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        customFace: String
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - failed apply beauty filter",
            eventLabel = getEventLabel(
                account = account,
                page = page,
                filterName = customFace,
            ),
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41396",
                sellerAppTrackerId = "41373",
            )
        )
    }

    override fun clickBeautificationEntryPointOnLivePage(account: ContentAccountUiModel) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - beauty filter ongoing livestream",
            account = account,
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41397",
                sellerAppTrackerId = "41374",
            )
        )
    }

    override fun viewBeautificationEntryPointOnLivePage(account: ContentAccountUiModel) {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - beauty filter ongoing livestream",
            account = account,
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41398",
                sellerAppTrackerId = "41375",
            )
        )
    }

    override fun clickRetryReapplyBeautyFilter(account: ContentAccountUiModel) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - reapply beauty filter",
            account = account,
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41399",
                sellerAppTrackerId = "41376",
            )
        )
    }

    override fun viewFailReapplyBeautyFilter(account: ContentAccountUiModel) {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - reapply beauty filter",
            account = account,
            trackerId = PlayBroadcasterAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "41400",
                sellerAppTrackerId = "41377",
            )
        )
    }

    private fun getEventLabel(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
    ): String {
        return "${PlayBroadcasterAnalyticHelper.getEventLabelByAccount(account)} - ${page.value}"
    }

    private fun getEventLabel(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        tab: PlayBroadcastBeautificationAnalytic.Tab,
    ): String {
        return "${getEventLabel(account, page)} - ${tab.value}"
    }

    private fun getEventLabel(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        filterName: String,
    ): String {
        return "${getEventLabel(account, page)} - $filterName"
    }

    private fun getEventLabel(
        account: ContentAccountUiModel,
        page: PlayBroadcastBeautificationAnalytic.Page,
        tab: PlayBroadcastBeautificationAnalytic.Tab,
        filterName: String,
    ): String {
        return "${getEventLabel(account, page, tab)} - $filterName"
    }
}
