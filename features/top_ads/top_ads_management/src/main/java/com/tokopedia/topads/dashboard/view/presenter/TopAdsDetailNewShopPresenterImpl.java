package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewShopPresenterImpl extends TopAdsDetailEditShopPresenterImpl implements TopAdsDetailNewShopPresenter {

    private TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase;

    public TopAdsDetailNewShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase, TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                            TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase,
                                            TopAdsProductListUseCase topAdsProductListUseCase,
                                            TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        super(topAdsGetDetailShopUseCase, topAdsSaveDetailShopUseCase, topAdsProductListUseCase, topAdsGetSourceTaggingUseCase);
        this.topAdsCreateDetailShopUseCase = topAdsCreateDetailShopUseCase;
    }

    @Override
    public void saveAd(final TopAdsDetailShopViewModel viewModel) {
        topAdsGetSourceTaggingUseCase.execute(new Subscriber<TopAdsSourceTaggingModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TopAdsSourceTaggingModel topAdsSourceTaggingModel) {
                String source = TopAdsNetworkConstant.VALUE_SOURCE_ANDROID;

                if (topAdsSourceTaggingModel != null){
                    source = topAdsSourceTaggingModel.getSource();
                }
                viewModel.setSource(source);
                topAdsCreateDetailShopUseCase.execute(TopAdsCreateDetailShopUseCase.createRequestParams(
                        TopAdDetailProductMapper.convertViewToDomain(viewModel)), getSubscriberSaveShop());
            }
        });
    }
}