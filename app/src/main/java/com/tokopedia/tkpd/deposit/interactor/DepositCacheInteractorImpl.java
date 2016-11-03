package com.tokopedia.tkpd.deposit.interactor;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.tkpd.database.CacheUtil;
import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.deposit.model.SummaryWithdraw;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nisie on 4/11/16.
 */
public class DepositCacheInteractorImpl implements DepositCacheInteractor {

    private static final String TAG = DepositCacheInteractorImpl.class.getSimpleName();
    private static final String CACHE_DEPOSIT = "CACHE_DEPOSIT";
    private GlobalCacheManager cacheManager;

    public DepositCacheInteractorImpl() {
        this.cacheManager = new GlobalCacheManager();
    }

    @Override
    public void getSummaryDepositCache(final GetSummaryDepositCacheListener listener) {
        Observable.just(CACHE_DEPOSIT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, SummaryWithdraw>() {
                    @Override
                    public SummaryWithdraw call(String s) {
                        return cacheManager.getConvertObjData(s, SummaryWithdraw.class);
                    }
                })
                .subscribe(new Subscriber<SummaryWithdraw>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(SummaryWithdraw result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void setSummaryDepositCache(SummaryWithdraw result) {
        Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<SummaryWithdraw, Boolean>() {
                    @Override
                    public Boolean call(SummaryWithdraw summaryWithdraw) {
                        Log.i(TAG, "Start to storing the cache.....");

                        GlobalCacheManager cache = new GlobalCacheManager();

                        cache.setKey(CACHE_DEPOSIT);
                        cache.setValue(CacheUtil.convertModelToString(summaryWithdraw,
                                new TypeToken<SummaryWithdraw>() {
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
