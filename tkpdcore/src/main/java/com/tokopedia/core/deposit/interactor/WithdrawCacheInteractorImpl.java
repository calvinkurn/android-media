package com.tokopedia.core.deposit.interactor;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.deposit.model.WithdrawForm;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nisie on 4/13/16.
 */
public class WithdrawCacheInteractorImpl implements WithdrawCacheInteractor {
    private static final String CACHE_WITHDRAW = "CACHE_WITHDRAW";

    private static final String TAG = DepositCacheInteractorImpl.class.getSimpleName();
    private GlobalCacheManager cacheManager;

    public WithdrawCacheInteractorImpl() {
        this.cacheManager = new GlobalCacheManager();
    }

    @Override
    public void getWithdrawFormCache(final GetWithdrawFormCacheListener listener) {
        Observable.just(CACHE_WITHDRAW)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, WithdrawForm>() {
                    @Override
                    public WithdrawForm call(String s) {
                        return cacheManager.getConvertObjData(s, WithdrawForm.class);
                    }
                })
                .subscribe(new Subscriber<WithdrawForm>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(WithdrawForm result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void setWithdrawFormCache(WithdrawForm result) {
        Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<WithdrawForm, Boolean>() {
                    @Override
                    public Boolean call(WithdrawForm withdrawForm) {
                        Log.i(TAG, "Start to storing the cache.....");

                        GlobalCacheManager cache = new GlobalCacheManager();

                        cache.setKey(CACHE_WITHDRAW);
                        cache.setValue(CacheUtil.convertModelToString(withdrawForm,
                                new TypeToken<WithdrawForm>() {
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
