package com.tokopedia.topads.dashboard.view.presenter;

import android.util.Log;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMinimumBidUseCase;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsRemoveSourceTaggingUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewGroupPresenterImpl<T extends TopAdsDetailNewGroupView>
        extends TopAdsDetailEditGroupPresenterImpl<T>
        implements TopAdsDetailNewGroupPresenter<T> {

    private TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase;
    private TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase;

    public TopAdsDetailNewGroupPresenterImpl(TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase,
                                             TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                             TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                             TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                             TopAdsProductListUseCase topAdsProductListUseCase,
                                             TopAdsMinimumBidUseCase minimumBidUseCase,
                                             TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase,
                                             UserSessionInterface sessionInterface) {
        super(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase, topAdsProductListUseCase,
                minimumBidUseCase, topAdsGetSourceTaggingUseCase, sessionInterface);
        this.topAdsCreateNewGroupUseCase = topAdsCreateNewGroupUseCase;
        this.topAdsCreateDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
    }

    @Override
    public void saveAdExisting(String groupId, final List<TopAdsProductViewModel> topAdsProductViewModelList, final String source) {
        super.getDetailAd(groupId, new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(final TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel) {
                // get the latest domain from API, then pass it to re-save it
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
                        topAdsCreateDetailProductListUseCase.execute(
                                TopAdsCreateDetailProductListUseCase.createRequestParams(
                                        topAdsDetailGroupDomainModel,
                                        topAdsProductViewModelList,
                                        source
                                ),
                                getSaveProductSubscriber()
                        );
                    }
                });
            }
        });
    }


    private Subscriber<TopAdsDetailProductDomainModel> getSaveProductSubscriber(){
        return new Subscriber<TopAdsDetailProductDomainModel>() {
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
                getView().goToGroupDetail(domainModel.getGroupId());
            }
        };
    }


    @Override
    public void saveAdNew(final String groupName,
                          final TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                          final List<TopAdsProductViewModel> topAdsProductViewModelList, final String source) {

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
                topAdsCreateNewGroupUseCase.execute(
                        TopAdsCreateNewGroupUseCase.createRequestParams(groupName,
                                topAdsDetailProductViewModel, topAdsProductViewModelList,
                                source),
                        new Subscriber<TopAdsDetailGroupViewModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
                            }

                            @Override
                            public void onNext(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel) {
                                getView().onSaveAdSuccess(topAdsDetailGroupViewModel);
                                getView().goToGroupDetail(String.valueOf(topAdsDetailGroupViewModel.getGroupId()));
                            }
                        });
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsCreateNewGroupUseCase.unsubscribe();
        topAdsCreateDetailProductListUseCase.unsubscribe();
        topAdsGetSourceTaggingUseCase.unsubscribe();
    }

}