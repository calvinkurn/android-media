package com.tokopedia.digital.widget.interactor;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.tokocash.TokoCashService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;
import com.tokopedia.digital.widget.domain.IDigitalCategoryListRepository;
import com.tokopedia.digital.widget.model.DigitalCategoryItemData;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListInteractor implements IDigitalCategoryListInteractor {
    private final CompositeSubscription compositeSubscription;
    private final IDigitalCategoryListRepository digitalCategoryListRepository;
    private final TokoCashService tokoCashService;

    public DigitalCategoryListInteractor(CompositeSubscription compositeSubscription,
                                         IDigitalCategoryListRepository digitalCategoryListRepository,
                                         TokoCashService tokoCashService) {
        this.compositeSubscription = compositeSubscription;
        this.digitalCategoryListRepository = digitalCategoryListRepository;
        this.tokoCashService = tokoCashService;
    }

    @Override
    public void getDigitalCategoryItemDataList(Subscriber<List<DigitalCategoryItemData>> subscriber) {
        compositeSubscription.add(digitalCategoryListRepository.getDigitalCategoryItemDataList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void getTokoCashData(Subscriber<TokoCashBalanceData> subscriber) {
        Observable<TokoCashBalanceData> observable = Observable
                .concat(getObservableFetchCacheTokoCashData(),
                        getObservableFetchNetworkTokoCashData())
                .first(isTokoCashDataAvailable());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @NonNull
    private Observable<TokoCashBalanceData> getObservableFetchCacheTokoCashData() {
        return Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, TokoCashBalanceData>() {
                    @Override
                    public TokoCashBalanceData call(Boolean aBoolean) {
                        GlobalCacheManager cacheManager = new GlobalCacheManager();
                        return CacheUtil.convertStringToModel(cacheManager
                                        .getValueString(TkpdCache.Key.KEY_TOKOCASH_DATA),
                                new TypeToken<TokoCashBalanceData>() {
                                }.getType());
                    }
                }).onErrorReturn(new Func1<Throwable, TokoCashBalanceData>() {
                    @Override
                    public TokoCashBalanceData call(Throwable throwable) {
                        return null;
                    }
                });
    }

    @NonNull
    private Observable<TokoCashBalanceData> getObservableFetchNetworkTokoCashData() {
        return tokoCashService.getApi()
                .getTokoCash()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<TokoCashBalanceData>>() {
                    @Override
                    public Observable<TokoCashBalanceData> call(Response<TkpdResponse> topCashItemResponse) {
                        return Observable
                                .just(topCashItemResponse.body().convertDataObj(TokoCashBalanceData.class));
                    }
                });
    }

    @NonNull
    private Func1<TokoCashBalanceData, Boolean> isTokoCashDataAvailable() {
        return new Func1<TokoCashBalanceData, Boolean>() {
            @Override
            public Boolean call(TokoCashBalanceData tokoCashData) {
                return tokoCashData != null;
            }
        };
    }


}
