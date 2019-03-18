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

    public void eventAnchorLabelClick(String eventAction) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.SALDO_MAIN_SCREEN,
                String.format(SaldoDetailsConstants.Action.SALDO_ANCHOR_EVENT_ACTION, eventAction.toLowerCase()),
                SaldoDetailsConstants.EventLabel.SALDO_PAGE
        ));
    }
}
