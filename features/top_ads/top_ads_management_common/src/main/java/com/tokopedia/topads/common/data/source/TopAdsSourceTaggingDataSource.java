package com.tokopedia.topads.common.data.source;

import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.common.data.TopAdsSourceTaggingModel;
import com.tokopedia.usecase.RequestParams;

import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsSourceTaggingDataSource {
    private TopAdsSourceTaggingLocal topAdsSourceTaggingLocal;

    public TopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal) {
        this.topAdsSourceTaggingLocal = topAdsSourceTaggingLocal;
    }

    public Observable<Void> save(final RequestParams requestParams){
        final TopAdsSourceTaggingModel data =
                new TopAdsSourceTaggingModel(requestParams.getString(TopAdsConstant.PARAM_KEY_SOURCE, null),
                        requestParams.getString(TopAdsConstant.PARAM_KEY_TIMESTAMP, null));
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return topAdsSourceTaggingLocal
                        .savingSource(data);
            }
        });

    }

    public Observable<Void> remove(){
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return topAdsSourceTaggingLocal.deleteSource();
            }
        });
    }

    public Observable<String> getSource(){
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return topAdsSourceTaggingLocal.getSource();
            }
        });
    }
}
