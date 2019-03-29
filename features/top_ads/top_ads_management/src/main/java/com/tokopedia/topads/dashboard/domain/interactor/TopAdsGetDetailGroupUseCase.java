package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGetDetailGroupUseCase extends UseCase<TopAdsDetailGroupDomainModel> {

    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    @Inject
    public TopAdsGetDetailGroupUseCase(TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super();
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailGroupDomainModel> createObservable(RequestParams requestParams) {
        return topAdsGroupAdsRepository.getDetailGroup(
                requestParams.getString(TopAdsNetworkConstant.PARAM_GROUP_ID, ""));
    }

    public static RequestParams createRequestParams(String adId){
        RequestParams params = RequestParams.create();
        params.putString(TopAdsNetworkConstant.PARAM_GROUP_ID, adId);
        return params;
    }
}
