package com.tokopedia.posapp.cache.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;
import com.tokopedia.posapp.bank.domain.usecase.GetBankUseCase;
import com.tokopedia.posapp.bank.domain.usecase.StoreBankUsecase;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.domain.model.ListDomain;
import com.tokopedia.posapp.cache.view.Cache;
import com.tokopedia.posapp.etalase.domain.GetEtalaseUseCase;
import com.tokopedia.posapp.etalase.domain.StoreEtalaseCacheUseCase;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 8/29/17.
 */

public class CachePresenter implements Cache.Presenter {
    private GetBankUseCase getBankUseCase;
    private StoreBankUsecase storeBankUsecase;

    private Cache.CallbackListener callbackListener;

    @Inject
    public CachePresenter(GetBankUseCase getBankUseCase,
                          StoreBankUsecase storeBankUsecase) {
        this.getBankUseCase = getBankUseCase;
        this.storeBankUsecase = storeBankUsecase;
    }

    @Override
    public void getData() {
//        getEtalase();
        getBankList();
    }

    @Override
    public void setCallbackListener(Cache.CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    public void onDestroy() {

    }

    private void getBankList() {
        getBankUseCase.createObservable(null)
                .flatMap(storeBankToCache())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<BankSavedResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BankSavedResult o) {

                    }
                });
    }

    private Func1<List<BankDomain>, Observable<BankSavedResult>> storeBankToCache() {
        return new Func1<List<BankDomain>, Observable<BankSavedResult>>() {
            @Override
            public Observable<BankSavedResult> call(List<BankDomain> bankDomains) {
                RequestParams requestParams = RequestParams.create();
                BankInstallmentDomain bankInstallmentDomain = new BankInstallmentDomain();
                bankInstallmentDomain.setBankDomainList(bankDomains);

                requestParams.putObject(
                        StoreBankUsecase.BANK_INSTALLMENT_DOMAIN,
                        bankInstallmentDomain
                );
                return storeBankUsecase.createObservable(requestParams);
            }
        };
    }
}
