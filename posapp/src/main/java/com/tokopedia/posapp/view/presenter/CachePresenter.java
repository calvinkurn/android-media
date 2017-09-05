package com.tokopedia.posapp.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.database.ProductSavedResult;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;
import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;
import com.tokopedia.posapp.view.Cache;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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

    private GetProductListUseCase getProductListUseCase;
    private StoreProductCacheUseCase storeProductCacheUseCase;
    private Cache.CallbackListener callbackListener;

    private CompositeSubscription compositeSubscription;

    // TODO: 9/5/17 this state is ugly, try somethin else
    private boolean isRequestNextProduct;
    private int productPage;

    @Inject
    public CachePresenter(@ApplicationContext Context context,
                          GetProductListUseCase getProductListUseCase,
                          StoreProductCacheUseCase storeProductCacheUseCase) {
        this.getProductListUseCase = getProductListUseCase;
        this.storeProductCacheUseCase = storeProductCacheUseCase;
        this.context = context;
        compositeSubscription = new CompositeSubscription();

        this.isRequestNextProduct = true;
        this.productPage = 1;
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

    private void getProduct() {
        compositeSubscription.add(
            Observable.defer(new Func0<Observable<ShopProductListDomain>>() {
                @Override
                public Observable<ShopProductListDomain> call() {
                    if(isRequestNextProduct) {
                        return getProductListUseCase.createObservable(getProductParam(productPage));
                    }

                    return Observable.error(null);
                }
            })
            .repeat()
            .subscribeOn(Schedulers.newThread())
            .subscribe(new ProductSubscriber())
        );
    }

    private RequestParams getProductParam(int page) {
        RequestParams params = AuthUtil.generateRequestParamsNetwork(context);
        params.putString(SHOP_ID, "1325661"); // TODO: 9/4/17 temporary
        params.putString(SHOP_DOMAIN, SessionHandler.getShopDomain(context));
        params.putString(PAGE, String.valueOf(page));
        params.putString(KEYWORD, "");
        params.putString(ETALASE_ID, "etalase");
        params.putString(ORDER_BY, "0");
        params.putString(PER_PAGE, "5");
        params.putString(WHOLESALE, "1");

        return params;
    }

    private void getInstallmentTerms() {

    }

    private void getBankList() {

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
