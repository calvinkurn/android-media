package com.tokopedia.saldodetails.commom.analytics

import com.tokopedia.saldodetails.commom.di.scope.SaldoDetailsScope
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

@SaldoDetailsScope
class SaldoDetailsAnalytics @Inject constructor(
    private val userSession: dagger.Lazy<UserSession>
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendTransactionHistoryEvents(action: String, label: String = "") {
        if (action.isEmpty()) return
        val map = TrackAppUtils.gtmData(
            SaldoDetailsConstants.Event.EVENT_CLICK_SALDO,
            SaldoDetailsConstants.Category.SALDO_PAGE,
            action,
            label
        )
        sendGeneralEvent(map)
    }

    fun sendClickPaymentEvents(action: String, label: String = "") {
        if (action.isEmpty()) return
        val map = TrackAppUtils.gtmData(
            SaldoDetailsConstants.Event.EVENT_CLICK_PAYMENT,
            SaldoDetailsConstants.Category.SALDO_PAGE,
            action,
            label
        )
        sendGeneralEvent(map)
    }

    fun sendApiFailureEvents(label: String = "") {
        if (label.isEmpty()) return
        val map = TrackAppUtils.gtmData(
            SaldoDetailsConstants.Event.EVENT_SALDO_IMPRESSION,
            SaldoDetailsConstants.Category.SALDO_PAGE,
            SaldoDetailsConstants.Action.SALDO_API_FAILED,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[SaldoDetailsConstants.KEY_BUSINESS_UNIT] = SaldoDetailsConstants.VALUE_BUSINESS_UNIT
        map[SaldoDetailsConstants.KEY_CURRENT_SITE] = SaldoDetailsConstants.VALUE_CURRENT_SITE
        map[SaldoDetailsConstants.KEY_USER_ID] = userSession.get().userId
        analyticTracker.sendGeneralEvent(map)
    }

    fun eventMCLImpression(label: String) {

        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_IMP,
                label
            )
        )
    }


    fun eventAnchorLabelClick(eventAction: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.SALDO_MAIN_SCREEN,
                String.format(
                    SaldoDetailsConstants.Action.SALDO_ANCHOR_EVENT_ACTION,
                    eventAction.toLowerCase()
                ),
                SaldoDetailsConstants.EventLabel.SALDO_PAGE
            )
        )
    }

    fun eventMCLCardCLick(label: String) {

        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_CLICK,
                label
            )
        )
    }

    fun eventMCLActionItemClick(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                String.format(
                    SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_ACTION_CLICK,
                    action.toLowerCase()
                ),
                label
            )
        )
    }

    fun sendOpenScreenEvent() {
        analyticTracker.sendScreenAuthenticated(SaldoDetailsConstants.EventLabel.SALDO_PAGE,
            mutableMapOf(
                SaldoDetailsConstants.KEY_BUSINESS_UNIT to SaldoDetailsConstants.VALUE_BUSINESS_UNIT,
                SaldoDetailsConstants.KEY_CURRENT_SITE to SaldoDetailsConstants.VALUE_CURRENT_SITE
            )
        )
    }
}
