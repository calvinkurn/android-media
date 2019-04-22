package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.DataSuggestions;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.request.MinimumBidRequest;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMinimumBidUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditProductPresenterImpl<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenterImpl<T> implements TopAdsDetailEditProductPresenter<T> {

    private TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase;
    private TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase;
    private TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase;
    protected TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;
    private TopAdsEditPromoFragmentListener listener;
    private UserSessionInterface sessionInterface;
    private TopAdsMinimumBidUseCase minimumBidUseCase;

    public TopAdsDetailEditProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                                TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                                TopAdsProductListUseCase topAdsProductListUseCase,
                                                TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase,
                                                TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase,
                                                TopAdsMinimumBidUseCase minimumBidUseCase, UserSessionInterface sessionInterface) {
        super(topAdsProductListUseCase);
        this.sessionInterface = sessionInterface;
        this.minimumBidUseCase = minimumBidUseCase;
        this.topAdsGetSuggestionUseCase = topAdsGetSuggestionUseCase;
        this.topAdsGetDetailProductUseCase = topAdsGetDetailProductUseCase;
        this.topAdsSaveDetailProductUseCase = topAdsSaveDetailProductUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
    }



    @Override
    public void saveAd(final TopAdsDetailProductViewModel adViewModel) {
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
                adViewModel.setSource(source);
                topAdsSaveDetailProductUseCase.execute(TopAdsSaveDetailProductUseCase
                        .createRequestParams(TopAdDetailProductMapper.convertViewToDomain(adViewModel)),
                        new Subscriber<TopAdsDetailProductDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onSaveAdError(ViewUtils.getErrorMessage(e));
                    }

                    @Override
                    public void onNext(TopAdsDetailProductDomainModel domainModel) {
                        getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
                    }
                });
            }
        });
    }

    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailProductUseCase.execute(TopAdsGetDetailProductUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetDetailProductUseCase.unsubscribe();
        topAdsSaveDetailProductUseCase.unsubscribe();
        topAdsGetDetailProductUseCase.unsubscribe();;
    }

    @Override
    public void getBidInfo(String requestType, List<DataSuggestions> dataSuggestions, String source) {
        MinimumBidRequest request = new MinimumBidRequest();
        request.setSource(source);
        request.setShopId(Integer.parseInt(sessionInterface.getShopId()));
        request.setRequestType(requestType);
        request.setDataSuggestions(dataSuggestions);
        minimumBidUseCase.execute(minimumBidUseCase.getBidParams(request),
                new Subscriber<MinimumBidDomain.TopadsBidInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached()) {
                            getView().onBidInfoError(e);
                        }
                    }

                    @Override
                    public void onNext(MinimumBidDomain.TopadsBidInfo topadsBidInfo) {
                        if(isViewAttached()) {
                            getView().onBidInfoSuccess(topadsBidInfo);
                        }
                    }
                });

    }
}