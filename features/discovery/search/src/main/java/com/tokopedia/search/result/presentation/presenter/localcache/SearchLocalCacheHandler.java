package com.tokopedia.search.result.presentation.presenter.localcache;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.newdiscovery.base.DefaultSearchSubscriber;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchLocalCacheHandler {

    private Context context;

    public SearchLocalCacheHandler(Context context) {
        this.context = context;
    }

    // Save DynamicFilterModel locally as temporary solution,
    // to prevent TransactionTooLargeException when opening RevampedDynamicFilterActivity
    // This method will not be tested with Unit Test
    public void saveDynamicFilterModelLocally(String screenNameId, DynamicFilterModel dynamicFilterModel) {
        Type listType = new TypeToken<List<Filter>>() { }.getType();
        Gson gson = new Gson();
        String filterData = gson.toJson(dynamicFilterModel.getData().getFilter(), listType);

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                DynamicFilterDbManager.store(context, screenNameId, filterData);
                subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
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
