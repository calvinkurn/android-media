package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/3/17.
 */

public class TopAdsCreateDetailProductListUseCase extends UseCase<TopAdsDetailProductDomainModel> {

    private final TopAdsProductAdsRepository topAdsProductAdsRepository;

    private static final String PRODUCT_AD_TYPE = "1";

    @Inject
    public TopAdsCreateDetailProductListUseCase(TopAdsProductAdsRepository topAdsProductAdsRepository) {
        super();
        this.topAdsProductAdsRepository = topAdsProductAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailProductDomainModel> createObservable(RequestParams requestParams) {
        return topAdsProductAdsRepository.saveDetailListProduct((List<TopAdsDetailProductDomainModel>) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD),
                requestParams.getString(TopAdsExtraConstant.EXTRA_SOURCE, "")
                );
    }

    public static RequestParams createRequestParams(List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels, String source){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_AD, topAdsDetailProductDomainModels);
        params.putString(TopAdsExtraConstant.EXTRA_SOURCE, source);
        return params;
    }

    public static RequestParams createRequestParams(TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel ,
                                                    List<TopAdsProductViewModel> topAdsProductViewModelList,
                                                    String source) {
        List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels = new ArrayList<>();
        for(TopAdsProductViewModel topAdsProductViewModel : topAdsProductViewModelList){
            TopAdsDetailGroupDomainModel detailGroupDomainModel = topAdsDetailGroupDomainModel.copy();
            String adId = String.valueOf(topAdsProductViewModel.getAdId());
            String itemId = String.valueOf( topAdsProductViewModel.getId() );
            detailGroupDomainModel.setAdId(adId);
            detailGroupDomainModel.setItemId(itemId);
            detailGroupDomainModel.setAdType(PRODUCT_AD_TYPE);
            topAdsDetailProductDomainModels.add(detailGroupDomainModel);
        }

        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_AD, topAdsDetailProductDomainModels);
        params.putString(TopAdsExtraConstant.EXTRA_SOURCE, source);
        return params;
    }
}
