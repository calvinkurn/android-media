package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.common.model.shopmodel.ShopModel;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.data.repository.ShopInfoRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 10/23/17.
 */

public class TopAdsGetSuggestionUseCase extends UseCase<GetSuggestionResponse> {
    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public TopAdsGetSuggestionUseCase(TopAdsGroupAdsRepository topAdsGroupAdsRepository, ShopInfoRepository shopInfoRepository) {
        super();
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<GetSuggestionResponse> createObservable(final RequestParams requestParams) {
        return shopInfoRepository.getShopInfo().flatMap(new Func1<ShopModel, Observable<GetSuggestionResponse>>() {
            @Override
            public Observable<GetSuggestionResponse> call(ShopModel shopModel) {
                return topAdsGroupAdsRepository.getSuggestion((GetSuggestionBody) requestParams.getObject(TopAdsNetworkConstant.PARAM_SUGGESTION), shopModel.getInfo().getShopId());
            }
        });
    }

    public static RequestParams createRequestParams(GetSuggestionBody getSuggestionBody){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_SUGGESTION, getSuggestionBody);
        return params;
    }
}
