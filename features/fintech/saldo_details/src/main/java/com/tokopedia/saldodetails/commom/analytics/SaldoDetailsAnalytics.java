package com.tokopedia.saldodetails.commom.analytics;


import javax.inject.Inject;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

public class SaldoDetailsAnalytics {

    @Inject
    public SaldoDetailsAnalytics() {
    }

    public void eventMCLImpression(String label) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_VIEW_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_IMP,
                label
        ));
    }


    public void eventAnchorLabelClick(String eventAction) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.SALDO_MAIN_SCREEN,
                String.format(SaldoDetailsConstants.Action.SALDO_ANCHOR_EVENT_ACTION, eventAction.toLowerCase()),
                SaldoDetailsConstants.EventLabel.SALDO_PAGE
        ));
    }

    public void eventMCLCardCLick(String label) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_CLICK,
                label
        ));
    }

    public void eventMCLActionItemClick(String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.FIN_SALDO_PAGE,
                String.format(SaldoDetailsConstants.Action.SALDO_MODAL_TOKO_ACTION_CLICK, action.toLowerCase()),
                label
        ));
    }
}
