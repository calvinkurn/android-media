package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailGroupMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditGroupPresenterImpl<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenterImpl<T> implements TopAdsDetailEditGroupPresenter<T> {

    protected TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase;
    protected TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase;
    protected TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase;

    public TopAdsDetailEditGroupPresenterImpl(TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                              TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                              TopAdsProductListUseCase topAdsProductListUseCase,
                                              TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase) {
        super(topAdsProductListUseCase);
        this.topAdsGetSuggestionUseCase = topAdsGetSuggestionUseCase;
        this.topAdsGetDetailGroupUseCase = topAdsGetDetailGroupUseCase;
        this.topAdsSaveDetailGroupUseCase = topAdsSaveDetailGroupUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel) {
        topAdsSaveDetailGroupUseCase.execute(TopAdsSaveDetailGroupUseCase.createRequestParams(
                TopAdDetailGroupMapper.convertViewToDomain(topAdsDetailGroupViewModel)),
                getSaveGroupSubscriber());
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

    public void getTopAdsSuggestionBid(GetSuggestionBody getSuggestionBody){
        topAdsGetSuggestionUseCase.execute(TopAdsGetSuggestionUseCase.createRequestParams(getSuggestionBody), new Subscriber<GetSuggestionResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GetSuggestionResponse getSuggestionResponse) {

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
    }


}