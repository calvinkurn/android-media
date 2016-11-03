package com.tokopedia.tkpd.shopinfo.interactor;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.tkpd.database.CacheUtil;
import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.deposit.interactor.DepositCacheInteractorImpl;
import com.tokopedia.tkpd.shopinfo.models.shopfavoritedmodel.ShopFavoritedResponse;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Alifa on 10/6/2016.
 */
public class ShopFavoritedCacheInteractorImpl implements ShopFavoritedCacheInteractor {

    private static final String TAG = DepositCacheInteractorImpl.class.getSimpleName();
    private static final String CACHE_FAVORITED = "CACHE_FAVORITED";
    private GlobalCacheManager cacheManager;

    @Override
    public void getShopFavoritedCache(final GetShopFavoritedCacheListener listener) {
        Observable.just(CACHE_FAVORITED)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, ShopFavoritedResponse>() {
                    @Override
                    public ShopFavoritedResponse call(String s) {
                        return cacheManager.getConvertObjData(s, ShopFavoritedResponse.class);
                    }
                })
                .subscribe(new Subscriber<ShopFavoritedResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(ShopFavoritedResponse result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void setShopFavoritedCache(ShopFavoritedResponse result) {
        Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ShopFavoritedResponse, Boolean>() {
                    @Override
                    public Boolean call(ShopFavoritedResponse summaryWithdraw) {
                        Log.i(TAG, "Start to storing the cache.....");

                        GlobalCacheManager cache = new GlobalCacheManager();

                        cache.setKey(CACHE_FAVORITED);
                        cache.setValue(CacheUtil.convertModelToString(summaryWithdraw,
                                new TypeToken<ShopFavoritedResponse>() {
                                }.getType()));
                        cache.store();

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
}
