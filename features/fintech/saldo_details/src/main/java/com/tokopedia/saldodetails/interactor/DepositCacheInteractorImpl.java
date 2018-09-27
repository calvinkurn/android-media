package com.tokopedia.saldodetails.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.saldodetails.response.model.SummaryWithdraw;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DepositCacheInteractorImpl implements DepositCacheInteractor {

    private static final String TAG = DepositCacheInteractorImpl.class.getSimpleName();
    private static final String CACHE_DEPOSIT = "CACHE_DEPOSIT";
    private CacheManager cacheManager;

    public DepositCacheInteractorImpl(Context context) {

        cacheManager = ((SaldoDetailsRouter) context.getApplicationContext()).getGlobalCacheManager();

//        this.cacheManager = new GlobalCacheManager();
    }

    @Override
    public void getSummaryDepositCache(final GetSummaryDepositCacheListener listener) {
        Observable.just(CACHE_DEPOSIT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, SummaryWithdraw>() {
                    @Override
                    public SummaryWithdraw call(String s) {
                        return getConvertObjData(cacheManager.get(s), SummaryWithdraw.class);
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


    public <T> T getConvertObjData(String s, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(s, clazz);
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

//                        GlobalCacheManager cache = new GlobalCacheManager();

                        cacheManager.save(CACHE_DEPOSIT, CacheUtil.convertModelToString(summaryWithdraw,
                                new TypeToken<SummaryWithdraw>() {
                                }.getType()), 900);
                        /*cache.setKey(CACHE_DEPOSIT);
                        cache.setValue(CacheUtil.convertModelToString(summaryWithdraw,
                                new TypeToken<SummaryWithdraw>() {
                                }.getType()));
                        cache.store();*/

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

