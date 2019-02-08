package com.tokopedia.saldodetails.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;

public class DepositCacheInteractorImpl implements DepositCacheInteractor {

    private static final String TAG = DepositCacheInteractorImpl.class.getSimpleName();
    private static final String CACHE_DEPOSIT = "CACHE_DEPOSIT";
    private static final String CACHE_USABLE_BUYER_SALDO_BALANCE = "cache_usable_buyer_saldo_balance";
    private static final String CACHE_USABLE_SELLER_SALDO_BALANCE = "cache_usable_seller_saldo_balance";
    private static final int CACHE_TIME_LIMIT = 900;
    private CacheManager cacheManager;

    public DepositCacheInteractorImpl(Context context) {
        cacheManager = ((SaldoDetailsRouter) context.getApplicationContext()).getGlobalCacheManager();
    }

    /*@Override
    public void setSummaryDepositCache(GqlDepositSummaryResponse result) {
        Observable.just(result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(gqlDepositSummaryResponse -> {
                    cacheManager.save(CACHE_DEPOSIT, CacheUtil.convertModelToString(gqlDepositSummaryResponse,
                            new TypeToken<GqlDepositSummaryResponse>() {
                            }.getType()), CACHE_TIME_LIMIT);
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
                        listener.onSuccess(result);
                    }
                });
    }*/

    /*@Override
    public void setUsableBuyerSaldoBalanceCache(GqlSaldoBalanceResponse gqlSaldoBalanceResponse) {
        Observable.just(gqlSaldoBalanceResponse)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> {
                    cacheManager.save(CACHE_USABLE_BUYER_SALDO_BALANCE, CacheUtil.convertModelToString(result,
                            new TypeToken<GqlSaldoBalanceResponse>() {
                            }.getType()), CACHE_TIME_LIMIT);
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
    public void setUsableSellerSaldoBalanceCache(GqlSaldoBalanceResponse gqlSaldoBalanceResponse) {
        Observable.just(gqlSaldoBalanceResponse)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> {
                    cacheManager.save(CACHE_USABLE_SELLER_SALDO_BALANCE, CacheUtil.convertModelToString(result,
                            new TypeToken<GqlSaldoBalanceResponse>() {
                            }.getType()), CACHE_TIME_LIMIT);
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
    public void getUsableBuyerSaldoBalanceCache(GetUsableSaldoBalanceCacheListener listener) {
        Observable.just(CACHE_USABLE_BUYER_SALDO_BALANCE)
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
                        listener.onSuccess(result);
                    }
                });
    }

    @Override
    public void getUsableSellerSaldoBalanceCache(GetUsableSaldoBalanceCacheListener listener) {
        Observable.just(CACHE_USABLE_SELLER_SALDO_BALANCE)
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
                        listener.onSuccess(result);
                    }
                });
    }*/

    public <T> T getConvertObjData(String s, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(s, clazz);
    }

}

