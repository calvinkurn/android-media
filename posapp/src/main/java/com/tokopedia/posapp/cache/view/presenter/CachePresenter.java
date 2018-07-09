package com.tokopedia.posapp.cache.view.presenter;

import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.bank.domain.model.BankInstallmentDomain;
import com.tokopedia.posapp.bank.domain.model.BankSavedResult;
import com.tokopedia.posapp.bank.domain.usecase.GetBankUseCase;
import com.tokopedia.posapp.bank.domain.usecase.GetBinUseCase;
import com.tokopedia.posapp.bank.domain.usecase.GetInstallmentUseCase;
import com.tokopedia.posapp.bank.domain.usecase.StoreBankUsecase;
import com.tokopedia.posapp.cache.view.Cache;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by okasurya on 8/29/17.
 */

public class
CachePresenter implements Cache.Presenter {
    private GetBankUseCase getBankUseCase;
    private GetInstallmentUseCase getInstallmentUseCase;
    private GetBinUseCase getBinUseCase;
    private StoreBankUsecase storeBankUsecase;

    private Cache.CallbackListener callbackListener;

    @Inject
    public CachePresenter(GetBankUseCase getBankUseCase,
                          GetInstallmentUseCase getInstallmentUseCase,
                          GetBinUseCase getBinUseCase,
                          StoreBankUsecase storeBankUsecase) {
        this.getBankUseCase = getBankUseCase;
        this.getInstallmentUseCase = getInstallmentUseCase;
        this.getBinUseCase = getBinUseCase;
        this.storeBankUsecase = storeBankUsecase;
    }

    @Override
    public void getData() {
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
        getInstallmentUseCase.createObservable(null).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<BankDomain>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<BankDomain> bankDomains) {
                        Observable.zip(
                                Observable.just(bankDomains),
                                getBinUseCase.createObservable(null),
                                new Func2<List<BankDomain>, List<BankDomain>, List<BankDomain>>() {
                                    @Override
                                    public List<BankDomain> call(List<BankDomain> installments, List<BankDomain> bins) {
                                        for(int i = 0; i < installments.size(); i++) {
                                            for(BankDomain bin: bins) {
                                                if(installments.get(i).getBankId() == bin.getBankId()) {
                                                    installments.get(i).setBankLogo(bin.getBankLogo());
                                                    installments.get(i).setBin(bin.getBin());
                                                    installments.get(i).setBinInstallment(bin.getBinInstallment());
                                                    installments.get(i).setAllowInstallment(bin.getAllowInstallment());
                                                }
                                            }
                                        }
                                        return installments;
                                    }
                                }
                        ).flatMap(storeBankToCache()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BankSavedResult>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(BankSavedResult bankSavedResult) {

                            }
                        });
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
