package com.tokopedia.topads.dashboard.view.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditShopPresenterImpl<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenterImpl<T> implements TopAdsDetailEditShopPresenter<T> {

    private TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase;
    private TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase;
    protected TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;

    public TopAdsDetailEditShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase,
                                             TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                             TopAdsProductListUseCase topAdsProductListUseCase,
                                             TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        super(topAdsProductListUseCase);
        this.topAdsGetDetailShopUseCase = topAdsGetDetailShopUseCase;
        this.topAdsSaveDetailShopUseCase = topAdsSaveDetailShopUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
    }

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
                topAdsSaveDetailShopUseCase.execute(TopAdsSaveDetailShopUseCase
                                .createRequestParams(TopAdDetailProductMapper.convertViewToDomain(viewModel)),
                        getSubscriberSaveShop());
            }
        });

    }

    @NonNull
    protected Subscriber<TopAdsDetailShopDomainModel> getSubscriberSaveShop() {
        return new Subscriber<TopAdsDetailShopDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailShopDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        };
    }

    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailShopUseCase.execute(TopAdsGetDetailShopUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailShopDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailShopDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }

    @Override
    public void getProductDetail(String productId) {
        // no op, shop has no product id
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetDetailShopUseCase.unsubscribe();
        topAdsSaveDetailShopUseCase.unsubscribe();
    }
}