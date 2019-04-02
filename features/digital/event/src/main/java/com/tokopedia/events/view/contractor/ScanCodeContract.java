package com.tokopedia.events.view.contractor;

import com.tokopedia.events.domain.model.scanticket.ScanTicketResponse;

public class ScanCodeContract {

    public interface ScanCodeDataView extends EventBaseContract.EventBaseView {
        void renderScannedData(ScanTicketResponse scanTicketResponse);

        void ticketRedeemed();

    }

    public interface ScanPresenter extends EventBaseContract.EventBasePresenter {
        void getScanData(String url);

        void redeemTicket(String url);

    }
}
