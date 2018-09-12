package com.tokopedia.reksadana.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import javax.inject.Inject;

public class TxListPresenter extends BaseDaggerPresenter<BuySellTxContract.TxListView> implements BuySellTxContract.TxListPresenter{
    @Inject
    public TxListPresenter() {
    }
}
