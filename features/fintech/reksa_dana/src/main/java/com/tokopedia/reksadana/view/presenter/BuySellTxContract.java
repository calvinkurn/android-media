package com.tokopedia.reksadana.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface BuySellTxContract {
    interface BuyView extends CustomerView {
    }

    interface BuyPresenter extends CustomerPresenter<BuyView> {
    }

    interface SellView extends CustomerView {
    }

    interface SellPresenter extends CustomerPresenter<SellView> {
    }

    interface TxListView extends CustomerView {
    }

    interface TxListPresenter extends CustomerPresenter<TxListView>{
    }
}
