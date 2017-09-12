package com.tokopedia.posapp.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.domain.model.result.BankSavedResult;
import com.tokopedia.posapp.domain.model.result.ProductSavedResult;
import com.tokopedia.posapp.domain.model.bank.BankInstallmentDomain;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;
import com.tokopedia.posapp.domain.usecase.GetBankInstallmentUseCase;
import com.tokopedia.posapp.domain.usecase.GetShopProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreBankInstallmentCacheUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;
import com.tokopedia.posapp.view.Cache;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.posapp.domain.usecase.StoreBankInstallmentCacheUseCase.BANK_INSTALLMENT_DOMAIN;

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
    private GetBankInstallmentUseCase getBankInstallmentUseCase;
    private StoreBankInstallmentCacheUseCase storeBankInstallmentCacheUseCase;

    private Cache.CallbackListener callbackListener;
    private CompositeSubscription compositeSubscription;

    // TODO: 9/5/17 this state is ugly, try somethin else
    private boolean isRequestNextProduct;
    private int productPage;

    @Inject
    public CachePresenter(@ApplicationContext Context context,
                          GetShopProductListUseCase getShopProductListUseCase,
                          StoreProductCacheUseCase storeProductCacheUseCase,
                          GetBankInstallmentUseCase getBankInstallmentUseCase,
                          StoreBankInstallmentCacheUseCase storeBankInstallmentCacheUseCase
    ) {
        this.context = context;
        this.getShopProductListUseCase = getShopProductListUseCase;
        this.storeProductCacheUseCase = storeProductCacheUseCase;
        this.getBankInstallmentUseCase = getBankInstallmentUseCase;
        this.storeBankInstallmentCacheUseCase = storeBankInstallmentCacheUseCase;

        compositeSubscription = new CompositeSubscription();

        initState();
    }

    @Override
    public void getData() {
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
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    private void initState() {
        this.isRequestNextProduct = true;
        this.productPage = 1;
    }

    private void getProduct() {
//        compositeSubscription.add(
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
//        );
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
//        compositeSubscription.add(
            getBankInstallmentUseCase
                    .createObservable(RequestParams.create())
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
//        );
    }

    private Func1<BankInstallmentDomain, Observable<BankSavedResult>> storeBankToCache() {
        return new Func1<BankInstallmentDomain, Observable<BankSavedResult>>() {
            @Override
            public Observable<BankSavedResult> call(BankInstallmentDomain bankInstallmentDomain) {
                RequestParams requestParams = RequestParams.create();
                requestParams.putObject(
                        StoreBankInstallmentCacheUseCase.BANK_INSTALLMENT_DOMAIN,
                        bankInstallmentDomain
                );
                return storeBankInstallmentCacheUseCase.createObservable(requestParams);
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
