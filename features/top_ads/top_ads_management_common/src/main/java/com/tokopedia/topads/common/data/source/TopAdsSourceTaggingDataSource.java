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
                        requestParams.getLong(TopAdsConstant.PARAM_KEY_TIMESTAMP, 0));

        return topAdsSourceTaggingLocal.savingSource(data);
    }

    public Observable<Void> remove(){
        return topAdsSourceTaggingLocal.deleteSource();
    }

    public Observable<String> getSource(){
        return topAdsSourceTaggingLocal.getSource();
    }
}
