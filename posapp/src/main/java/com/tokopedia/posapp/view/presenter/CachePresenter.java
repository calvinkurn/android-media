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
import com.tokopedia.posapp.domain.model.result.ProductSavedResult;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;
import com.tokopedia.posapp.domain.usecase.GetBankUseCase;
import com.tokopedia.posapp.domain.usecase.GetEtalaseUseCase;
import com.tokopedia.posapp.domain.usecase.GetShopProductListUseCase;
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

    private Context context;
    private GetShopProductListUseCase getShopProductListUseCase;
    private StoreProductCacheUseCase storeProductCacheUseCase;
    private GetBankUseCase getBankUseCase;
    private StoreBankUsecase storeBankUsecase;
    private GetEtalaseUseCase getEtalaseUseCase;
    private StoreEtalaseCacheUseCase storeEtalaseCacheUseCase;

    private Cache.CallbackListener callbackListener;

    // TODO: 9/5/17 this state is ugly, try somethin else
    private boolean isRequestNextProduct;
    private int productPage;

    @Inject
    public CachePresenter(@ApplicationContext Context context,
                          GetShopProductListUseCase getShopProductListUseCase,
                          StoreProductCacheUseCase storeProductCacheUseCase,
                          GetBankUseCase getBankUseCase,
                          StoreBankUsecase storeBankUsecase,
                          GetEtalaseUseCase getEtalaseUseCase,
                          StoreEtalaseCacheUseCase storeEtalaseCacheUseCase
    ) {
        this.context = context;
        this.getShopProductListUseCase = getShopProductListUseCase;
        this.storeProductCacheUseCase = storeProductCacheUseCase;
        this.getBankUseCase = getBankUseCase;
        this.storeBankUsecase = storeBankUsecase;
        this.getEtalaseUseCase = getEtalaseUseCase;
        this.storeEtalaseCacheUseCase = storeEtalaseCacheUseCase;

        initState();
    }

    @Override
    public void getData() {
        getEtalase();
        getProduct();
        getBankList();
        getInstallmentTerms();
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

                    }

                    @Override
                    public void onNext(DataStatus dataStatus) {
                        Log.d("CachePresenter", dataStatus.getMessage());
                    }
                });
    }

    private void getProduct() {
        Observable.defer(new Func0<Observable<ShopProductListDomain>>() {
            @Override
            public Observable<ShopProductListDomain> call() {
                if(isRequestNextProduct) {
                    return getShopProductListUseCase.createObservable(getProductParam(productPage));
                }

                return Observable.error(null);
            }
        })
        .repeat()
        .subscribeOn(Schedulers.newThread())
        .subscribe(new ProductSubscriber());
    }

    private RequestParams getProductParam(int page) {
        RequestParams params = AuthUtil.generateRequestParamsNetwork(context);
        params.putString(SHOP_ID, SessionHandler.getShopID(context));
        params.putString(SHOP_DOMAIN, SessionHandler.getShopDomain(context));
        params.putString(PAGE, String.valueOf(page));
        params.putString(KEYWORD, "");
        params.putString(ETALASE_ID, "etalase");
        params.putString(ORDER_BY, "0");
        params.putString(PER_PAGE, "5");
        params.putString(WHOLESALE, "1");

        return params;
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

    private void getInstallmentTerms() {

    }

    private class ProductSubscriber extends Subscriber<ShopProductListDomain> {
        @Override
        public void onCompleted() {
            Log.d("oka", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            if(e != null ) e.printStackTrace();
        }

        @Override
        public void onNext(ShopProductListDomain shopProductListDomain) {
            if(shopProductListDomain.getNextUri() != null && !shopProductListDomain.getNextUri().isEmpty()) {
                productPage++;
                isRequestNextProduct = true;
            } else {
                productPage = 0;
                isRequestNextProduct = false;
            }

            storeProductCacheUseCase.createObservable(shopProductListDomain).subscribe(new SaveProductSubscriber());
            onCompleted();
        }
    }

    private class SaveProductSubscriber extends Subscriber<ProductSavedResult> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ProductSavedResult productSavedResult) {
            Log.d("oka save result", String.valueOf(productSavedResult.isStatus()));
        }
    }
}
