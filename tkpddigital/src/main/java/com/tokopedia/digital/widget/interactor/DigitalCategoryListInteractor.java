package com.tokopedia.digital.widget.interactor;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;
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
    public void getTokoCashData(Subscriber<TokoCashData> subscriber) {
        Observable<TokoCashData> observable = Observable
                .concat(getObservableFetchCacheTokoCashData(),
                        getObservableFetchNetworkTokoCashData())
                .first(isTokoCashDataAvailable());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @NonNull
    private Observable<TokoCashData> getObservableFetchCacheTokoCashData() {
        return Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, TokoCashData>() {
                    @Override
                    public TokoCashData call(Boolean aBoolean) {
                        GlobalCacheManager cacheManager = new GlobalCacheManager();
                        return CacheUtil.convertStringToModel(cacheManager
                                        .getValueString(TkpdCache.Key.KEY_TOKOCASH_DATA),
                                new TypeToken<TokoCashData>() {
                                }.getType());
                    }
                }).onErrorReturn(new Func1<Throwable, TokoCashData>() {
                    @Override
                    public TokoCashData call(Throwable throwable) {
                        return null;
                    }
                });
    }

    @NonNull
    private Observable<TokoCashData> getObservableFetchNetworkTokoCashData() {
        return tokoCashService.getApi()
                .getTokoCash()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<TokoCashData>>() {
                    @Override
                    public Observable<TokoCashData> call(Response<TkpdResponse> topCashItemResponse) {
                        return Observable
                                .just(topCashItemResponse.body().convertDataObj(TokoCashData.class));
                    }
                });
    }

    @NonNull
    private Func1<TokoCashData, Boolean> isTokoCashDataAvailable() {
        return new Func1<TokoCashData, Boolean>() {
            @Override
            public Boolean call(TokoCashData tokoCashData) {
                return tokoCashData != null;
            }
        };
    }


}
