package com.tokopedia.contactus.orderquery.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.contactus.orderquery.data.SubmitTicketInvoiceData;

/**
 * Created by sandeepgoyal on 16/04/18.
 */

public interface QueryTicketDetailContract {
    public interface View extends CustomerView {
        public void setDetail(String details);
        public void setTitle(String title);
        public SubmitTicketInvoiceData getSubmitTicketInvoiceData();
    }

    public interface Presenter extends CustomerPresenter<View>{
    }

}
