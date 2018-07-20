package com.tokopedia.topads.group.domain.usecase;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.group.domain.repository.TopAdsGroupAdRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsGetGroupAdUseCase extends UseCase<PageDataResponse<List<GroupAd>>> {
    private final TopAdsGroupAdRepository topAdsGroupAdRepository;

    @Inject
    public TopAdsGetGroupAdUseCase(TopAdsGroupAdRepository topAdsGroupAdRepository) {
        this.topAdsGroupAdRepository = topAdsGroupAdRepository;
    }

    @Override
    public Observable<PageDataResponse<List<GroupAd>>> createObservable(RequestParams requestParams) {
        return topAdsGroupAdRepository.getGroupAd(requestParams);
    }

    public static RequestParams createRequestParams(SearchAdRequest searchAdRequest){
        RequestParams requestParams = RequestParams.create();
        for (String key : searchAdRequest.getParams().keySet()){
            requestParams.putString(key, searchAdRequest.getParams().get(key));
        }

        return requestParams;
    }
}
