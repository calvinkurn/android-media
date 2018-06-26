package com.tokopedia.contactus.home.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;

import java.util.List;

/**
 * Created by sandeepgoyal on 11/04/18.
 */

public interface PurchaseListContract {
    public interface View extends CustomerView {
        public void setPurchaseList(List<BuyerPurchaseList> buyerPurchaseLists);
        public void setEmptyLayout();

    }

    public interface Presenter extends CustomerPresenter<PurchaseListContract.View> {
    }
}
