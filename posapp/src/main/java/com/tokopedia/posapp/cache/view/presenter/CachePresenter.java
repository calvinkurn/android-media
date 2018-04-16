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
    private static final String SHOP_ID = "shop_id";
    private static final String START_OFFSET = "startoffset";
    private static final String ROW_OFFSET = "rowoffset";
    private static final String DATA_PER_ROW = "data_per_row";
    private static final String ETALASE = "etalase";

    private Context context;
    private GetBankUseCase getBankUseCase;
    private StoreBankUsecase storeBankUsecase;
//    private GetEtalaseUseCase getEtalaseUseCase;
//    private StoreEtalaseCacheUseCase storeEtalaseCacheUseCase;

    private Cache.CallbackListener callbackListener;

//    private int defaultRowPerPage = 10;

    @Inject
    public CachePresenter(@ApplicationContext Context context,
                          GetBankUseCase getBankUseCase,
                          StoreBankUsecase storeBankUsecase,
                          GetEtalaseUseCase getEtalaseUseCase,
                          StoreEtalaseCacheUseCase storeEtalaseCacheUseCase
    ) {
        this.context = context;
        this.getBankUseCase = getBankUseCase;
        this.storeBankUsecase = storeBankUsecase;
//        this.getEtalaseUseCase = getEtalaseUseCase;
//        this.storeEtalaseCacheUseCase = storeEtalaseCacheUseCase;
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

//    private void getEtalase() {
//        RequestParams etalaseParams = RequestParams.create();
//        etalaseParams.putString(SHOP_ID, SessionHandler.getShopID(context));
//        getEtalaseUseCase.createObservable(etalaseParams)
//                .flatMap(new Func1<List<EtalaseDomain>, Observable<DataStatus>>() {
//                    @Override
//                    public Observable<DataStatus> call(List<EtalaseDomain> etalaseDomains) {
//                        ListDomain<EtalaseDomain> data = new ListDomain<>();
//                        data.setList(etalaseDomains);
//
//                        RequestParams requestParams = RequestParams.create();
//                        requestParams.putObject(StoreEtalaseCacheUseCase.DATA, data);
//
//                        return storeEtalaseCacheUseCase.createObservable(requestParams);
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<DataStatus>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(DataStatus dataStatus) {
//                        Log.d("CachePresenter", dataStatus.getMessage());
//                    }
//                });
//    }

//    private RequestParams getGatewayProductParam(String etalaseId) {
//        RequestParams params = RequestParams.create();
//        params.putString(SHOP_ID, SessionHandler.getShopID(context));
//        params.putString(START_OFFSET, "0");
//        params.putString(ROW_OFFSET, defaultRowPerPage + "");
//        params.putString(ETALASE, etalaseId);
//        params.putInt(DATA_PER_ROW, defaultRowPerPage);
//        return params;
//    }

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

//    private class SaveProductSubscriber extends Subscriber<List<DataStatus>> {
//
//        @Override
//        public void onCompleted() {
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onNext(List<DataStatus> statuses) {
//            for (DataStatus dataStatus : statuses) {
//                Log.d("o2o", "product data saved : " + dataStatus.getStatus() + " | " + dataStatus.getMessage());
//            }
//        }
//    }
}
