package com.tokopedia.tkpd.tkpdcontactus.orderquery.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;

/**
 * Created by sandeepgoyal on 12/04/18.
 */;

public interface OrderQueryTicketContract {
    public interface View extends CustomerView {
        public void addTicket(QueryTicket ticket);
        public void addPopularArticleDivider();
        public void setInvoiceNumber(String invoiceNumber);
        public void setPurchaseTitle(String title);
        public void setOrderImage(String image);

    }
    public interface Presenter extends CustomerPresenter<View> {
        public void setBuyerPurchaseList(BuyerPurchaseList buyerPurchaseList);
    }
}
