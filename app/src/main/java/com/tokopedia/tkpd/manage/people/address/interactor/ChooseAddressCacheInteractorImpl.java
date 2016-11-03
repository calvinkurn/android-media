package com.tokopedia.tkpd.manage.people.address.interactor;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.tkpd.database.CacheUtil;
import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.manage.people.address.model.ChooseAddress.ChooseAddressResponse;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressCacheInteractorImpl implements ChooseAddressCacheInteractor {

    private static final String TAG = ChooseAddressCacheInteractorImpl.class.getSimpleName();
    private static final String CACHE_CHOOSE_ADDRESS= "CACHE_CHOOSE_ADDRESS";
    private GlobalCacheManager cacheManager;


    @Override
    public void getAddressesCache(final GetAddressesCacheListener listener) {
        Observable.just(CACHE_CHOOSE_ADDRESS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, ChooseAddressResponse>() {
                    @Override
                    public ChooseAddressResponse call(String s) {
                        return cacheManager.getConvertObjData(s, ChooseAddressResponse.class);
                    }
                })
                .subscribe(new Subscriber<ChooseAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(ChooseAddressResponse result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void setAddressesCache(ChooseAddressResponse result) {
        Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ChooseAddressResponse, Boolean>() {
                    @Override
                    public Boolean call(ChooseAddressResponse summaryWithdraw) {

                        GlobalCacheManager cache = new GlobalCacheManager();

                        cache.setKey(CACHE_CHOOSE_ADDRESS);
                        cache.setValue(CacheUtil.convertModelToString(summaryWithdraw,
                                new TypeToken<ChooseAddressResponse>() {
                                }.getType()));
                        cache.store();


                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }
}
