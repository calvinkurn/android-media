package com.tokopedia.core.shopinfo.interactor;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by brilliant.oka on 26/04/17.
 */

public class ShopProductCacheInteractorImpl implements ShopProductCacheInteractor {

    private final String TAG = ShopProductCacheInteractor.class.getSimpleName();
    private final String CACHE_SHOP_PRODUCT = "CACHE_SHOP_PRODUCT";

    @Override
    public void getAllShopProductCache(final GetShopProductCacheListener listener) {
        Observable.just(CACHE_SHOP_PRODUCT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, ProductModel>() {
                    @Override
                    public ProductModel call(String s) {
                        GlobalCacheManager cacheManager = new GlobalCacheManager();
                        return cacheManager.getConvertObjData(s, ProductModel.class);
                    }
                })
                .subscribe(new Subscriber<ProductModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(ProductModel result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void getLimitedShopProductCache(final int limit, final GetShopProductCacheListener listener) {
        Observable.just(CACHE_SHOP_PRODUCT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, ProductModel>() {
                    @Override
                    public ProductModel call(String s) {
                        GlobalCacheManager cacheManager = new GlobalCacheManager();

                        ProductModel model = cacheManager.getConvertObjData(s, ProductModel.class);
                        java.util.List<List> list = new ArrayList<>();
                        for(int i = 0; i < model.list.size(); i++) {
                            if (i>=limit) break;
                            list.add(model.list.get(i));
                        }
                        model.list.clear();
                        model.list.addAll(list);

                        return model;
                    }
                })
                .subscribe(new Subscriber<ProductModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(ProductModel result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void setShopProductCache(ProductModel model) {
        Observable.just(model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ProductModel, Boolean>() {
                    @Override
                    public Boolean call(ProductModel product) {
                        Log.i(TAG, "Start to storing the cache.....");

                        GlobalCacheManager cacheManager = new GlobalCacheManager();

                        cacheManager.setKey(CACHE_SHOP_PRODUCT);
                        cacheManager.setValue(CacheUtil.convertModelToString(
                                product,
                                new TypeToken<ProductModel>() {}.getType()
                        ));
                        cacheManager.store();

                        Log.i(TAG, "End of storing the cache.....");

                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    @Override
    public void deleteShopProductCache() {
        Observable.just(CACHE_SHOP_PRODUCT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        GlobalCacheManager cacheManager = new GlobalCacheManager();
                        cacheManager.delete(s);
                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Boolean result) {

                    }
                });
    }
}
