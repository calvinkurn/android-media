package com.tokopedia.events.view.contractor;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.events.domain.model.scanticket.ScanProductDetail;
import com.tokopedia.events.domain.model.scanticket.ScanTicketResponse;

public class ScanCodeContract {

    public interface ScanCodeDataView extends CustomerView {
        void renderScannedData(ScanTicketResponse scanTicketResponse);

        void ticketRedeemed();

        void showProgressBar();

        void hideProgressBar();
    }

    public interface Presenter extends CustomerPresenter<ScanCodeDataView> {
        void getScanData(String url);

        void redeemTicket(String url);

        void initialize();

        void onDestroy();
    }
}
