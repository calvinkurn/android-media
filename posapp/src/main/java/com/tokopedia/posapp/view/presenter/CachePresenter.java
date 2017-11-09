package com.tokopedia.posapp.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.ListDomain;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
import com.tokopedia.posapp.domain.model.product.ProductDomain;
import com.tokopedia.posapp.domain.model.result.BankSavedResult;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;
import com.tokopedia.posapp.domain.model.product.ProductListDomain;
import com.tokopedia.posapp.domain.usecase.GetAllProductUseCase;
import com.tokopedia.posapp.domain.usecase.GetBankUseCase;
import com.tokopedia.posapp.domain.usecase.GetEtalaseCacheUseCase;
import com.tokopedia.posapp.domain.usecase.GetEtalaseUseCase;
import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreBankUsecase;
import com.tokopedia.posapp.domain.usecase.StoreEtalaseCacheUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;
import com.tokopedia.posapp.view.Cache;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
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
    private StoreProductCacheUseCase storeProductCacheUseCase;
    private GetBankUseCase getBankUseCase;
    private StoreBankUsecase storeBankUsecase;
    private GetEtalaseUseCase getEtalaseUseCase;
    private StoreEtalaseCacheUseCase storeEtalaseCacheUseCase;
    private GetEtalaseCacheUseCase getEtalaseCacheUseCase;
    private GetAllProductUseCase getAllProductUseCase;

    private Cache.CallbackListener callbackListener;

    private int defaultRowPerPage = 10;

    @Inject
    public CachePresenter(@ApplicationContext Context context,
                          StoreProductCacheUseCase storeProductCacheUseCase,
                          GetBankUseCase getBankUseCase,
                          StoreBankUsecase storeBankUsecase,
                          GetEtalaseUseCase getEtalaseUseCase,
                          StoreEtalaseCacheUseCase storeEtalaseCacheUseCase,
                          GetEtalaseCacheUseCase getEtalaseCacheUseCase,
                          GetAllProductUseCase getAllProductUseCase
    ) {
        this.context = context;
        this.storeProductCacheUseCase = storeProductCacheUseCase;
        this.getBankUseCase = getBankUseCase;
        this.storeBankUsecase = storeBankUsecase;
        this.getEtalaseUseCase = getEtalaseUseCase;
        this.storeEtalaseCacheUseCase = storeEtalaseCacheUseCase;
        this.getEtalaseCacheUseCase = getEtalaseCacheUseCase;
        this.getAllProductUseCase = getAllProductUseCase;
    }

    @Override
    public void getData() {
        getEtalase();
        getBankList();
    }

    @Override
    public void setCallbackListener(Cache.CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    public void onDestroy() {

    }

    private void getEtalase() {
        RequestParams etalaseParams = RequestParams.create();
        etalaseParams.putString(SHOP_ID, SessionHandler.getShopID(context));
        getEtalaseUseCase.createObservable(etalaseParams)
            .flatMap(new Func1<List<EtalaseDomain>, Observable<DataStatus>>() {
                @Override
                public Observable<DataStatus> call(List<EtalaseDomain> etalaseDomains) {
                    ListDomain<EtalaseDomain> data = new ListDomain<>();
                    data.setList(etalaseDomains);

                    RequestParams requestParams = RequestParams.create();
                    requestParams.putObject(StoreEtalaseCacheUseCase.DATA, data);

                    return storeEtalaseCacheUseCase.createObservable(requestParams);
                }
            })
            .subscribeOn(Schedulers.newThread())
            .subscribe(new Subscriber<DataStatus>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(DataStatus dataStatus) {
                    Log.d("CachePresenter", dataStatus.getMessage());
                    getAllProduct();
                }
            });
    }

    private void getAllProduct() {
        getEtalaseCacheUseCase.createObservable(RequestParams.EMPTY)
            .flatMapIterable(new Func1<List<EtalaseDomain>, Iterable<EtalaseDomain>>() {
                @Override
                public Iterable<EtalaseDomain> call(List<EtalaseDomain> etalaseDomains) {
                    return etalaseDomains;
                }
            })
            .flatMap(new Func1<EtalaseDomain, Observable<ProductListDomain>>() {
                @Override
                public Observable<ProductListDomain> call(EtalaseDomain etalaseDomain) {
                    return Observable.zip(
                            Observable.just(etalaseDomain.getEtalaseId()),
                            getAllProductUseCase.createObservable(getGatewayProductParam(etalaseDomain.getEtalaseId())),
                            new Func2<String, ProductListDomain, ProductListDomain>() {
                                @Override
                                public ProductListDomain call(String etalaseId, ProductListDomain productListDomain) {
                                    for(ProductDomain productDomain : productListDomain.getProductDomains()){
                                        productDomain.setEtalaseId(etalaseId);
                                    }
                                    return productListDomain;
                                }
                            }
                    );
                }
            })
            .flatMap(new Func1<ProductListDomain, Observable<DataStatus>>() {
                @Override
                public Observable<DataStatus> call(ProductListDomain productListDomain) {
                    return storeProductCacheUseCase.createObservable(productListDomain);
                }
            })
            .toList()
            .subscribeOn(Schedulers.newThread())
            .subscribe(new SaveProductSubscriber());
    }

    private RequestParams getGatewayProductParam(String etalaseId) {
        RequestParams params = RequestParams.create();
        params.putString(SHOP_ID, SessionHandler.getShopID(context));
        params.putString(START_OFFSET, 1 + "");
        params.putString(ROW_OFFSET, defaultRowPerPage + "");
        params.putString(ETALASE, etalaseId);
        params.putInt(DATA_PER_ROW, defaultRowPerPage);
        return params;
    }

    private class SaveProductSubscriber extends Subscriber<List<DataStatus>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(List<DataStatus> statuses) {
            for(DataStatus dataStatus: statuses) {
                Log.d("o2o", "product data saved : " + dataStatus.getStatus() + " | " + dataStatus.getMessage());
            }
        }
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
