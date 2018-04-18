package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public interface SubmitTicketContract {
    public interface View extends CustomerView {
        public String getDescription();
        public void setDescriptionError(String error);
        public void setQueryTitle(String title);
        public SubmitTicketInvoiceData getSubmitTicketInvoiceData();
        public void setInvoiceNumber(String number);
        public void setInvoiceTitle(String description);
        public void setInvoiceImage(String imagePath);
    }
    public interface Presenter extends CustomerPresenter<View> {

    }
}
