package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/10/17.
 */

public class TopAdsCreateDetailShopUseCase extends TopAdsSaveDetailShopUseCase {

    @Inject
    public TopAdsCreateDetailShopUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsShopAdsRepository topAdsShopAdsRepository) {
        super(threadExecutor, postExecutionThread, topAdsShopAdsRepository);
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.createDetailShop((TopAdsDetailShopDomainModel) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD));
    }
}
