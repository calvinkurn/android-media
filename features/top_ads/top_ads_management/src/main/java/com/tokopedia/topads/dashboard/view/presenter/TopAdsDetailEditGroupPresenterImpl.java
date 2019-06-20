package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.DataSuggestions;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.request.MinimumBidRequest;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMinimumBidUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailGroupMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditGroupPresenterImpl<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenterImpl<T> implements TopAdsDetailEditGroupPresenter<T> {

    protected TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase;
    protected TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase;
    protected TopAdsMinimumBidUseCase minimumBidUseCase;
    protected TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;
    protected UserSessionInterface sessionInterface;

    public TopAdsDetailEditGroupPresenterImpl(TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                              TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                              TopAdsProductListUseCase topAdsProductListUseCase,
                                              TopAdsMinimumBidUseCase topAdsMinimumBidUseCase,
                                              TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase,
                                              UserSessionInterface sessionInterface) {
        super(topAdsProductListUseCase);
        this.minimumBidUseCase = topAdsMinimumBidUseCase;
        this.topAdsGetDetailGroupUseCase = topAdsGetDetailGroupUseCase;
        this.topAdsSaveDetailGroupUseCase = topAdsSaveDetailGroupUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
        this.sessionInterface = sessionInterface;
    }

    @Override
    public void saveAd(final TopAdsDetailGroupViewModel topAdsDetailGroupViewModel) {
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
                topAdsDetailGroupViewModel.setSource(source);
                topAdsSaveDetailGroupUseCase.execute(TopAdsSaveDetailGroupUseCase.createRequestParams(
                        TopAdDetailGroupMapper.convertViewToDomain(topAdsDetailGroupViewModel)),
                        getSaveGroupSubscriber());
            }
        });
    }

    /**
     * retrieve to populate the fields of groups ad detail to the view
     *
     * @param adId adId here is group ID
     */
    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailGroupUseCase.execute(
                TopAdsGetDetailGroupUseCase.createRequestParams(adId),
                getDetailAdSubscriber());
    }

    public void getDetailAd(String adId, Subscriber<TopAdsDetailGroupDomainModel> subscriber) {
        topAdsGetDetailGroupUseCase.execute(
                TopAdsGetDetailGroupUseCase.createRequestParams(adId),
                subscriber);
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

    private Subscriber<TopAdsDetailGroupDomainModel> getDetailAdSubscriber(){
        return new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailGroupDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailGroupMapper.convertDomainToView(domainModel));
            }
        };
    }

    private Subscriber<TopAdsDetailGroupDomainModel> getSaveGroupSubscriber(){
        return new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailGroupDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailGroupMapper.convertDomainToView(domainModel));
            }
        };
    }


    @Override
    public void detachView() {
        super.detachView();
        topAdsSaveDetailGroupUseCase.unsubscribe();
        topAdsGetDetailGroupUseCase.unsubscribe();
        topAdsGetSourceTaggingUseCase.unsubscribe();
    }


}