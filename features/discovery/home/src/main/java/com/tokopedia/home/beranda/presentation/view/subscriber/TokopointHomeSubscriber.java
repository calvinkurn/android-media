package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.home.beranda.presentation.view.HomeContract;

import rx.Subscriber;

/**
 * Created by meta on 16/07/18.
 */
public class TokopointHomeSubscriber extends Subscriber<TokoPointDrawerData> {

    private HomeContract.Presenter presenter;

    public TokopointHomeSubscriber(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() { }

    @Override
    public void onError(Throwable e) {
        presenter.onHeaderTokopointError();
    }

    @Override
    public void onNext(TokoPointDrawerData tokoPointDrawerData) {
        presenter.updateHeaderTokoPointData(tokoPointDrawerData);
    }
}
