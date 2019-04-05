package com.tokopedia.tkpd.tokocash;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.exception.SessionExpiredException;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.tokocash.balance.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.pendingcashback.domain.GetPendingCasbackUseCase;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by nabillasabbaha on 3/8/18.
 */

public class GetBalanceTokoCashWrapper {

    private GetBalanceTokoCashUseCase getBalanceTokoCashUseCase;
    private GetPendingCasbackUseCase getPendingCasbackUseCase;

    public GetBalanceTokoCashWrapper(GetBalanceTokoCashUseCase getBalanceTokoCashUseCase) {
        this.getBalanceTokoCashUseCase = getBalanceTokoCashUseCase;
    }

    public GetBalanceTokoCashWrapper(GetBalanceTokoCashUseCase getBalanceTokoCashUseCase,
                                     GetPendingCasbackUseCase getPendingCasbackUseCase) {
        this.getBalanceTokoCashUseCase = getBalanceTokoCashUseCase;
        this.getPendingCasbackUseCase = getPendingCasbackUseCase;
    }

    /*  TODO need to migrate all method who will use this method to independent get balance
     *  bcs we want to avoid use com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData
     */
    public Observable<TokoCashData> processGetBalance() {
        return getBalanceTokoCashUseCase
                .createObservable(RequestParams.EMPTY)
                .map(new TokoCashBalanceMapper());
    }

    public Observable<WalletModel> getTokoCashAccountBalance() {
        return getBalanceTokoCashUseCase
                .createObservable(RequestParams.EMPTY)
                .flatMap(new Func1<BalanceTokoCash, Observable<BalanceTokoCash>>() {
                    @Override
                    public Observable<BalanceTokoCash> call(BalanceTokoCash balanceTokoCash) {
                        if (!balanceTokoCash.getLink()) {
                            return Observable.zip(Observable.just(balanceTokoCash), getPendingCasbackUseCase.createObservable(RequestParams.EMPTY),
                                    (Func2<BalanceTokoCash, PendingCashback, BalanceTokoCash>) (balanceTokoCash1, pendingCashback1) -> {
                                            balanceTokoCash1.setPendingCashback(pendingCashback1.getAmountText());
                                            balanceTokoCash1.setAmountPendingCashback(pendingCashback1.getAmount());
                                        return balanceTokoCash1;
                                    });
                        }
                        balanceTokoCash.setPendingCashback("");
                        balanceTokoCash.setAmountPendingCashback(0);
                        return Observable.just(balanceTokoCash);
                    }
                })

                .map(new TokoCashAccountBalanceMapper());
    }

    public Observable<HomeHeaderWalletAction> getWalletBalanceHomeHeader() {
        return getBalanceTokoCashUseCase
                .createObservable(RequestParams.EMPTY)
                .map(new TokoCashHomeBalanceMapper());
    }


    public Observable<com.tokopedia.digital.categorylist.data.cloud.entity.tokocash.TokoCashData> processDigitalGetBalance() {
        return getBalanceTokoCashUseCase
                .createObservable(RequestParams.EMPTY)
                .map(new DigitalTokoCashBalanceMapper())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof SessionExpiredException)
                        return Observable.error(
                                new com.tokopedia.digital.categorylist.data.cloud.exception.
                                        SessionExpiredException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                        );
                    return Observable.error(throwable);
                });
    }

}
