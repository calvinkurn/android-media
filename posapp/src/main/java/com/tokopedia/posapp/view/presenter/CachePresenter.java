package com.tokopedia.posapp.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.domain.model.DataStatus;
import com.tokopedia.posapp.domain.model.ListDomain;
import com.tokopedia.posapp.domain.model.bank.BankDomain;
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

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 8/29/17.
 */

public class CachePresenter implements Cache.Presenter {
    private static final String SHOP_ID = "shop_id";
    private static final String SHOP_DOMAIN = "shop_domain";
    private static final String PAGE = "page";
    private static final String KEYWORD = "keyword";
    private static final String ETALASE_ID = "etalase_id";
    private static final String ORDER_BY = "order_by";
    private static final String PER_PAGE = "per_page";
    private static final String WHOLESALE = "wholesale";
    public static final String ROW = "row";
    public static final String START = "start";
    public static final String ETALASE = "etalase";

    private Context context;
    private GetProductListUseCase getProductListUseCase;
    private StoreProductCacheUseCase storeProductCacheUseCase;
    private GetBankUseCase getBankUseCase;
    private StoreBankUsecase storeBankUsecase;
    private GetEtalaseUseCase getEtalaseUseCase;
    private StoreEtalaseCacheUseCase storeEtalaseCacheUseCase;
    private GetEtalaseCacheUseCase getEtalaseCacheUseCase;
    private GetAllProductUseCase getAllProductUseCase;

    private Cache.CallbackListener callbackListener;

    // TODO: 9/5/17 this state is ugly, try somethin else
    private boolean isRequestNextProduct;
    private int productPage;

    private int defaultRowPerPage = 10;

    @Inject
    public CachePresenter(@ApplicationContext Context context,
                          GetProductListUseCase getProductListUseCase,
                          StoreProductCacheUseCase storeProductCacheUseCase,
                          GetBankUseCase getBankUseCase,
                          StoreBankUsecase storeBankUsecase,
                          GetEtalaseUseCase getEtalaseUseCase,
                          StoreEtalaseCacheUseCase storeEtalaseCacheUseCase,
                          GetEtalaseCacheUseCase getEtalaseCacheUseCase,
                          GetAllProductUseCase getAllProductUseCase
    ) {
        this.context = context;
        this.getProductListUseCase = getProductListUseCase;
        this.storeProductCacheUseCase = storeProductCacheUseCase;
        this.getBankUseCase = getBankUseCase;
        this.storeBankUsecase = storeBankUsecase;
        this.getEtalaseUseCase = getEtalaseUseCase;
        this.storeEtalaseCacheUseCase = storeEtalaseCacheUseCase;
        this.getEtalaseCacheUseCase = getEtalaseCacheUseCase;
        this.getAllProductUseCase = getAllProductUseCase;

        initState();
    }

    @Override
    public void getData() {
        getEtalase();
//        getProduct();
        getBankList();
    }

    @Override
    public void setCallbackListener(Cache.CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    public void onDestroy() {

    }

    private void initState() {
        this.isRequestNextProduct = true;
        this.productPage = 1;
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
                    return getAllProductUseCase.createObservable(getGatewayProductParam(1, etalaseDomain.getEtalaseId()));
                }
            })
            .flatMap(new Func1<ProductListDomain, Observable<DataStatus>>() {
                @Override
                public Observable<DataStatus> call(ProductListDomain productListDomain) {
                    return storeProductCacheUseCase.createObservable(productListDomain);
                }
            })
            .subscribeOn(Schedulers.newThread())
            .subscribe(new Subscriber<DataStatus>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(DataStatus dataStatus) {
                    Log.d("o2o", "product data saved : " + dataStatus.getStatus() + " | " + dataStatus.getMessage());
                }
            });
    }

//    private void getProduct() {
//        Observable.defer(new Func0<Observable<ProductListDomain>>() {
//            @Override
//            public Observable<ProductListDomain> call() {
//                if(isRequestNextProduct) {
//                    return getProductListUseCase.createObservable(getGatewayProductParam(productPage));
//                }
//
//                return Observable.error(null);
//            }
//        })
//        .repeat()
//        .subscribeOn(Schedulers.newThread())
//        .subscribe(new ProductSubscriber());
//    }

    private RequestParams getGatewayProductParam(int page) {
        RequestParams params = RequestParams.create();
        params.putString(SHOP_ID, SessionHandler.getShopID(context));
        params.putInt(START, 1 + (defaultRowPerPage * (page - 1)));
        params.putInt(ROW, defaultRowPerPage);
        return params;
    }

    private RequestParams getGatewayProductParam(int page, String etalaseId) {
        RequestParams params = RequestParams.create();
        params.putString(SHOP_ID, SessionHandler.getShopID(context));
        params.putInt(START, 1 + (defaultRowPerPage * (page - 1)));
        params.putInt(ROW, defaultRowPerPage);
        params.putString(ETALASE, etalaseId);
        return params;
    }

    private class ProductSubscriber extends Subscriber<ProductListDomain> {
        @Override
        public void onCompleted() {
            Log.d("oka", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            if(e != null ) e.printStackTrace();
        }

        @Override
        public void onNext(ProductListDomain productListDomain) {
            if(productListDomain.getNextUri() != null && !productListDomain.getNextUri().isEmpty()) {
                productPage++;
                isRequestNextProduct = true;
            } else {
                productPage = 1;
                isRequestNextProduct = false;
            }

            storeProductCacheUseCase.createObservable(productListDomain).subscribe(new SaveProductSubscriber());
            onCompleted();
        }
    }

    private class SaveProductSubscriber extends Subscriber<DataStatus> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(DataStatus dataStatus) {
            Log.d("oka save result", String.valueOf(dataStatus.getMessage()));
        }
    }

    private void getBankList() {
        getBankUseCase
                .createObservable(null)
                .observeOn(Schedulers.newThread())
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
