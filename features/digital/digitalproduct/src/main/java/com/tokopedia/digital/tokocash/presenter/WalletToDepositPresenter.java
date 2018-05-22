package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.digital.tokocash.interactor.ITokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.listener.IWalletToDepositView;
import com.tokopedia.digital.tokocash.model.ParamsActionHistory;
import com.tokopedia.digital.tokocash.model.WithdrawSaldo;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositPresenter implements IWalletToDepositPresenter {

    private final IWalletToDepositView view;
    private final ITokoCashHistoryInteractor interactor;

    public WalletToDepositPresenter(IWalletToDepositView view, ITokoCashHistoryInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void processMoveToSaldo(String url, ParamsActionHistory paramsActionHistory) {
        interactor.postMoveToSaldo(getWithdrawSaldoSubscriber(), url, paramsActionHistory);
        view.showProgressLoading();
    }

    private Subscriber<WithdrawSaldo> getWithdrawSaldoSubscriber() {
        return new Subscriber<WithdrawSaldo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.wrappingDataFailed();
                view.hideProgressLoading();
            }

            @Override
            public void onNext(WithdrawSaldo withdrawSaldo) {
                view.wrappingDataSuccess(withdrawSaldo.getAmount());
                view.hideProgressLoading();
            }
        };
    }
}
