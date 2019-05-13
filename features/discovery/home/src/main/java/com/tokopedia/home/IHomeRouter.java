package com.tokopedia.home;

import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;

import rx.Observable;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface IHomeRouter {
    Observable<HomeHeaderWalletAction> getWalletBalanceHomeHeader();

    Observable<TokopointHomeDrawerData> getTokopointUseCaseForHome();
}
