package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by meta on 16/07/18.
 */
public class PendingCashbackHomeSubscriber extends Subscriber<PendingCashback> {

    private HomeContract.Presenter presenter;

    public PendingCashbackHomeSubscriber(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() { }

    @Override
    public void onError(Throwable e) {
        Timber.d(e);
    }

    @Override
    public void onNext(PendingCashback data) {

        CashBackData cashBackData = new CashBackData();
        cashBackData.setAmount(data.getAmount());
        cashBackData.setAmountText(data.getAmountText());

        presenter.updateHeaderTokoCashPendingData(cashBackData);
    }
}
