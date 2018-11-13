package com.tokopedia.saldodetails.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.saldodetails.response.model.GqlDepositSummaryResponse;
import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DepositCacheInteractorImpl implements DepositCacheInteractor {

    private static final String TAG = DepositCacheInteractorImpl.class.getSimpleName();
    private static final String CACHE_DEPOSIT = "CACHE_DEPOSIT";
    private static final String CACHE_USABLE_SALDO_BALANCE = "CACHE_USABLE_SALDO_BALANCE";
    private CacheManager cacheManager;

    public DepositCacheInteractorImpl(Context context) {
        cacheManager = ((SaldoDetailsRouter) context.getApplicationContext()).getGlobalCacheManager();
    }

    @Override
    public void getSummaryDepositCache(final GetSummaryDepositCacheListener listener) {
        Observable.just(CACHE_DEPOSIT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(s -> getConvertObjData(cacheManager.get(s), GqlDepositSummaryResponse.class))
                .subscribe(new Subscriber<GqlDepositSummaryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(GqlDepositSummaryResponse result) {
                        try {
                            listener.onSuccess(result);
                        } catch (Exception e) {
                            listener.onError(e);
                        }
                    }
                });
    }

    @Override
    public void getUsableSaldoBalanceCache(GetUsableSaldoBalanceCacheListener listener) {
        Observable.just(CACHE_USABLE_SALDO_BALANCE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(s -> getConvertObjData(cacheManager.get(s), GqlSaldoBalanceResponse.class))
                .subscribe(new Subscriber<GqlSaldoBalanceResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(GqlSaldoBalanceResponse result) {
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
    public void setSummaryDepositCache(GqlDepositSummaryResponse result) {
        Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(gqlDepositSummaryResponse -> {
                    cacheManager.save(CACHE_DEPOSIT, CacheUtil.convertModelToString(gqlDepositSummaryResponse,
                            new TypeToken<GqlDepositSummaryResponse>() {
                            }.getType()), 900);
                    return true;
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
    public void setUsableSaldoBalanceCache(GqlSaldoBalanceResponse gqlSaldoBalanceResponse) {
        Observable.just(gqlSaldoBalanceResponse)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> {
                    cacheManager.save(CACHE_USABLE_SALDO_BALANCE, CacheUtil.convertModelToString(result,
                            new TypeToken<GqlSaldoBalanceResponse>() {
                            }.getType()), 900);
                    return true;
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

