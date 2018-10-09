package com.tokopedia.topads.sourcetagging.data.source;

import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.usecase.RequestParams;

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
                new TopAdsSourceTaggingModel(requestParams
                        .getString(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE, null),
                        System.currentTimeMillis());

        return topAdsSourceTaggingLocal.savingSource(data);
    }

    public Observable<Void> remove(){
        return topAdsSourceTaggingLocal.deleteSource();
    }

    public Observable<String> getSource(){
        return topAdsSourceTaggingLocal.getSource();
    }
}
