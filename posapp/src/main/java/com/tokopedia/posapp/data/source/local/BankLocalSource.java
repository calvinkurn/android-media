package com.tokopedia.posapp.data.source.local;

import com.tokopedia.posapp.database.manager.BankDbManager;
import com.tokopedia.posapp.database.manager.DbManager;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.result.BankSavedResult;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankLocalSource {
    private BankDbManager bankDbManager;

    public BankLocalSource(BankDbManager bankDbManager) {
        this.bankDbManager = bankDbManager;
    }

    public Observable<BankSavedResult> storeBankToCache(BankInstallmentDomain data) {
        return Observable.just(data)
                .flatMap(new Func1<BankInstallmentDomain, Observable<BankSavedResult>>() {
                    @Override
                    public Observable<BankSavedResult> call(final BankInstallmentDomain bankInstallmentDomain) {
                        return Observable.create(new Observable.OnSubscribe<BankSavedResult>() {
                            @Override
                            public void call(final Subscriber<? super BankSavedResult> subscriber) {
                                bankDbManager.store(
                                        bankInstallmentDomain.getBankDomainList(),
                                        new DbManager.TransactionListener() {
                                            @Override
                                            public void onTransactionSuccess() {
                                                BankSavedResult result = new BankSavedResult();
                                                result.setStatus(true);
                                                subscriber.onNext(result);
                                            }

                                            @Override
                                            public void onError(Throwable throwable) {
                                                BankSavedResult result = new BankSavedResult();
                                                result.setStatus(false);
                                                subscriber.onNext(result);
                                            }
                                        }
                                );
                            }
                        });
                    }
                });
    }
}
