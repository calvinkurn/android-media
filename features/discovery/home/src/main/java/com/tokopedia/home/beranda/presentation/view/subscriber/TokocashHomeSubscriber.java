package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.util.TokoCashUtil;
import com.tokopedia.home.beranda.presentation.view.HomeContract;

import rx.Subscriber;

/**
 * Created by meta on 16/07/18.
 */
public class TokocashHomeSubscriber extends Subscriber<TokoCashData> {

    private HomeContract.Presenter presenter;

    public TokocashHomeSubscriber(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() { }

    @Override
    public void onError(Throwable e) {
        presenter.onHeaderTokocashError();
    }

    @Override
    public void onNext(TokoCashData balanceTokoCash) {
        presenter.updateHeaderTokoCashData(TokoCashUtil.convertToActionHomeHeader(balanceTokoCash));
    }
}
