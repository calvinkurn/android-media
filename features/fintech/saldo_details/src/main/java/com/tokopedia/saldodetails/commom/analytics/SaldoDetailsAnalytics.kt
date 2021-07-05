package com.tokopedia.saldodetails.commom.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class SaldoDetailsAnalytics @Inject
constructor() {

    fun eventMCLImpression(label: String) {

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_IMP,
                label
        ))
    }


    fun eventAnchorLabelClick(eventAction: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.SALDO_MAIN_SCREEN,
                String.format(SaldoDetailsConstants.Action.SALDO_ANCHOR_EVENT_ACTION, eventAction.toLowerCase()),
                SaldoDetailsConstants.EventLabel.SALDO_PAGE
        ))
    }

    fun eventMCLCardCLick(label: String) {

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_CLICK,
                label
        ))
    }

    fun eventMCLActionItemClick(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                String.format(SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_ACTION_CLICK, action.toLowerCase()),
                label
        ))
    }
}
