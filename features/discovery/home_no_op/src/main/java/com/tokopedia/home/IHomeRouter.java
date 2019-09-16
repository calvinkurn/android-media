package com.tokopedia.home;

import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;

import rx.Observable;

public interface IHomeRouter {

    Observable<HomeHeaderWalletAction> getWalletBalanceHomeHeader();

    Observable<TokopointHomeDrawerData> getTokopointUseCaseForHome();
}
