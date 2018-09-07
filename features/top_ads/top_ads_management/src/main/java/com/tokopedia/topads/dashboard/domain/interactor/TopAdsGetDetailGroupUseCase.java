package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGetDetailGroupUseCase extends UseCase<TopAdsDetailGroupDomainModel> {

    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    @Inject
    public TopAdsGetDetailGroupUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
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
