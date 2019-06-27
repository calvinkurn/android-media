package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsSaveDetailProductUseCase extends UseCase<TopAdsDetailProductDomainModel> {

    private final TopAdsProductAdsRepository topAdsProductAdsRepository;

    @Inject
    public TopAdsSaveDetailProductUseCase(TopAdsProductAdsRepository topAdsProductAdsRepository) {
        super();
        this.topAdsProductAdsRepository = topAdsProductAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailProductDomainModel> createObservable(RequestParams requestParams) {
        return topAdsProductAdsRepository.saveDetail((TopAdsDetailProductDomainModel) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD));
    }

    public static RequestParams createRequestParams(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_AD, topAdsDetailProductDomainModel);
        return params;
    }
}