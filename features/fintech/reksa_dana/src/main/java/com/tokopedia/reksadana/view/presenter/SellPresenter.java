package com.tokopedia.reksadana.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import javax.inject.Inject;

public class SellPresenter extends BaseDaggerPresenter<BuySellTxContract.SellView> {
    @Inject
    public SellPresenter() {
    }
}
