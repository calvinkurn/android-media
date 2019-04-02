package com.tokopedia.topads.group.domain.usecase;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.group.domain.repository.TopAdsGroupAdRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 14/05/18.
 */

public class TopAdsSearchGroupAdUseCase extends UseCase<List<GroupAd>> {
    private final TopAdsGroupAdRepository topAdsGroupAdRepository;

    @Inject
    public TopAdsSearchGroupAdUseCase(TopAdsGroupAdRepository topAdsGroupAdRepository) {
        this.topAdsGroupAdRepository = topAdsGroupAdRepository;
    }

    @Override
    public Observable<List<GroupAd>> createObservable(RequestParams requestParams) {
        return topAdsGroupAdRepository.searchGroupAd(requestParams);
    }

    public static RequestParams createRequestParams(String keyword, String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        requestParams.putString(TopAdsNetworkConstant.PARAM_KEYWORD, keyword);
        return requestParams;
    }
}
